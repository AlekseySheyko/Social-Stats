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
        ArrayList<Account> accounts = readAccounts();
        addAccountStats(accounts);
        return accounts;
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
                Account account = new Account(getIntFromColumnName(cursor, BaseColumns._ID),
                        getStringFromColumnName(cursor, SqliteHelper.COLUMN_SERVICE),
                        getStringFromColumnName(cursor, SqliteHelper.COLUMN_USER_NAME),
                        null);
                accounts.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close(database);
        return accounts;
    }

    public void addAccountStats(ArrayList<Account> accounts) {
        SQLiteDatabase database = open();

        for (Account account : accounts) {
            ArrayList<AccountStats> statsList = new ArrayList<AccountStats>();
            Cursor cursor = database.rawQuery(
                    "SELECT * FROM " + SqliteHelper.STATS_TABLE +
                            " WHERE ACCOUNT_ID = " + account.getId(), null);

            if (cursor.moveToFirst()) {
                do {
                    AccountStats dataSet = new AccountStats(
                        getIntFromColumnName(cursor, BaseColumns._ID),
                            getStringFromColumnName(cursor, SqliteHelper.COLUMN_LABEL),
                            getIntFromColumnName(cursor, SqliteHelper.COLUMN_VALUE)
                    );
                    statsList.add(dataSet);
                } while (cursor.moveToNext());
            }
            account.setStatsList(statsList);
            cursor.close();
            close(database);
        }
    }

    private int getIntFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getInt(columnIndex);
    }

    private String getStringFromColumnName(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return cursor.getString(columnIndex);
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
