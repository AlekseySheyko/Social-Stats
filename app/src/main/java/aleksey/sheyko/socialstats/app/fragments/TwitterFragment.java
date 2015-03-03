package aleksey.sheyko.socialstats.app.fragments;

import android.app.Fragment;


public class TwitterFragment extends Fragment {
//
//    public static final String TAG = TwitterFragment.class.getSimpleName();
//
//    private static final String TWITTER_KEY = "bmQCF0w32m1awcIOIua56yPqa";
//    private static final String TWITTER_SECRET = "8XjePz2wz3TjuvRBuN5fk6O1WzthXYEEvB6AVk1xayVUq5hhnY";
//
//    private TwitterLoginButton mTwitterButton;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_twitter, container, false);
//    }
//
//    @Override
//    public void onViewCreated(final View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        populateList(view);
//
//        TwitterAuthConfig authConfig =
//                new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(getActivity(), new Twitter(authConfig));
//
//        Button loginButton = (Button) view.findViewById(R.id.loginButton);
//        loginButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getAccessToken(view);
//            }
//        });
//    }
//
//    private void populateList(View v) {
//    }
//
//    public String getAccessToken(View v) {
//
//        mTwitterButton = (TwitterLoginButton) v.findViewById(R.id.twitterLoginButton);
//        mTwitterButton.setEnabled(true);
//        mTwitterButton.setCallback(new Callback<TwitterSession>() {
//            @Override
//            public void success(Result<TwitterSession> result) {
//                // Do something with result, which provides a
//                // TwitterSession for making API calls
//                TwitterApiClient apiClient = TwitterCore.getInstance().getApiClient(result.data);
//                apiClient.getAccountService().verifyCredentials(false, false, new Callback<User>() {
//                            @Override
//                            public void success(Result<User> userResult) {
//                                TwitterSession session =
//                                        Twitter.getSessionManager().getActiveSession();
//                                TwitterAuthToken authToken = session.getAuthToken();
//                                String accessToken = authToken.token;
//
//                                User userData = userResult.data;
//
//                                String username = userData.screenName;
//                                int followers = userData.followersCount;
//                                int tweets = userData.listedCount;
//                                int following = userData.favouritesCount;
//                                String avatarUrl = userData.profileImageUrl;
//
//                                saveAccount(username, followers, tweets, following, avatarUrl, accessToken);
//
//                                startActivity(new Intent(getActivity(), MainActivity.class)
//                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                            }
//
//                            @Override
//                            public void failure(TwitterException e) {
//                                Log.e(TAG, "Failed to get account credentials: " + e);
//                            }
//                        }
//                );
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                // Do something on failure
//                Log.w(TAG, "Twitter auth request cancelled");
//            }
//        });
//        return null;
//    }
//
//    private void saveAccount(String username, int followers, int tweets, int following, String avatarUrl, String accessToken) {
//
//        List<Stats> statsList = new ArrayList<>();
//        statsList.add(new Stats("followers", followers));
//        statsList.add(new Stats("tweets", tweets));
//        statsList.add(new Stats("following", following));
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        getActivity().getActionBar().setTitle("Twitter");
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Pass the activity result to the login button.
//        mTwitterButton.onActivityResult(requestCode, resultCode, data);
//    }
}
