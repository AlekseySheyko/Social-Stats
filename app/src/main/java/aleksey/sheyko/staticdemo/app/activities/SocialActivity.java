package aleksey.sheyko.staticdemo.app.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import aleksey.sheyko.staticdemo.R;
import aleksey.sheyko.staticdemo.app.fragments.InstagramFragment;
import aleksey.sheyko.staticdemo.app.fragments.TwitterFragment;

public class SocialActivity extends Activity {

    public static final int FR_TWITTER = 0;
    public static final int FR_INSTAGRAM = 1;
    public static final int FR_FACEBOOK = 2;
    public static final int FR_GOOGLE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        int fragmentId = getIntent()
                .getIntExtra("fragment", -1);

        Fragment fragment = null;
        switch (fragmentId) {
            case FR_TWITTER:
                fragment = new TwitterFragment();
                break;
            case FR_INSTAGRAM:
                fragment = new InstagramFragment();
                break;
            case FR_FACEBOOK:

                break;
            case FR_GOOGLE:

                break;
        }
        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.fragment_container, fragment);
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the fragment, which will
        // then pass the result to the login button.
        Fragment fragment = getFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
