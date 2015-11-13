package com.example.reader;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reader.data.NewsContract;

/**
 * Created by 01011549 on 15/11/12.
 */
public class NewsAdapter extends CursorAdapter {
    public NewsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static class ViewHolder{
        public final TextView title;

        public ViewHolder(View view){
            title = (TextView) view.findViewById(android.R.id.text1);
        }
    }

//    /**
//     * Prepare the weather high/lows for presentation.
//     */
//    private String formatHighLows(double high, double low) {
//        boolean isMetric = Utility.isMetric(mContext);
//        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
//        return highLowStr;
//    }
//
//    /*
//        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
//        string.
//     */
//    private String convertCursorRowToUXFormat(Cursor cursor) {
////        // get row indices for our cursor
////        int idx_max_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
////        int idx_min_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);
////        int idx_date = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
////        int idx_short_desc = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);
//
//        String highAndLow = formatHighLows(
//                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
//                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));
//
//        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
//                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
//                " - " + highAndLow;
//    }
//
//    private static final int VIEW_TYPE_COUNT = 2;
//    private static final int VIEW_TYPE_TODAY = 0;
//    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        int viewType = getItemViewType(cursor.getPosition());
//        int layoutId = -1;
//        switch (viewType){
//            case VIEW_TYPE_TODAY:{
//                layoutId = R.layout.list_item_forecast_today;
//                break;
//            }
//            case VIEW_TYPE_FUTURE_DAY:{
//                layoutId = R.layout.list_item_forecast;
//                break;
//            }
//        }

        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;

//        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

//        viewHolder.iconView.setImageResource(R.mipmap.ic_launcher);

        viewHolder.title.setText(cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE)));
//
//        int viewType = getItemViewType(cursor.getPosition());
//        switch (viewType){
//            case VIEW_TYPE_TODAY:{
//                viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
//                break;
//            }
//            case VIEW_TYPE_FUTURE_DAY:{
//                viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
//                break;
//            }
//        }
//
//        long dateInMills = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
//        viewHolder.dateView.setText(Utility.getFriendlyDayString(mContext, dateInMills));
//
//        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
//        viewHolder.descriptionView.setText(description);
//
//        boolean isMetric = Utility.isMetric(context);
//
//        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
//        viewHolder.highTempView.setText(Utility.formatTemperature(context, high, isMetric));
//
//        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
//        viewHolder.lowTempView.setText(Utility.formatTemperature(context, low, isMetric));
    }

//    @Override
//    public int getItemViewType(int position){
//        return position == 0 ? VIEW_TYPE_TODAY: VIEW_TYPE_FUTURE_DAY;
//    }
//
//    @Override
//    public int getViewTypeCount(){
//        return VIEW_TYPE_COUNT;
//    }
}
