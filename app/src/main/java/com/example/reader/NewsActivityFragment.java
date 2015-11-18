package com.example.reader;

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

    public NewsActivityFragment() {
    }

    private NewsAdapter adapter;

    private long mNewsSourceId;
    private String mUrl;
    private String mTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        mUrl = intent.getStringExtra(NewsContract.NewsSourceEntry.COLUMN_URL);
        mTitle = intent.getStringExtra(NewsContract.NewsSourceEntry.COLUMN_TITLE);
        mNewsSourceId = Long.parseLong(intent.getStringExtra(NewsContract.NewsSourceEntry._ID));

        Log.v("aaa", "url: " + mUrl + ", title: " + mTitle + ", id: " + mNewsSourceId);

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        Cursor cursor = getActivity().getContentResolver().query(NewsContract.NewsEntry.CONTENT_WITH_SOURCE_URI.buildUpon().appendPath(String.valueOf(mNewsSourceId)).build(), null, null, null, null);

        adapter = new NewsAdapter(getActivity(), cursor, 1);
//        ArrayList<String> items = new ArrayList<String>(Arrays.asList("ダウンタウン", "バナナマン", "オードリー"));

//        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
//        NewsSourceAdapter adapter = new NewsSourceAdapter(getActivity(), sourceTitles, sourceUrls);
        final ListView listView = (ListView)rootView.findViewById(R.id.listview_news_list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.v("aaa", ((TextView) view.findViewById(R.id.list_item_source_url)).getText().toString());
//
//                Intent intent = new Intent(getActivity(), NewsActivity.class)
//                        .putExtra("title", ((TextView)view.findViewById(R.id.list_item_source_title)).getText().toString())
//                        .putExtra("url", ((TextView)view.findViewById(R.id.list_item_source_url)).getText().toString());
//
//                startActivity(intent);
            }
        });

        updateNews();

        return rootView;
    }

    private void updateNews(){
        FetchNewsTask fetchTask = new FetchNewsTask(getActivity(), adapter);
        fetchTask.execute(mUrl, String.valueOf(mNewsSourceId));
    }
}
