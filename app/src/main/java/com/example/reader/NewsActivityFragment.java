package com.example.reader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
public class NewsActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // 非同期で処理を実行するLoaderを生成します.
        // ここを切り替えてあげるだけで様々な非同期処理に対応できます.
        if(args != null) {
            String url = args.getString("url");
            String sourceId = args.getString("sourceId");
            Loader<Cursor> loade = new FetchNewsTaskLoader(getActivity(), adapter, url, sourceId);
            loade.forceLoad();

            return loade;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        if(arg1 != null && arg1.getCount() > 0){
            adapter.changeCursor(arg1);
//            adapter.notifyAll();
        }
//
//        // 非同期処理が終了したら呼ばれます.
//        // 今回はDownloadが完了した画像をImageViewに表示します.
//        ImageView imageView = (ImageView)findViewById(R.id.imageview);
//        Drawable iconImage = new BitmapDrawable(getResources(), bmp);
//        imageView.setImageDrawable(iconImage);
//        imageView.invalidate();
    }
    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {

    }

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

        (new LoadDBAsyncTask()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        mCursor = getActivity().getContentResolver().query(NewsContract.NewsEntry.CONTENT_WITH_SOURCE_URI.buildUpon().appendPath(String.valueOf(mNewsSourceId)).build(), null, null, null, null);
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
//        FetchNewsTask fetchTask = new FetchNewsTask(getActivity(), adapter);
//        fetchTask.execute(mUrl, String.valueOf(mNewsSourceId));
        Bundle args = new Bundle();
        args.putString("url", mUrl);
        args.putString("sourceId", String.valueOf(mNewsSourceId));
        getActivity().getSupportLoaderManager().initLoader(0, args, this);
    }

    private class LoadDBAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            mCursor = getActivity().getContentResolver().query(NewsContract.NewsEntry.CONTENT_WITH_SOURCE_URI.buildUpon().appendPath(String.valueOf(mNewsSourceId)).build(), null, null, null, null);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.changeCursor(mCursor);
//            adapter = new NewsAdapter(getActivity(), mCursor, 1);
//        ListView listView = (ListView)getView().findViewById(R.id.listview_news_list);

//            listView.setAdapter(adapter);
            Log.v("aaa", "owata");
            adapter.notifyDataSetChanged();
        }
    }
}
