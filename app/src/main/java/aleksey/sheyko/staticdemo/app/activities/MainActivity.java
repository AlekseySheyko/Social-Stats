package aleksey.sheyko.staticdemo.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import aleksey.sheyko.staticdemo.R;
import aleksey.sheyko.staticdemo.app.adapters.AccountAdapter;
import aleksey.sheyko.staticdemo.app.adapters.DynamicListView;
import aleksey.sheyko.staticdemo.app.database.AccountDataSource;
import aleksey.sheyko.staticdemo.models.Account;


public class MainActivity extends Activity
        implements OnItemClickListener, OnRefreshListener {

    private DynamicListView mListView;
    private AccountAdapter mAdapter;
    private ArrayList<Account> mAccounts;

    private SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AccountDataSource dataSource = new AccountDataSource(this);
        mAccounts = dataSource.read();

        mAdapter = new AccountAdapter(this,
                R.layout.list_item_account, mAccounts);

        mListView = (DynamicListView) findViewById(R.id.listview);
        mListView.setCheeseList(mAccounts);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Account currentAccount = mAccounts.get(position);
        currentAccount.notifyDataSetChanged();

        mAdapter.notifyDataSetChanged();
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

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 1000);
    }

    public SwipeRefreshLayout getSwipeLayout() {
        return mSwipeLayout;
    }
}
