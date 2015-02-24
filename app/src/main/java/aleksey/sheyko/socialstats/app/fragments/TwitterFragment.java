package aleksey.sheyko.socialstats.app.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

import aleksey.sheyko.socialstats.R;
import aleksey.sheyko.socialstats.app.activities.MainActivity;
import aleksey.sheyko.socialstats.app.adapters.ServiceAdapter;
import aleksey.sheyko.socialstats.database.AccountDataSource;
import aleksey.sheyko.socialstats.rest.model.Account;
import aleksey.sheyko.socialstats.rest.model.Stats;
import io.fabric.sdk.android.Fabric;


public class TwitterFragment extends Fragment {

    public static final String TAG = TwitterFragment.class.getSimpleName();

    private static final String TWITTER_KEY = "bmQCF0w32m1awcIOIua56yPqa";
    private static final String TWITTER_SECRET = "8XjePz2wz3TjuvRBuN5fk6O1WzthXYEEvB6AVk1xayVUq5hhnY";

    private TwitterLoginButton mTwitterButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_twitter, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView) view.findViewById(R.id.listview);
        populateList(listView);

        TwitterAuthConfig authConfig =
                new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(getActivity(), new Twitter(authConfig));

        Button loginButton = (Button) view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                login(view);
            }
        });
    }

    private void populateList(ListView listView) {
        // List existing Twitter accounts
        AccountDataSource dataSource = new AccountDataSource(getActivity());
        ArrayList<Account> accounts = dataSource.read("Twitter");

        ServiceAdapter adapter = new ServiceAdapter(
                getActivity(), R.layout.list_item_service, accounts);

        listView.setAdapter(adapter);
    }

    public void login(View v) {

        mTwitterButton = (TwitterLoginButton) v.findViewById(R.id.twitterLoginButton);
        mTwitterButton.setEnabled(true);
        mTwitterButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a
                // TwitterSession for making API calls
                TwitterApiClient apiClient = TwitterCore.getInstance().getApiClient(result.data);
                apiClient.getAccountService().verifyCredentials(false, false, new Callback<User>() {
                            @Override
                            public void success(Result<User> userResult) {
                                User user = userResult.data;

                                String username = user.screenName;
                                int followers = user.followersCount;
                                int tweets = user.listedCount;
                                int following = user.favouritesCount;
                                String avatarUrl = user.profileImageUrl;

                                saveAccount(username, followers, tweets, following, avatarUrl);

                                startActivity(new Intent(getActivity(), MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }

                            @Override
                            public void failure(TwitterException e) {
                                Log.e(TAG, "Failed to get account credentials: " + e);
                            }
                        }
                );
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.w(TAG, "Twitter auth request cancelled");
            }
        });
    }

    private void saveAccount(String username, int followers, int tweets, int following, String avatarUrl) {

        List<Stats> statsList = new ArrayList<>();
        statsList.add(new Stats("followers", followers));
        statsList.add(new Stats("tweets", tweets));
        statsList.add(new Stats("following", following));

        Account account = new Account("Twitter", username, statsList, avatarUrl);

        AccountDataSource dataSource = new AccountDataSource(getActivity());
        dataSource.create(account);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setTitle("Twitter");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        mTwitterButton.onActivityResult(requestCode, resultCode, data);
    }
}
