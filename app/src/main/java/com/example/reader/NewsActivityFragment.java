package com.example.reader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;

import com.example.reader.data.NewsContract;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment {

    public interface NewsCallback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onNewsSelected(String url, String title);
    }

    public NewsActivityFragment() {
    }

    private NewsAdapter adapter;

    private long mNewsSourceId;
    private String mUrl;
    private String mTitle;
    private Cursor mCursor;

    private ListView listView;

    public void setDatas(String url, String title, long sourceId){
        mUrl = url;
        mTitle = title;
        mNewsSourceId = sourceId;

        setNews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//
//        Intent intent = getActivity().getIntent();
//        mUrl = intent.getStringExtra(NewsContract.NewsSourceEntry.COLUMN_URL);
//        mTitle = intent.getStringExtra(NewsContract.NewsSourceEntry.COLUMN_TITLE);
//        mNewsSourceId = Long.parseLong(intent.getStringExtra(NewsContract.NewsSourceEntry._ID));

//        super.onCreateView(inflater, container, savedInstanceState);
        Log.v("aaa", "oncreate url: " + mUrl + ", title: " + mTitle + ", id: " + mNewsSourceId);

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        listView = (ListView)rootView.findViewById(R.id.listview_news_list);

        Bundle arguments = getArguments();
        if (arguments != null) {
            Log.v("aaa", null);
            setDatas(arguments.getString(NewsContract.NewsSourceEntry.COLUMN_URL),
                    arguments.getString(NewsContract.NewsSourceEntry.COLUMN_TITLE),
                    Long.parseLong(arguments.getString(NewsContract.NewsSourceEntry._ID)));
//            mTitle = arguments.getString(NewsContract.NewsSourceEntry.COLUMN_TITLE);
//            mUrl = arguments.getString(NewsContract.NewsSourceEntry.COLUMN_URL);
//            mNewsSourceId = Long.parseLong(arguments.getString(NewsContract.NewsSourceEntry._ID));
//
//            setNews();
        }
//
//        mCursor = getActivity().getContentResolver().query(NewsContract.NewsEntry.CONTENT_WITH_SOURCE_URI.buildUpon().appendPath(String.valueOf(mNewsSourceId)).build(), null, null, null, null);
//
//        adapter = new NewsAdapter(getActivity(), mCursor, 1);
//        final ListView listView = (ListView)rootView.findViewById(R.id.listview_news_list);
//
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                if(((NewsAdapter.ViewHolder)view.getTag()).url != null){
//                    Log.v("aaa", ((NewsAdapter.ViewHolder)view.getTag()).url);
//
//                    Intent intent = new Intent(getActivity(), WebViewActivity.class)
//                            .putExtra(NewsContract.NewsEntry.COLUMN_LINK, ((NewsAdapter.ViewHolder)view.getTag()).url);
//
//                    startActivity(intent);
//                }
//
//            }
//        });
//
//        updateNews();

        return rootView;
    }

    private void setNews(){
        Log.v("aaa", "url: " + mUrl + ", title: " + mTitle + ", id: " + mNewsSourceId);

        mCursor = getActivity().getContentResolver().query(NewsContract.NewsEntry.CONTENT_WITH_SOURCE_URI.buildUpon().appendPath(String.valueOf(mNewsSourceId)).build(), null, null, null, null);
Log.v("aaa", "count :" +getActivity().toString());
        adapter = new NewsAdapter(getActivity(), mCursor, 1);
//        ListView listView = (ListView)getView().findViewById(R.id.listview_news_list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(((NewsAdapter.ViewHolder)view.getTag()).url != null){
                    Log.v("aaa", ((NewsAdapter.ViewHolder) view.getTag()).url);

                    ((NewsCallback)getActivity()).onNewsSelected(((NewsAdapter.ViewHolder)view.getTag()).url, ((NewsAdapter.ViewHolder)view.getTag()).title.getText().toString());
//
//                    Intent intent = new Intent(getActivity(), WebViewActivity.class)
//                            .putExtra(NewsContract.NewsEntry.COLUMN_LINK, ((NewsAdapter.ViewHolder)view.getTag()).url)
//                            .putExtra("title", ((NewsAdapter.ViewHolder)view.getTag()).title.getText());
//
//                    startActivity(intent);
                }

            }
        });

        updateNews();
    }

    private void updateNews(){
        Log.v("aaa", "updateTask");
        FetchNewsTask fetchTask = new FetchNewsTask(getActivity(), adapter);
        fetchTask.execute(mUrl, String.valueOf(mNewsSourceId));
    }
}
