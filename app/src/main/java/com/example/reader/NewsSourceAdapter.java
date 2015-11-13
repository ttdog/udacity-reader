package com.example.reader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by 01011549 on 15/10/30.
 */
public class NewsSourceAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater mInflater;

    private String[] titles;
    private String[] urls;

    static class ViewHolder{
        TextView title;
        TextView url;
    }

    public NewsSourceAdapter(Context context, String[] titles, String[] urls){
        this(context);

        this.titles = titles;
        this.urls = urls;
    }

    public NewsSourceAdapter(Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(String[] titles, String[] urls){
        this.titles = titles;
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_news_source, null);
            holder.title = (TextView)convertView.findViewById(R.id.list_item_source_title);
            holder.url = (TextView)convertView.findViewById(R.id.list_item_source_url);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.title.setText(titles[position]);
        holder.url.setText(urls[position]);

        return convertView;
    }
}
