package aleksey.sheyko.socialstats.app.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import aleksey.sheyko.socialstats.R;
import aleksey.sheyko.socialstats.rest.InstagramService;

public class InstagramFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instagram, container, false);
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        final String clientId = getString(R.string.instagram_client_id);
        final String redirectUrl = getString(R.string.instagram_redirect_url);

        Button loginButton = (Button) rootView.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(InstagramService.BASE_URL + "/oauth/authorize/?client_id=" + clientId
                        + "&redirect_uri=" + redirectUrl + "&response_type=code"));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set action bar title
        getActivity().getActionBar().setTitle("Instagram");
    }
}
