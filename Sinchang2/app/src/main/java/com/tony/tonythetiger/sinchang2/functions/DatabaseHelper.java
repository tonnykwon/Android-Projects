package com.tony.tonythetiger.sinchang2.functions;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tony on 2017-03-26.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    String TABLE_NAME = "sinchang";
    String SECOND_TABLE_NAME = "InOut";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        TABLE_NAME = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //제품 테이블(accum은 현재 창고 내 수량)
        String CreateTable =  "create table " + TABLE_NAME + "("
                + " barcode text PRIMARY KEY, "
                + " big_category text, " //대분류 big category
                + " small_category text, " // 소분류 small category
                + " company text, " // 회사
                + " name text NOT NULL, " // 제품
                + " note text, " // 비고란
                + " accum integer, "
                + " boxn integer, " // 박스 개수
                + " standard integer)";
        db.execSQL(CreateTable);

        //입출고 테이블 생성
        String CreateSecondTable =  "create table " + SECOND_TABLE_NAME + "("
                + " id integer PRIMARY KEY autoincrement, "
                + " inout boolean, "
                + " number integer, "
                + " thedate date, "
                + " barcode2 text, "
                + " FOREIGN KEY(barcode2) REFERENCES "+TABLE_NAME+"(barcode))";
        db.execSQL(CreateSecondTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
