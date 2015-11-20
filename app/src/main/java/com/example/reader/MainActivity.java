package com.example.reader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.reader.data.NewsContract;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback, NewsActivityFragment.NewsCallback{

    private boolean isMultiPane = false;

    private String NEWS_FRAGMENT_TAG = "newsFragment";
    private WebViewActivityFragment mWebFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(findViewById(R.id.web_fragment) != null){
            isMultiPane = true;

            MainActivityFragment fragment = new MainActivityFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //TODO: intentの作成
            Intent settingIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String title, String url, Long id){
        Log.v("aaa", "callback");
        if(isMultiPane){
            Bundle args = new Bundle();
            args.putString(NewsContract.NewsSourceEntry.COLUMN_TITLE, title);
            args.putString(NewsContract.NewsSourceEntry.COLUMN_URL, url);
            args.putString(NewsContract.NewsSourceEntry._ID, String.valueOf(id));

            NewsActivityFragment fragment = new NewsActivityFragment();
            fragment.setArguments(args);
//
            FragmentManager manger = getSupportFragmentManager();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

//            transaction.remove(findViewById(R.id.main_fragment));
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.main_fragment, fragment, NEWS_FRAGMENT_TAG)
//                    .addToBackStack(null)
//                    .commit();

//            fragment.setDatas(url, title, id);
        }
        else{
            Intent intent = new Intent(this, NewsActivity.class)
                    .putExtra(NewsContract.NewsSourceEntry.COLUMN_TITLE, title)
                    .putExtra(NewsContract.NewsSourceEntry.COLUMN_URL, url)
                    .putExtra(NewsContract.NewsSourceEntry._ID, String.valueOf(id));
            startActivity(intent);
        }
    }

    @Override
    public void onNewsSelected(String url, String title){
        Log.v("aaa", "news callback multi");

        mWebFragment = (WebViewActivityFragment)getSupportFragmentManager().findFragmentById(R.id.web_fragment);
        mWebFragment.setUrl(url);
    }
}
