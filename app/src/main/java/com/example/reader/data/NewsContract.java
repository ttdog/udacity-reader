package com.example.reader.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by 01011549 on 15/11/05.
 */
public class NewsContract {
    public static final String CONTENT_AUTHORITY = "com.example.reader";
    public static final String PATH_NEWS = "news";
    public static final String PATH_NEWS_SOURCE = "newsSource";
    public static final String PATH_NEWS_WITH_NEWS_SOURCE = "newsWithNewsSource";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class NewsEntry implements BaseColumns{
        public static final String TABLE_NAME = "news";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_UPDATED_AT = "updatedAt";
        public static final String COLUMN_SOURCE_ID = "sourceId";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();
        public static final Uri CONTENT_WITH_SOURCE_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).appendPath(PATH_NEWS_SOURCE).build();

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS_WITH_NEWS_SOURCE;

//        //add
//        public static final Uri CONTENT_URI =
//                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();

        public static Uri buildNews(String newsSetting) {
            return BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).appendPath(newsSetting).build();
        }

    }

    public static final class NewsSourceEntry implements BaseColumns{
        public static final String TABLE_NAME = "newsSource";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USE = "use";
        public static final String COLUMN_IS_DEFAULT = "isDefault";

        public static final String[] sourceTitles = {"はてな", "BBBB", "cccccc", "DDDDDD", "EEEEE", "FFFFF", "GGGGGG", "HHHHHH"};
        public static final String[] sourceUrls = {"http://b.hatena.ne.jp/hotentry.rss", "http://BBBB", "http://cccccc", "http://DDDDDD", "http://EEEEE", "http://FFFFF", "http://GGGGGG", "http://HHHHHH"};

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS_SOURCE).build();

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS_SOURCE;

        public static Uri buildNewsSource(String newsSourceSetting) {
            return BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).appendPath(newsSourceSetting).build();
        }
//
//        public static String getLocationSettingFromUri(Uri uri) {
//            return uri.getPathSegments().get(1);
//        }
//
//        public static long getDateFromUri(Uri uri) {
//            return Long.parseLong(uri.getPathSegments().get(2));
//        }

        public static Cursor getAllRssSources(Context context){
            return context.getContentResolver().query(
                    NewsContract.NewsSourceEntry.CONTENT_URI,
                    null,
                    NewsContract.NewsSourceEntry.COLUMN_USE + " = ?",
                    new String[]{"1"},
                    NewsContract.NewsSourceEntry._ID + " ASC"
            );
        }
    }
}
