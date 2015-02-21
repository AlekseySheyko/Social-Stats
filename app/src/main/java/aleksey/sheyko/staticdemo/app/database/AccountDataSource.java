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

    private SQLiteHelper mSQLiteHelper;

    public AccountDataSource(Context context) {

        mSQLiteHelper = new SQLiteHelper(context);
    }

    private SQLiteDatabase open() {
        return mSQLiteHelper.getWritableDatabase();
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
                SQLiteHelper.ACCOUNTS_TABLE,
                new String[]{SQLiteHelper.COLUMN_USER_NAME, BaseColumns._ID, SQLiteHelper.COLUMN_SERVICE},
                null, //selection
                null, //selection args
                null, //group by
                null, //having
                null); //order

        ArrayList<Account> accounts = new ArrayList<Account>();
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(getIntFromColumnName(cursor, BaseColumns._ID),
                        getStringFromColumnName(cursor, SQLiteHelper.COLUMN_SERVICE),
                        getStringFromColumnName(cursor, SQLiteHelper.COLUMN_USER_NAME),
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
                    "SELECT * FROM " + SQLiteHelper.STATS_TABLE +
                            " WHERE ACCOUNT_ID = " + account.getId(), null);

            if (cursor.moveToFirst()) {
                do {
                    AccountStats dataSet = new AccountStats(
                            getIntFromColumnName(cursor, BaseColumns._ID),
                            getStringFromColumnName(cursor, SQLiteHelper.COLUMN_LABEL),
                            getIntFromColumnName(cursor, SQLiteHelper.COLUMN_VALUE));
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
        accountValues.put(SQLiteHelper.COLUMN_SERVICE, account.getService());
        accountValues.put(SQLiteHelper.COLUMN_USER_NAME, account.getUsername());
        long accountID = database.insert(SQLiteHelper.ACCOUNTS_TABLE, null, accountValues);

        for (AccountStats dataSet : account.getStatsList()) {
            ContentValues statsValues = new ContentValues();
            statsValues.put(SQLiteHelper.COLUMN_LABEL, dataSet.getLabel());
            statsValues.put(SQLiteHelper.COLUMN_VALUE, dataSet.getValue());
            statsValues.put(SQLiteHelper.COLUMN_FOREIGN_KEY_ACCOUNT, accountID);

            database.insert(SQLiteHelper.STATS_TABLE, null, statsValues);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
    }
}
