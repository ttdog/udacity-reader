package com.example.reader;

import android.content.Intent;
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

import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }
//    private static final String[] sourceTitles = {"はてな", "BBBB", "cccccc", "DDDDDD", "EEEEE", "FFFFF", "GGGGGG", "HHHHHH"};
//    private static final String[] sourceUrls = {"http://b.hatena.ne.jp/hotentry.rss", "http://BBBB", "http://cccccc", "http://DDDDDD", "http://EEEEE", "http://FFFFF", "http://GGGGGG", "http://HHHHHH"};
    private static final String[] sourceTitles = {"はてな"};
    private static final String[] sourceUrls = {"http://b.hatena.ne.jp/hotentry.rss"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        NewsSourceAdapter adapter = new NewsSourceAdapter(getActivity(), sourceTitles, sourceUrls);
        final ListView listView = (ListView)rootView.findViewById(R.id.listview_top_news_header);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView)view.findViewById(R.id.list_item_source_url)).getText()
                Log.v("aaa", ((TextView)view.findViewById(R.id.list_item_source_url)).getText().toString());

                Intent intent = new Intent(getActivity(), NewsActivity.class)
                        .putExtra("title", ((TextView)view.findViewById(R.id.list_item_source_title)).getText().toString())
                        .putExtra("url", ((TextView)view.findViewById(R.id.list_item_source_url)).getText().toString());

                startActivity(intent);
            }
        });

        return rootView;
    }
}
