package aleksey.sheyko.staticdemo.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

import aleksey.sheyko.staticdemo.models.Account;
import aleksey.sheyko.staticdemo.models.AccountStats;

public class AccountDataSource {

    private Context mContext;
    private SqliteHelper mSqliteHelper;

    public AccountDataSource(Context context) {

        mContext = context;
        mSqliteHelper = new SqliteHelper(context);
    }

    private SQLiteDatabase open() {
        return mSqliteHelper.getWritableDatabase();
    }

    private void close(SQLiteDatabase database) {
        database.close();
    }

    public ArrayList<Account> read() {
        return null;
    }

    public ArrayList<Account> readAccounts() {
        SQLiteDatabase database = open();

        Cursor cursor = database.query(
                SqliteHelper.ACCOUNTS_TABLE,
                new String[] {SqliteHelper.COLUMN_SERVICE, BaseColumns._ID, SqliteHelper.COLUMN_USER_NAME},
                null, //selection
                null, //selection args
                null, //group by
                null, //having
                null); //order

        ArrayList<Account> accounts = new ArrayList<Account>();
        if (cursor.moveToFirst()) {
            do {

            } while (cursor.moveToNext());
        }

        return null;
    }

    public void create(Account account) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues accountValues = new ContentValues();
        accountValues.put(SqliteHelper.COLUMN_SERVICE, account.getService());
        accountValues.put(SqliteHelper.COLUMN_USER_NAME, account.getUsername());
        long accountID = database.insert(SqliteHelper.ACCOUNTS_TABLE, null, accountValues);

        for (AccountStats accountDataSet : account.getStatsList()) {
            ContentValues statsValues = new ContentValues();
            statsValues.put(SqliteHelper.COLUMN_LABEL, accountDataSet.getLabel());
            statsValues.put(SqliteHelper.COLUMN_VALUE, accountDataSet.getValue());
            statsValues.put(SqliteHelper.COLUMN_FOREIGN_KEY_ACCOUNT, accountID);

            database.insert(SqliteHelper.STATS_TABLE, null, statsValues);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }
}
