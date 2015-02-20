package aleksey.sheyko.staticdemo.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class SqliteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "stats.db";
    public static final int DB_VERSION = 1;
    //Account Table functionality
    public static final String ACCOUNTS_TABLE = "ACCOUNTS";
    public static final String COLUMN_SERVICE = "SERVICE";
    public static final String COLUMN_USER_NAME = "NAME";
    public static final String COLUMN_STATS = "STATS";
    public static String CREATE_ACCOUNTS =
            "CREATE TABLE " + ACCOUNTS_TABLE + "("
                    + BaseColumns._ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_SERVICE + " TEXT," +
                    COLUMN_USER_NAME + " TEXT," +
                    COLUMN_STATS + " TEXT)";

    //Stats Table functionality
    public static final String STATS_TABLE = "STATS";
    public static final String COLUMN_LABEL = "LABEL";
    public static final String COLUMN_VALUE = "VALUE";
    public static final String COLUMN_FOREIGN_KEY_ACCOUNT = "ACCOUNT_ID";
    public static String CREATE_STATS =
            "CREATE TABLE " + STATS_TABLE + "("
                    + BaseColumns._ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_LABEL + " TEXT," +
                    COLUMN_VALUE + " TEXT," +
                    "FOREIGN KEY(" + COLUMN_FOREIGN_KEY_ACCOUNT + ") REFERENCES ACCOUNTS(_ID))";

    public SqliteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_ACCOUNTS);
        database.execSQL(CREATE_STATS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i2) {

    }
}
