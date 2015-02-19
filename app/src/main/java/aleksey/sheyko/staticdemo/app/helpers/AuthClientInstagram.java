package aleksey.sheyko.staticdemo.app.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import javax.net.ssl.HttpsURLConnection;

import aleksey.sheyko.staticdemo.R;
import aleksey.sheyko.staticdemo.app.activities.SocialActivity;

public class AuthClientInstagram extends WebViewClient {

    private Context mContext;

    private String mTokenUrlString;
    private String mRequestToken;

    private final SharedPreferences mSharedPref;

    public AuthClientInstagram(Context context) {
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
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                outputStreamWriter.write("client_id=" + Constants.CLIENT_ID +
                        "&client_secret=" + Constants.CLIENT_SECRET +
                        "&grant_type=authorization_code" +
                        "&redirect_uri=" + Constants.CALLBACK_URL +
                        "&code=" + mRequestToken);
                outputStreamWriter.flush();
                String response = streamToString(httpsURLConnection.getInputStream());
                JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                String accessToken = jsonObject.getString("access_token"); // Here is your ACCESS TOKEN
                String id = jsonObject.getJSONObject("user").getString("id");
                String username = jsonObject.getJSONObject("user").getString("username"); //This is how you can get the user info. You can explore the JSON sent by Instagram as well to know what info you got in a response
                mSharedPref.edit()
                        .putString("access_token", accessToken)
                        .putString("id", id)
                        .putString("username", username)
                        .apply();
                mContext.startActivity(new Intent(mContext, SocialActivity.class)
                        .putExtra("username", username));
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
