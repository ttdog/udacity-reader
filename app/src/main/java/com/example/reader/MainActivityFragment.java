package com.example.reader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.reader.data.NewsContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    //列の順
    static final int COL_NEWS_SOURCE_ID = 0;
    static final int COL_NEWS_SOURCE_TITLE = 1;
    static final int COL_NEWS_SOURCE_URL = 2;
    static final int COL_NEWS_SOURCE_USE = 3;

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String title, String url, Long id);
    }

    public MainActivityFragment() {
    }

    private Cursor mCursor;
    NewsSourceAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        mCursor = getActivity().getContentResolver().query(
//                NewsContract.NewsSourceEntry.CONTENT_URI,
//                null,
//                NewsContract.NewsSourceEntry.COLUMN_USE + " = ?",
//                new String[]{"1"},
//                NewsContract.NewsSourceEntry._ID + " ASC"
//        );

//        Thread th = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mCursor = NewsContract.NewsSourceEntry.getAllActiveRssSources(getActivity());
//            }
//        });
//        th.start();
//        mCursor = NewsContract.NewsSourceEntry.getAllActiveRssSources(getActivity());


//        adapter = new NewsSourceAdapter(getActivity(), NewsContract.NewsSourceEntry.sourceTitles, NewsContract.NewsSourceEntry.sourceUrls);
        adapter = new NewsSourceAdapter(getActivity());
//        updateSourceList();
        final ListView listView = (ListView)rootView.findViewById(R.id.listview_top_news_header);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = getCursorIndex(position);

                if(cursor != null){
                    Log.v("aaa", "clicked: " + mCursor.getString(COL_NEWS_SOURCE_TITLE) + ", "+mCursor.getString(COL_NEWS_SOURCE_ID));
                    ((Callback) getActivity()).onItemSelected(mCursor.getString(COL_NEWS_SOURCE_TITLE),
                            mCursor.getString(COL_NEWS_SOURCE_URL),
                            Long.parseLong(mCursor.getString(COL_NEWS_SOURCE_ID)));
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        Log.v("aaa", "resume");
//        mCursor = NewsContract.NewsSourceEntry.getAllActiveRssSources(getActivity());
//        updateSourceList();

        LoadListAsyncTask task = new LoadListAsyncTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
//        Log.d(TAG, methodName);
        super.onResume();
    }

    private Cursor getCursorIndex(int index){
        if(mCursor != null && mCursor.moveToPosition(index)){
            return mCursor;
        }
        return null;
    }

    private void updateSourceList(){
        if (mCursor != null && mCursor.moveToFirst()) {
            List<String> titles = new ArrayList<String>();
            List<String> urls = new ArrayList<String>();
            int nameColumn = mCursor.getColumnIndex(NewsContract.NewsSourceEntry.COLUMN_TITLE);
            int phoneColumn = mCursor.getColumnIndex(NewsContract.NewsSourceEntry.COLUMN_URL);
            int useColumn = mCursor.getColumnIndex(NewsContract.NewsSourceEntry.COLUMN_USE);

            int count = 0;
            do {
//                Log.v("aaa", "isUse: " + String.valueOf(mCursor.getInt(useColumn)));
//                if(mCursor.getInt(useColumn) == 1){
                    titles.add(mCursor.getString(nameColumn));
                    urls.add(mCursor.getString(phoneColumn));
//                }
                ++count;
            } while (mCursor.moveToNext());

            adapter.setData((String[])titles.toArray(new String[0]), (String[])urls.toArray(new String[0]));
            adapter.notifyDataSetChanged();
        }
    }

    private class LoadListAsyncTask extends AsyncTask<Void, Integer, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            mCursor = NewsContract.NewsSourceEntry.getAllActiveRssSources(getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            updateSourceList();
        }
    }
}
