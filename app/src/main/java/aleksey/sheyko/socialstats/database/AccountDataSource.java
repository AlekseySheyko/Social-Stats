package aleksey.sheyko.socialstats.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

import aleksey.sheyko.socialstats.model.Account;
import aleksey.sheyko.socialstats.model.Stats;

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

    public ArrayList<Account> read(String service) {
        ArrayList<Account> accounts = readAccounts(service);
        addAccountStats(accounts);
        return accounts;
    }

    public ArrayList<Account> readAccounts() {
        SQLiteDatabase database = open();

        Cursor cursor = database.query(
                SQLiteHelper.ACCOUNTS_TABLE,
                new String[]{SQLiteHelper.COLUMN_USER_NAME, BaseColumns._ID, SQLiteHelper.COLUMN_SERVICE, SQLiteHelper.COLUMN_AVATAR_URL},
                null, //selection
                null, //selection args
                null, //group by
                null, //having
                null); //order

        ArrayList<Account> accounts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(getIntFromColumnName(cursor, BaseColumns._ID),
                        getStringFromColumnName(cursor, SQLiteHelper.COLUMN_SERVICE),
                        getStringFromColumnName(cursor, SQLiteHelper.COLUMN_USER_NAME),
                        null,
                        getStringFromColumnName(cursor, SQLiteHelper.COLUMN_AVATAR_URL),
                        getStringFromColumnName(cursor, SQLiteHelper.COLUMN_AUTH_TOKEN));
                accounts.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close(database);
        return accounts;
    }

    public ArrayList<Account> readAccounts(String service) {
        SQLiteDatabase database = open();

        Cursor cursor = database.query(
                SQLiteHelper.ACCOUNTS_TABLE,
                new String[]{SQLiteHelper.COLUMN_USER_NAME, BaseColumns._ID, SQLiteHelper.COLUMN_SERVICE, SQLiteHelper.COLUMN_AVATAR_URL},
                SQLiteHelper.COLUMN_SERVICE + " = ?", //selection
                new String[]{service}, //selection args
                null, //group by
                null, //having
                null); //order

        ArrayList<Account> accounts = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(getIntFromColumnName(cursor, BaseColumns._ID),
                        getStringFromColumnName(cursor, SQLiteHelper.COLUMN_SERVICE),
                        getStringFromColumnName(cursor, SQLiteHelper.COLUMN_USER_NAME),
                        null,
                        getStringFromColumnName(cursor, SQLiteHelper.COLUMN_AVATAR_URL),
                        getStringFromColumnName(cursor, SQLiteHelper.COLUMN_AUTH_TOKEN));
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
            ArrayList<Stats> statsList = new ArrayList<>();
            Cursor cursor = database.rawQuery(
                    "SELECT * FROM " + SQLiteHelper.STATS_TABLE +
                            " WHERE ACCOUNT_ID = " + account.getId(), null);

            if (cursor.moveToFirst()) {
                do {
                    Stats dataSet = new Stats(
                            getIntFromColumnName(cursor, BaseColumns._ID),
                            getStringFromColumnName(cursor, SQLiteHelper.COLUMN_LABEL),
                            getIntFromColumnName(cursor, SQLiteHelper.COLUMN_VALUE));
                    statsList.add(dataSet);
                } while (cursor.moveToNext());
            }
            account.setStatsList(statsList);
            cursor.close();
        }
        close(database);
    }

    public void update(Account account) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        for (Stats dataSet : account.getStatsList()) {
            ContentValues updateStatsValues = new ContentValues();
            updateStatsValues.put(SQLiteHelper.COLUMN_VALUE, dataSet.getValue());
            database.update(SQLiteHelper.STATS_TABLE,
                    updateStatsValues,
                    String.format("%s=%d", BaseColumns._ID, dataSet.getId())
                    ,null);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);
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

        for (Stats dataSet : account.getStatsList()) {
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
