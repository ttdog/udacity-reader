package com.example.reader;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
 * Created by 01011549 on 15/11/05.
 */
public class FetchNewsTask extends AsyncTask<String, Void, String[]>{
    private final Context mContext;
    private ArrayAdapter<String> adapter;

    public FetchNewsTask(Context context, ArrayAdapter<String> adapter){
        mContext = context;
        this.adapter = adapter;
    }

    @Override
    protected String[] doInBackground(String... params){
        if(params.length == 0){
            return null;
        }

        String url = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try{
            URL rssUrl = new URL(url);

            urlConnection = (HttpURLConnection)rssUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null){
                Log.e("AsyncTask", "ERROR for connectting rss resource" + url);
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
                return null;
            }

            List<ContentValues> list = parse(buffer.toString());
            if(list != null){
                //saveList
                saveNewsData(list);
                String[] ret = new String[list.size()];

                for(int i = 0, length = list.size(); i < length; i++){
                    ContentValues value = list.get(i);
                    ret[i] = (String)value.get(NewsContract.NewsEntry.COLUMN_TITLE);
                }

                return ret;
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

    private void saveNewsData(List<ContentValues> news){
        ContentValues[] arrays = new ContentValues[news.size()];
        news.toArray(arrays);
    }

    private List<ContentValues> parse(String xml){
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
                        Log.d("AsyncTask", "start document");
                        break;
                    }
                    case XmlPullParser.START_TAG:{
                        data = xmlPullParser.getName();
                        Log.d("AsyncTask", "Start tag "+ data);
                        if(data.equals("item")){
                            itemFlg = 1;
                            item = new ContentValues();
                        }
                        fieldName = data;
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        data = xmlPullParser.getName();
                        Log.d("AsyncTask", "End tag "+ data);
                        if(data.equals("item")){
                            itemFlg = 0;
                            list.add(item);
                        }
                        break;
                    }
                    case XmlPullParser.TEXT:{
                        data = xmlPullParser.getText();
                        Log.d("AsyncTask", "Text "+ data);

                        if(itemFlg == 1){
                            if(fieldName.equals("title")){
                                Log.d("AsyncTask", "title = "+ data);
                                item.put(NewsContract.NewsEntry.COLUMN_TITLE, data);
                                fieldName = "";
                            }
                            if(fieldName.equals("description")){
                                Log.d("AsyncTask", "description = "+ data);
                                item.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, data);
                                fieldName = "";
                            }
                            if(fieldName.equals("link")){
                                Log.d("AsyncTask", "link = "+ data);
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

    @Override
    protected void onPostExecute(String[] result) {
//        Log.v("aaa", result.toString());
        if (result != null && adapter != null) {
            adapter.clear();
            for(String title : result) {
                adapter.add(title);
            }
            // New data is back from the server.  Hooray!
        }
    }
}
