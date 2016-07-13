package vip.chengchao.tools.mypwnode.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chengchao on 16/6/30.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String CREATE_SQL = "create table account(id integer primary key autoincrement," +
            "type text," +
            "account text," +
            "password text," +
            "desc text," +
            "create_time text," +
            "update_time text)";

    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
