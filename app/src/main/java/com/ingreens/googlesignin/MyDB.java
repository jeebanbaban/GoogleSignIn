package com.ingreens.googlesignin;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 12/2/18.
 */

public class MyDB extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public MyDB(Context context) {
        super(context, AllKeys.DB_NAME, null, AllKeys.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
        db.execSQL("CREATE TABLE IF NOT EXISTS "+AllKeys.DB_TBL_USER+"(" +
                AllKeys.DB_TBL_USER_ID+" integer primary key autoincrement," +
                AllKeys.DB_ACCOUNT_ID+" text unique,"+AllKeys.DB_TBL_USER_NAME+" text, " +
                AllKeys.DB_TBL_USER_NICKNAME+" text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.db=db;
        if(oldVersion<newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+AllKeys.DB_TBL_USER);
            onCreate(db);
        }
    }

    public boolean executeNonQuery(String sql){
        boolean flag=false;
        try {
            this.db = getWritableDatabase();
            db.execSQL(sql);
            flag=true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    public Cursor executeQuery(String sql){
        Cursor c=null;
        try {
            this.db = getReadableDatabase();
            c=db.rawQuery(sql,null);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }
}