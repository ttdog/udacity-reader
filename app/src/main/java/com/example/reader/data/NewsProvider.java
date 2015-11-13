package com.example.reader.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by 01011549 on 15/11/05.
 */
public class NewsProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private NewsDbHelper mOpenHelper;

    // USERS テーブル URI ID
    private static final int NEWS = 1;
    // USERS テーブル 個別 URI ID
    private static final int NEWS_SOURCE = 2;
    // USERS テーブル 個別 URI ID
    private static final int NEWS_WITH_NEWS_SOURCE = 3;

    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NewsContract.CONTENT_AUTHORITY;

        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.
        //マッチした場合第3引数で設定した値が返る
        //*:文字列にマッチ
        //#:数値にマッチ
        //NEWS一覧取得
        matcher.addURI(authority, NewsContract.PATH_NEWS, NEWS);
        //ニュースソースを指定したニュースの一覧取得
        matcher.addURI(authority, NewsContract.PATH_NEWS + "/"+ NewsContract.PATH_NEWS_SOURCE +"/#", NEWS_WITH_NEWS_SOURCE);
        //ニュースソース一覧取得
        matcher.addURI(authority, NewsContract.PATH_NEWS_SOURCE, NEWS_SOURCE);

        // 3) Return the new matcher!
        return matcher;
    }

    private static final SQLiteQueryBuilder sNewsWithNewsSourceQueryBuilder;

    static{
        sNewsWithNewsSourceQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sNewsWithNewsSourceQueryBuilder.setTables(
                NewsContract.NewsEntry.TABLE_NAME + " INNER JOIN " +
                        NewsContract.NewsSourceEntry.TABLE_NAME +
                        " ON " + NewsContract.NewsEntry.TABLE_NAME +
                        "." + NewsContract.NewsEntry.COLUMN_SOURCE_ID +
                        " = " + NewsContract.NewsSourceEntry.TABLE_NAME +
                        "." + NewsContract.NewsSourceEntry._ID);
    }

//    private static final String sNewsWithNewsSource =
//            WeatherContract.LocationEntry.TABLE_NAME +
//                    "." + WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
//                    WeatherContract.WeatherEntry.COLUMN_DATE + " = ? ";

    //news.source_id = ?
    private static final String sNewsWithSourceSelection =
            NewsContract.NewsEntry.TABLE_NAME+
                    "." + NewsContract.NewsEntry.COLUMN_SOURCE_ID + " = ? ";

    //プロバイダの初期化
    @Override
    public boolean onCreate() {
        mOpenHelper = new NewsDbHelper(getContext());
        return true;
    }

    //MIME typeを返す
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case NEWS:
                return NewsContract.NewsEntry.CONTENT_ITEM_TYPE;
            case NEWS_SOURCE:
                return NewsContract.NewsSourceEntry.CONTENT_ITEM_TYPE;
            case NEWS_WITH_NEWS_SOURCE:
                return NewsContract.NewsEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    //データの取得
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "news"
            case NEWS:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NewsContract.NewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "newsSource"
            case NEWS_SOURCE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NewsContract.NewsSourceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "news/newsSource/#"
            case NEWS_WITH_NEWS_SOURCE: {
                String sourceId = uri.getPathSegments().get(2);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NewsContract.NewsEntry.TABLE_NAME,
                        projection,
                        sNewsWithSourceSelection,
                        new String[]{sourceId},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case NEWS: {
                long _id = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = NewsContract.NewsEntry.buildNews(String.valueOf(_id));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case NEWS_SOURCE:{
                long _id = db.insert(NewsContract.NewsSourceEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = NewsContract.NewsSourceEntry.buildNewsSource(String.valueOf(_id));
                }
                else{
                    throw  new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;

        switch (match) {
            case NEWS:
                db.beginTransaction();

                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case NEWS_WITH_NEWS_SOURCE:
                db.beginTransaction();

                try{
                    String sourceId = uri.getPathSegments().get(2);

                    for(ContentValues value : values){
                        Cursor cur = db.query(NewsContract.NewsEntry.TABLE_NAME,
                                null,
                                NewsContract.NewsEntry.COLUMN_SOURCE_ID + " = ? AND " + NewsContract.NewsEntry.COLUMN_LINK + " = ?",
                                new String[]{sourceId, (String) value.get(NewsContract.NewsEntry.COLUMN_LINK)},
                                null,
                                null,
                                null
                        );
                        if(cur != null && cur.getCount() > 0) {
                            cur.moveToFirst();
                            db.update(NewsContract.NewsEntry.TABLE_NAME,
                                    value,
                                    NewsContract.NewsEntry._ID + " = ?",
                                    new String[]{String.valueOf(cur.getLong(cur.getColumnIndex(NewsContract.NewsEntry._ID)))});
                        }
                        else{
                            long _id = db.insert(NewsContract.NewsEntry.TABLE_NAME, null, value);
                            if(_id != -1){
                                ++returnCount;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }

                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Student: Start by getting a writable database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        // Student: Use the uriMatcher to match the WEATHER and LOCATION URI's we are going to
        // handle.  If it doesn't match these, throw an UnsupportedOperationException.

        // Student: A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        // Oh, and you should notify the listeners here.
        if(null == selection){
            selection = "1";
        }

        switch(match){
            case NEWS:
                rowsDeleted = db.delete(NewsContract.NewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NEWS_SOURCE:
                rowsDeleted = db.delete(NewsContract.NewsSourceEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }

        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Student: return the actual rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case NEWS:
                rowsUpdated = db.update(NewsContract.NewsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case NEWS_SOURCE:
                rowsUpdated = db.update(NewsContract.NewsSourceEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }
        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
