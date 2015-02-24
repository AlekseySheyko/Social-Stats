package aleksey.sheyko.socialstats.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import aleksey.sheyko.socialstats.R;
import aleksey.sheyko.socialstats.app.adapters.ServiceAdapter;
import aleksey.sheyko.socialstats.database.AccountDataSource;
import aleksey.sheyko.socialstats.model.Account;

public class InstagramFragment extends Fragment {

    // Instagram auth urls
    public static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    public static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    public static final String API_URL = "https://api.instagram.com/v1";
    public static String CALLBACK_URL = "http://social-stats-demo.com/redirect";
    public static String GRANT_TYPE = "authorization_code";
    public static String RESPONSE_TYPE = "code";
    // Instagram client info
    public static String CLIENT_ID = "ea32999fda584584881945a9f5157f8e";
    public static String CLIENT_SECRET = "bb6c0b735e7d46b4a009fd3208c98f6d";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instagram, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button loginButton = (Button) view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(view);
            }
        });

        // List existing Twitter accounts
        AccountDataSource dataSource = new AccountDataSource(getActivity());
        ArrayList<Account> accounts = dataSource.read("Instagram");

        ServiceAdapter adapter = new ServiceAdapter(
                getActivity(), R.layout.list_item_service, accounts);

        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

    private void signIn(View view) {
        getActivity().getActionBar().hide();

//        // Form urls for Instagram authentication
//        String authUrlString = Constants.AUTH_URL
//                + "?client_id=" + Constants.CLIENT_ID
//                + "&redirect_uri=" + Constants.CALLBACK_URL
//                + "&response_type=code";
//
//        // Display the webview for user to authenticate
//        WebView webView = (WebView) view.findViewById(R.id.webview);
//        webView.setVisibility(View.VISIBLE);
//        webView.setVerticalScrollBarEnabled(false);
//        webView.setHorizontalScrollBarEnabled(false);
//        webView.setWebViewClient(new WebviewAuthClient(getActivity()));
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl(authUrlString);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setTitle("Instagram");
    }
}
