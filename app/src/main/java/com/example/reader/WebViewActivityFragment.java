package com.example.reader;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.reader.data.NewsContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class WebViewActivityFragment extends Fragment {

    private WebView mWebView;
    private FloatingActionButton mBackButton;
    private FloatingActionButton mNextButton;
    private MyWebViewClient mClient;

    public WebViewActivityFragment() {
    }

    public void setUrl(String url){
        mWebView.loadUrl(url);
        mClient.doUpdateVisitedHistory(mWebView, url, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
        mWebView = (WebView)rootView.findViewById(R.id.webView);
        mClient = new MyWebViewClient();
        mWebView.setWebViewClient(mClient);

        mBackButton = (FloatingActionButton)rootView.findViewById(R.id.fab_back);
        mBackButton.setEnabled(false);

//        mBackButton.setBackgroundTintList(new ColorStateList(
//                new int[][] {
//                        new int[]{ android.R.attr.state_enabled },
////                        new int[]{ -android.R.attr.state_enabled },
//                },
//                new int[] {
//                        Color.argb(0xff, 0xff, 0xff, 0xff),
////                        Color.argb(0xff,0x8f,0x8f,0x8f),
//                }
//        ));


        mNextButton = (FloatingActionButton)rootView.findViewById(R.id.fab_next);
        mNextButton.setEnabled(false);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWebView.canGoBack()){
                    mWebView.goBack();
                }
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWebView.canGoForward()){
                    mWebView.goForward();
                }
            }
        });

//        Intent intent = getActivity().getIntent();
//        String url = intent.getStringExtra(NewsContract.NewsEntry.COLUMN_LINK);
//
//        Log.v("bbb", "url: "+url);
//
//        if(url != null){
//            mWebView.loadUrl(url);
//        }

        return rootView;
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView aView, String aUrl, Bitmap aFavicon) {
            super.onPageStarted(aView, aUrl, aFavicon);
        }

        @Override
        public void onPageFinished(WebView aView, String aUrl) {
            super.onPageFinished(aView, aUrl);
            if(aView.canGoBack()){
                mBackButton.setEnabled(true);
            }
            else{
                mBackButton.setEnabled(false);
            }

            if(aView.canGoForward()){
                mNextButton.setEnabled(true);
            }
            else{
                mNextButton.setEnabled(false);
            }
        }
    }
}
