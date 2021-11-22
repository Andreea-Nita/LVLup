package com.andreeanita.lvlup.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "myDatabase.db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL("CREATE TABLE if not exists user(ID INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT, email TEXT, password TEXT)");
        db.execSQL("CREATE TABLE if not exists user_activity(ID INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT,time TEXT,pace TEXT," +
                "time_elapsed TEXT, distance TEXT,image BLOB, email TEXT NOT NULL,FOREIGN KEY (email) REFERENCES user (email))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF exists user");
        db.execSQL("DROP TABLE IF exists user_activity");
        onCreate(db);
    }

    public boolean Insert(String name,String email,String password ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = sqLiteDatabase.insert("user", null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean Insert(LocalDate date, String time, String pace, String time_elapsed, String distance, byte[] image, String email){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("date", String.valueOf(date));
        contentValues.put("time", String.valueOf(time));
        contentValues.put("pace",pace);
        contentValues.put("time_elapsed",time_elapsed);
        contentValues.put("distance",distance);
        contentValues.put("email",email);
        contentValues.put("image", image);
        long result=sqLiteDatabase.insert("user_activity",null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    public Boolean CheckEmail(String email){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user WHERE email=?", new String[]{email});
        if(cursor.getCount() > 0){
            return false;
        }else{
            return true;
        }
    }

    public Boolean CheckLogin(String email, String password){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user WHERE email=? AND password=?", new String[]{email, password});
        if(cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }
}
