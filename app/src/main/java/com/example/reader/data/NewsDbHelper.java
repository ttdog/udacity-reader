package com.example.reader.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.reader.MainActivityFragment;
import com.example.reader.data.NewsContract.NewsEntry;
import com.example.reader.data.NewsContract.NewsSourceEntry;
/**
 * Created by 01011549 on 15/11/05.
 */
public class NewsDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    static final String DATABASE_NAME = "news.db";

    public NewsDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_NEWS_SOURCE_TABLE = "CREATE TABLE " + NewsSourceEntry.TABLE_NAME + " (" +
                NewsSourceEntry._ID + " INTEGER PRIMARY KEY," +
                NewsSourceEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                NewsSourceEntry.COLUMN_URL + " TEXT NOT NULL," +
                NewsSourceEntry.COLUMN_USE + " INTEGER NOT NULL DEFAULT 1," +
                NewsSourceEntry.COLUMN_IS_DEFAULT + " INTEGER NOT NULL DEFAULT 0" +
                ");";

        final String SQL_CREATE_NEWS_TABLE = "CREATE TABLE " + NewsEntry.TABLE_NAME + " (" +
                NewsEntry._ID + " INTEGER PRIMARY KEY," +
                NewsEntry.COLUMN_SOURCE_ID + " INTEGER NOT NULL," +
                NewsEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                NewsEntry.COLUMN_DESCRIPTION + " TEXT, " +
                NewsEntry.COLUMN_LINK + " TEXT NOT NULL, " +
                NewsEntry.COLUMN_UPDATED_AT + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + NewsEntry.COLUMN_SOURCE_ID + ") REFERENCES " +
                NewsSourceEntry.TABLE_NAME + " (" + NewsSourceEntry._ID + ")" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_NEWS_SOURCE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_NEWS_TABLE);

        insertDefaultRecord(sqLiteDatabase);
    }

    //デフォルトRSSフィードリストの登録
    private void insertDefaultRecord(SQLiteDatabase sqLiteDatabase){
        String sql;
        for (int i = 0, length = NewsSourceEntry.sourceTitles.length; i < length; ++i){
            sql = "INSERT INTO " + NewsSourceEntry.TABLE_NAME + " (" +
                    NewsSourceEntry.COLUMN_TITLE + "," +
                    NewsSourceEntry.COLUMN_URL + "," +
                    NewsSourceEntry.COLUMN_IS_DEFAULT + ") VALUES('" +
                    NewsSourceEntry.sourceTitles[i] + "','" +
                    NewsSourceEntry.sourceUrls[i] + "'," +
                    String.valueOf(1) + ");";
            sqLiteDatabase.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsSourceEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
