package aleksey.sheyko.staticdemo;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import aleksey.sheyko.staticdemo.helpers.AuthClientWebView;
import aleksey.sheyko.staticdemo.helpers.Constants;


public class AuthActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Form urls for Instagram authentication
        String authUrlString = Constants.AUTH_URL
                + "?client_id=" + Constants.CLIENT_ID
                + "&redirect_uri=" + Constants.CALLBACK_URL
                + "&response_type=code";

        // Display the webview for user to authenticate
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new AuthClientWebView(this));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(authUrlString);
    }
}
