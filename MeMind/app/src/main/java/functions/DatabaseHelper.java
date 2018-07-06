package functions;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tony on 2016-02-04.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    String TABLE_NAME;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        TABLE_NAME = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTable =  "create table " + TABLE_NAME + "("
                + " id integer PRIMARY KEY autoincrement, "
                + " question text, "
                + " experience text, "
                + " thedate date,"
                + " star real)";
        db.execSQL(CreateTable);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
