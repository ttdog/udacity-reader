package com.example.reader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.reader.data.NewsContract;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        Intent intent = getIntent();
//
//        if(intent != null && intent.getExtras() != null){
//            setTitle(intent.getStringExtra("title"));
//
//            String url = intent.getStringExtra(NewsContract.NewsEntry.COLUMN_LINK);
//
//            WebViewActivityFragment fragment = (WebViewActivityFragment)getSupportFragmentManager().findFragmentById(R.id.web_fragment);
//            fragment.setUrl(url);
//        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();

        if(intent != null && intent.getExtras() != null){
            setTitle(intent.getStringExtra("title"));

            String url = intent.getStringExtra(NewsContract.NewsEntry.COLUMN_LINK);

            WebViewActivityFragment fragment = (WebViewActivityFragment)getSupportFragmentManager().findFragmentById(R.id.web_fragment);
            fragment.setUrl(url);
        }
    }
}
