package com.example.reader;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.reader.data.NewsContract;

public class NewsActivity extends AppCompatActivity implements NewsActivityFragment.NewsCallback{

    private String mUrl;
    private String mTitle;
    private Long mNewsSourceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        Log.v("aaa", "NewsActivity created");

        if(intent != null && intent.getExtras() != null){
            Log.v("aaa", "have intent");
            setTitle(intent.getStringExtra("title"));

            mUrl = intent.getStringExtra(NewsContract.NewsSourceEntry.COLUMN_URL);
            mTitle = intent.getStringExtra(NewsContract.NewsSourceEntry.COLUMN_TITLE);
            mNewsSourceId = Long.parseLong(intent.getStringExtra(NewsContract.NewsSourceEntry._ID));
            NewsActivityFragment fragment = (NewsActivityFragment)getSupportFragmentManager().findFragmentById(R.id.news_fragment);
            fragment.setDatas(mUrl, mTitle, mNewsSourceId);
        }
        else if(savedInstanceState != null){
            Log.v("aaa", "no bundle");
        }
//        else{
//            Log.v("aaa", "no intent");
//            mUrl = savedInstanceState.getString(NewsContract.NewsSourceEntry.COLUMN_URL);
//            mTitle = savedInstanceState.getString(NewsContract.NewsSourceEntry.COLUMN_TITLE);
//            mNewsSourceId = savedInstanceState.getLong(NewsContract.NewsSourceEntry._ID);
//        }
//
//        NewsActivityFragment fragment = (NewsActivityFragment)getSupportFragmentManager().findFragmentById(R.id.news_fragment);
//        fragment.setDatas(mUrl, mTitle, mNewsSourceId);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.v("aaa", "onRestart");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.v("aaa", "onResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.v("aaa", "onPause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.v("aaa", "onStop");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.v("aaa", "onDestroy");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v("aaa", "saved!!! "+ mUrl);

        outState.putString(NewsContract.NewsSourceEntry.COLUMN_URL, mUrl);
        outState.putString(NewsContract.NewsSourceEntry.COLUMN_TITLE, mTitle);
        outState.putLong(NewsContract.NewsSourceEntry._ID, mNewsSourceId);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.v("aaa", "restore: " + savedInstanceState.getString(NewsContract.NewsSourceEntry.COLUMN_URL));
    }

    @Override
    public void onNewsSelected(String url, String title){
        Log.v("aaa", "news callback single");
            Intent intent = new Intent(this, WebViewActivity.class)
                    .putExtra(NewsContract.NewsEntry.COLUMN_LINK, url)
                    .putExtra("title", title);

            startActivity(intent);
    }
}
