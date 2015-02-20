package aleksey.sheyko.staticdemo.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class StatsDataSource {

    private Context mContext;
    private SqliteHelper mSqliteHelper;

    public StatsDataSource(Context context) {

        mContext = context;
        mSqliteHelper = new SqliteHelper(context);
        SQLiteDatabase database = mSqliteHelper.getReadableDatabase();
        database.close();
    }
}
