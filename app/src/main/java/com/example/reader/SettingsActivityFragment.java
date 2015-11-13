package com.example.reader;

import android.database.Cursor;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reader.data.NewsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends PreferenceFragment {

    public SettingsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

//        addPreferencesFromResource(R.xml.preference);

//        Cursor cursor = NewsContract.NewsSourceEntry.getAllRssSources(getActivity());
//        CheckBoxPreference checkBoxPreference;
//        PreferenceScreen sc = getPreferenceScreen();
//        PreferenceCategory cate = (PreferenceCategory)sc.getPreference(0);

//        if (cursor != null && cursor.moveToFirst()) {
//            int nameColumn = cursor.getColumnIndex(NewsContract.NewsSourceEntry.COLUMN_TITLE);
//            int isUseColumn = cursor.getColumnIndex(NewsContract.NewsSourceEntry.COLUMN_USE);
//            int count = 0;
//            do {
//                checkBoxPreference = new CheckBoxPreference(getActivity());
//                checkBoxPreference.setTitle(cursor.getString(nameColumn));
//                checkBoxPreference.setKey(String.valueOf(count));
//
//                if(cursor.getInt(isUseColumn) == 1){
//                    checkBoxPreference.setChecked(true);
//                }
//
//                Log.v("aaa", "abc");
//                cate.addPreference(checkBoxPreference);
//                ++count;
//            } while (cursor.moveToNext());
//        }
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.preference);
    }
}
