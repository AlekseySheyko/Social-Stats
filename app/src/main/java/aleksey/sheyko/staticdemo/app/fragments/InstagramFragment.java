package aleksey.sheyko.staticdemo.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import aleksey.sheyko.staticdemo.R;
import aleksey.sheyko.staticdemo.app.helpers.AuthClientInstagram;
import aleksey.sheyko.staticdemo.app.helpers.Constants;

public class InstagramFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instagram, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Form urls for Instagram authentication
        String authUrlString = Constants.AUTH_URL
                + "?client_id=" + Constants.CLIENT_ID
                + "&redirect_uri=" + Constants.CALLBACK_URL
                + "&response_type=code";

        // Display the webview for user to authenticate
        WebView webView = (WebView) view.findViewById(R.id.webview);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new AuthClientInstagram(getActivity()));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(authUrlString);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setTitle("Instagram");
    }
}
