package aleksey.sheyko.socialstats.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "stats.db";
    public static final int DB_VERSION = 1;
    //Account Table functionality
    public static final String ACCOUNTS_TABLE = "ACCOUNTS";
    public static final String COLUMN_SERVICE = "SERVICE";
    public static final String COLUMN_USER_NAME = "NAME";
    public static final String COLUMN_DATA_SETS = "DATA_SETS";
    public static final String COLUMN_AVATAR_URL = "AVATAR_URL";
    public static final String COLUMN_AUTH_TOKEN = "AUTH_TOKEN";
    public static String CREATE_ACCOUNTS =
            "CREATE TABLE " + ACCOUNTS_TABLE + "("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_SERVICE + " TEXT," +
                    COLUMN_USER_NAME + " TEXT," +
                    COLUMN_DATA_SETS + " TEXT," +
                    COLUMN_AVATAR_URL + " TEXT," +
                    COLUMN_AUTH_TOKEN + " TEXT," +
                    "UNIQUE(" + COLUMN_SERVICE + "," + COLUMN_USER_NAME + "))";

    //Stats Table functionality
    public static final String STATS_TABLE = "STATS";
    public static final String COLUMN_LABEL = "LABEL";
    public static final String COLUMN_VALUE = "VALUE";
    public static final String COLUMN_FOREIGN_KEY_ACCOUNT = "ACCOUNT_ID";
    public static String CREATE_STATS =
            "CREATE TABLE " + STATS_TABLE + "("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_LABEL + " TEXT," +
                    COLUMN_VALUE + " INTEGER," +
                    COLUMN_FOREIGN_KEY_ACCOUNT + " INTEGER," +
                    "FOREIGN KEY(" + COLUMN_FOREIGN_KEY_ACCOUNT
                    + ") REFERENCES " + ACCOUNTS_TABLE + "(" + BaseColumns._ID + "))";

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_ACCOUNTS);
        database.execSQL(CREATE_STATS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }
}
