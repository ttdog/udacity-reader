package com.example.reader;

//import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.util.Xml;
import android.widget.CursorAdapter;

import com.example.reader.data.NewsContract;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoshidayuuki on 2015/11/23.
 */
public class FetchNewsTaskLoader extends AsyncTaskLoader<Cursor> {
    Context mContext;
    CursorAdapter adapter;
    String mUrl;
    String mSourceId;

    public FetchNewsTaskLoader(Context context, CursorAdapter adapter, String url, String sourceId){
        super(context);

        mContext = context;
        this.adapter = adapter;
        mUrl = url;
        mSourceId = sourceId;
    }

    @Override
    public Cursor loadInBackground() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try{
            URL rssUrl = new URL(mUrl);

            urlConnection = (HttpURLConnection)rssUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null){
                Log.e("AsyncTask", "ERROR for connectting rss resource" + mUrl);
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(reader);

            String line;
            while((line = reader.readLine()) != null){
                buffer.append(line);
            }

            if(buffer.length() == 0){
                Log.v("aaa", "0000");
                return null;
            }

            List<ContentValues> list = parse(buffer.toString(), mSourceId);
            if(list != null){
                //saveList
                saveNewsData(list, mSourceId);
                String[] ret = new String[list.size()];

                return mContext.getContentResolver().query(NewsContract.NewsEntry.CONTENT_WITH_SOURCE_URI.buildUpon().appendPath(String.valueOf(mSourceId)).build(), null, null, null, null);
            }
        }
        catch (IOException e){

        }
        catch (XmlPullParserException e){
            Log.e("AsyncTask", "parse error");
        }
        finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                }
                catch (final IOException e){
                    Log.e("AsyncTask", "Error closing stream");
                }
            }
        }
        return null;
    }

    private void saveNewsData(List<ContentValues> news, String sourceId){
        ContentValues[] arrays = new ContentValues[news.size()];
        news.toArray(arrays);

        mContext.getContentResolver().bulkInsert(NewsContract.NewsEntry.CONTENT_WITH_SOURCE_URI.buildUpon().appendPath(sourceId).build(), arrays);
    }

    private List<ContentValues> parse(String xml, String sourceId){
        List<ContentValues> list = new ArrayList<ContentValues>();

        XmlPullParser xmlPullParser = Xml.newPullParser();

        try{
            xmlPullParser.setInput(new StringReader(xml));
        }
        catch (XmlPullParserException e){
            Log.e("AsyncTask", "Error parsing");

            return null;
        }

        try{
            int eventType;
            String data = null;
            int itemFlg = -1;
            String fieldName = null;
            ContentValues item = null;

            eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:{
                        break;
                    }
                    case XmlPullParser.START_TAG:{
                        data = xmlPullParser.getName();

                        Long now = System.currentTimeMillis();
                        if(data.equals("item")){
                            itemFlg = 1;
                            item = new ContentValues();
                            //更新時間とフィードのidを設定する
                            item.put(NewsContract.NewsEntry.COLUMN_SOURCE_ID, Long.parseLong(sourceId));
                            item.put(NewsContract.NewsEntry.COLUMN_UPDATED_AT, now);
                        }
                        fieldName = data;
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        data = xmlPullParser.getName();
                        if(data.equals("item")){
                            itemFlg = 0;
                            list.add(item);
                        }
                        break;
                    }
                    case XmlPullParser.TEXT:{
                        data = xmlPullParser.getText();

                        if(itemFlg == 1){
                            if(fieldName.equals("title")){
                                item.put(NewsContract.NewsEntry.COLUMN_TITLE, data);
                                fieldName = "";
                            }
                            if(fieldName.equals("description")){
                                item.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, data);
                                fieldName = "";
                            }
                            if(fieldName.equals("link")){
                                item.put(NewsContract.NewsEntry.COLUMN_LINK, data);
                                fieldName = "";
                            }
                        }
                        break;
                    }
                }

                eventType = xmlPullParser.next();
            }
        }catch (Exception e){
            Log.e("AsyncTask", "error");
        }

        return list;
    }
}
