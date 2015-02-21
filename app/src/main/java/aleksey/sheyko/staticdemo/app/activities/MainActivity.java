package aleksey.sheyko.staticdemo.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import aleksey.sheyko.staticdemo.R;
import aleksey.sheyko.staticdemo.app.adapters.AccountAdapter;
import aleksey.sheyko.staticdemo.app.adapters.DynamicListView;
import aleksey.sheyko.staticdemo.app.database.AccountDataSource;
import aleksey.sheyko.staticdemo.models.Account;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AccountDataSource dataSource = new AccountDataSource(this);
        ArrayList<Account> accounts = dataSource.read();

        AccountAdapter adapter = new AccountAdapter(this,
                R.layout.account_list_item, accounts);

        DynamicListView listView = (DynamicListView) findViewById(R.id.listview);
        listView.setCheeseList(accounts);
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
