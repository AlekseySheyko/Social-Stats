package aleksey.sheyko.socialstats.app.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import aleksey.sheyko.socialstats.R;
import aleksey.sheyko.socialstats.app.adapters.AccountAdapter;
import aleksey.sheyko.socialstats.app.adapters.DynamicListView;
import aleksey.sheyko.socialstats.database.AccountDataSource;


public class MainActivity extends Activity implements OnRefreshListener {

    private AccountDataSource mDataSource;
    private ArrayList<aleksey.sheyko.socialstats.model.Account> mAccountList;

    private AccountAdapter mAdapter;

    private SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccountManager manager = AccountManager.get(this);
        for (Account account : manager.getAccountsByType("com.twitter.android.auth.login")) {
            Log.d("TAG", account.name + " - " + account.type);
        }


        mDataSource = new AccountDataSource(this);
        mAccountList = mDataSource.read();

        mAdapter = new AccountAdapter(this,
                R.layout.list_item_account, mAccountList);

        DynamicListView listView = (DynamicListView) findViewById(R.id.listview);
        listView.setAccountList(mAccountList);
        listView.setAdapter(mAdapter);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {

        updateStats();
        mSwipeLayout.setRefreshing(false);
    }

    private void updateStats() {
//        for (Account account : mAccountList) {
//            mDataSource.update(account);
//        }
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

    public SwipeRefreshLayout getSwipeLayout() {
        return mSwipeLayout;
    }

    public AccountAdapter getAdapter() {
        return mAdapter;
    }
}
