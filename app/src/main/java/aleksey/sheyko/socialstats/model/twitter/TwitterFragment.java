package aleksey.sheyko.socialstats.model.twitter;

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
import aleksey.sheyko.socialstats.app.database.AccountDataSource;
import aleksey.sheyko.socialstats.model.Account;
import aleksey.sheyko.socialstats.model.AccountStats;
import io.fabric.sdk.android.Fabric;


public class TwitterFragment extends Fragment {

    public static final String TAG = TwitterFragment.class.getSimpleName();

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "bmQCF0w32m1awcIOIua56yPqa";
    private static final String TWITTER_SECRET = "8XjePz2wz3TjuvRBuN5fk6O1WzthXYEEvB6AVk1xayVUq5hhnY";

    private TwitterLoginButton twitterLoginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_twitter, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // List existing Twitter accounts
        AccountDataSource dataSource = new AccountDataSource(getActivity());
        ArrayList<Account> accounts = dataSource.read("Twitter");

        ServiceAdapter adapter = new ServiceAdapter(
                getActivity(), R.layout.list_item_service, accounts);

        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);

        // Give the ability to add new account
        TwitterAuthConfig authConfig =
                new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(getActivity(), new Twitter(authConfig));

        twitterLoginButton = (TwitterLoginButton)
                view.findViewById(R.id.twitterLoginButton);
        twitterLoginButton.setEnabled(true);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a
                // TwitterSession for making API calls
                TwitterApiClient twitterApiClient =
                        TwitterCore.getInstance().getApiClient(result.data);

                twitterApiClient.getAccountService()
                        .verifyCredentials(false, false, new Callback<User>() {
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

        Button loginButton = (Button) view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                twitterLoginButton.performClick();
            }
        });
    }

    private void saveAccount(String username, int followers, int tweets, int following, String avatarUrl) {

        List<AccountStats> statsList = new ArrayList<>();
        statsList.add(new AccountStats("followers", followers));
        statsList.add(new AccountStats("tweets", tweets));
        statsList.add(new AccountStats("following", following));

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
        twitterLoginButton.onActivityResult(requestCode, resultCode,
                data);
    }
}
