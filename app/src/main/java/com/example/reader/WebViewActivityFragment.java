package com.example.reader;

import android.content.Intent;
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

    public WebViewActivityFragment() {
    }

    public void setUrl(String url){
        mWebView.loadUrl(url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
        mWebView = (WebView)rootView.findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient());

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
}
