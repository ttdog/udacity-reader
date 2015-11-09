package com.example.reader;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment {

    public NewsActivityFragment() {
    }

//    private static final String[] sourceTitles = {"aaaa", "bbb", "cccccc", "DDDDDD", "EEEEE", "FFFFF", "GGGGGG", "HHHHHH"};
//    private static final String[] sourceUrls = {"bbbb", "http://BBBB", "http://cccccc", "http://DDDDDD", "http://EEEEE", "http://FFFFF", "http://GGGGGG", "http://HHHHHH"};

    private ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        ArrayList<String> items = new ArrayList<String>(Arrays.asList("ダウンタウン", "バナナマン", "オードリー"));

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
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
        fetchTask.execute("http://b.hatena.ne.jp/hotentry.rss");
    }
}
