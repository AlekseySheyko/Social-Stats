package aleksey.sheyko.staticdemo.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import aleksey.sheyko.staticdemo.R;


public class SelectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.80);
        int screenHeight = (int) (metrics.widthPixels * 0.80);

        setContentView(R.layout.activity_select);

        getWindow().setLayout(screenWidth, screenHeight);
    }

    public void addAccount(View view) {
        Intent intent = new Intent(this, SocialActivity.class);
        switch (view.getId()) {
            case R.id.twitter_btn:
                intent.putExtra("fragment", SocialActivity.FR_TWITTER);
                break;
            case R.id.instagram_btn:
                intent.putExtra("fragment", SocialActivity.FR_INSTAGRAM);
                break;
            case R.id.facebook_btn:
                intent.putExtra("fragment", SocialActivity.FR_FACEBOOK);
                break;
            case R.id.google_btn:
                intent.putExtra("fragment", SocialActivity.FR_GOOGLE);
                break;
        }
        startActivity(intent);
    }
}
