package aleksey.sheyko.socialstats.rest.instagram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import aleksey.sheyko.socialstats.R;
import aleksey.sheyko.socialstats.app.activities.MainActivity;
import aleksey.sheyko.socialstats.database.AccountDataSource;
import aleksey.sheyko.socialstats.rest.model.Account;
import aleksey.sheyko.socialstats.rest.model.DataSet;

public class WebviewAuthClient extends WebViewClient {

    private Context mContext;

    private String mTokenUrlString;
    private String mRequestToken;

    private final SharedPreferences mSharedPref;

    public WebviewAuthClient(Context context) {
        mContext = context;
        mSharedPref = mContext.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        mTokenUrlString = Constants.TOKEN_URL
                + "?client_id=" + Constants.CLIENT_ID
                + "&client_secret=" + Constants.CLIENT_SECRET
                + "&redirect_uri=" + Constants.CALLBACK_URL + "&grant_type=authorization_code";

        if (url.startsWith(Constants.CALLBACK_URL)) {
            String parts[] = url.split("=");
            mRequestToken = parts[1];  // This is the request token
            new GetTokenTask().execute();
            return true;
        }
        return false;
    }

    private class GetTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String accessToken = mSharedPref.getString("in_access_token", "");
                String id = mSharedPref.getString("in_user_id", "");

                if (accessToken.isEmpty() || id.isEmpty()) {
                    queryAccessToken();
                    return null;
                }

                // Start querying user data
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("api.instagram.com")
                        .appendPath("v1")
                        .appendPath("users")
                        .appendPath(id)
                        .appendQueryParameter("access_token", accessToken);
                String urlString = builder.build().toString();

                URL url = new URL(urlString);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) return null;

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                String response = buffer.toString();
                JSONObject responseObject = (JSONObject) new JSONTokener(response).nextValue();

                JSONObject dataObject = responseObject.getJSONObject("data");

                String username = dataObject.getString("username");
                String avatarUrl = dataObject.getString("profile_picture");
                JSONObject countsObject = dataObject.getJSONObject("counts");

                int media = countsObject.getInt("media");
                int following = countsObject.getInt("follows");
                int followers = countsObject.getInt("followed_by");

                List<DataSet> statsList = new ArrayList<>();
                statsList.add(new DataSet("followers", followers));
                statsList.add(new DataSet("media", media));
                statsList.add(new DataSet("following", following));

                Account account = new Account("Instagram", username, statsList, avatarUrl);

                AccountDataSource dataSource = new AccountDataSource(mContext);
                dataSource.create(account);

                mContext.startActivity(new Intent(mContext, MainActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public void queryAccessToken() throws Exception {
        URL url = new URL(mTokenUrlString);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
        outputStreamWriter.write("client_id=" + Constants.CLIENT_ID +
                "&client_secret=" + Constants.CLIENT_SECRET +
                "&grant_type=authorization_code" +
                "&redirect_uri=" + Constants.CALLBACK_URL +
                "&code=" + mRequestToken);
        outputStreamWriter.flush();
        String response = streamToString(urlConnection.getInputStream());
        JSONObject responseObject = (JSONObject) new JSONTokener(response).nextValue();

        String id = responseObject.getJSONObject("user").getString("id");
        String accessToken = responseObject.getString("access_token");
        mSharedPref.edit()
                .putString("in_user_id", id)
                .putString("in_access_token", accessToken)
                .commit();

        new GetTokenTask().execute();
    }

    private String streamToString(InputStream inputStream) {
        String string = "";
        try {
            if (inputStream != null) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream));

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    reader.close();
                } finally {
                    inputStream.close();
                }

                string = stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string;
    }
}
