package aleksey.sheyko.staticdemo.models.instagram;

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

import aleksey.sheyko.staticdemo.R;
import aleksey.sheyko.staticdemo.app.activities.MainActivity;
import aleksey.sheyko.staticdemo.app.database.AccountDataSource;
import aleksey.sheyko.staticdemo.models.Account;
import aleksey.sheyko.staticdemo.models.AccountStats;

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
                String accessToken = responseObject.getString("access_token"); // Here is your ACCESS TOKEN
                String id = responseObject.getJSONObject("user").getString("id");
                String username = responseObject.getJSONObject("user").getString("username");

                // Start querying user data
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("api.instagram.com")
                        .appendPath("v1")
                        .appendPath("users")
                        .appendPath(id)
                        .appendQueryParameter("access_token", accessToken);
                String urlString = builder.build().toString();

                url = new URL(urlString);
                urlConnection = (HttpsURLConnection) url.openConnection();
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
                response = buffer.toString();
                responseObject = (JSONObject) new JSONTokener(response).nextValue();

                JSONObject dataObject = responseObject.getJSONObject("data");
                JSONObject countsObject = dataObject.getJSONObject("counts");

                int media = countsObject.getInt("media");
                int following = countsObject.getInt("follows");
                int followers = countsObject.getInt("followed_by");

                List<AccountStats> statsList = new ArrayList<>();
                statsList.add(new AccountStats("followers", followers));
                statsList.add(new AccountStats("media", media));
                statsList.add(new AccountStats("following", following));

                Account account = new Account("Instagram", username, statsList);

                AccountDataSource dataSource = new AccountDataSource(mContext);
                dataSource.create(account);

                mContext.startActivity(new Intent(mContext, MainActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}
