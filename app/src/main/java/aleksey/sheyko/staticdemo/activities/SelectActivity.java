package aleksey.sheyko.staticdemo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import aleksey.sheyko.staticdemo.R;


public class SelectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
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
