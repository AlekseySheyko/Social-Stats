package aleksey.sheyko.staticdemo.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;

import aleksey.sheyko.staticdemo.R;
import aleksey.sheyko.staticdemo.app.adapters.DynamicListView;
import aleksey.sheyko.staticdemo.app.adapters.StableArrayAdapter;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> mCheeseList = new ArrayList<String>();
        Collections.addAll(mCheeseList, "AlekseySheyko", "aleksey_sheyko", "tropic2c");

        StableArrayAdapter adapter = new StableArrayAdapter(this, R.layout.list_item, mCheeseList);
        DynamicListView listView = (DynamicListView) findViewById(R.id.listview);

        listView.setCheeseList(mCheeseList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(this, SelectActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
