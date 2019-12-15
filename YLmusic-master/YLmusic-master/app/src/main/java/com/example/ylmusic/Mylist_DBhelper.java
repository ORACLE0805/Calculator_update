package com.example.ylmusic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Mylist_DBhelper extends SQLiteOpenHelper {
    private String CREATE_MUSICS_TABLE="create table musics(_id integer primary key autoincrement,title varchar(200),artist varchar(200),img varchar(200),music_id varchar(200) )";
    public Mylist_DBhelper(Context context){
        super(context,"music_db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MUSICS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
