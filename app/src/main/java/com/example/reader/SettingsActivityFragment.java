package com.example.reader;

import android.content.ContentValues;
import android.content.SharedPreferences;
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
import com.example.reader.data.NewsProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends PreferenceFragment {
    private Cursor mCursor;

    public SettingsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
//        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
//
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
//        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        mCursor = NewsContract.NewsSourceEntry.getAllRssSources(getActivity());
        CheckBoxPreference checkBoxPreference;
        PreferenceScreen sc = getPreferenceScreen();
        PreferenceCategory cate = (PreferenceCategory)sc.getPreference(0);

        if (mCursor != null && mCursor.moveToFirst()) {
            int nameColumn = mCursor.getColumnIndex(NewsContract.NewsSourceEntry.COLUMN_TITLE);
            int isUseColumn = mCursor.getColumnIndex(NewsContract.NewsSourceEntry.COLUMN_USE);
            int count = 0;
            do {
                checkBoxPreference = new CheckBoxPreference(getActivity());
                checkBoxPreference.setTitle(mCursor.getString(nameColumn));
                checkBoxPreference.setKey(String.valueOf(count));

                if(mCursor.getInt(isUseColumn) == 1){
                    checkBoxPreference.setChecked(true);
                }

                cate.addPreference(checkBoxPreference);
                ++count;
            } while (mCursor.moveToNext());
        }
    }

    private SharedPreferences.OnSharedPreferenceChangeListener onPreferenceChangeListenter = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.v("aaa", "change: " + key);

            if (mCursor != null && mCursor.moveToFirst()) {
                int idColumn = mCursor.getColumnIndex(NewsContract.NewsSourceEntry._ID);
                int count = 0;
                do {
                    if(Integer.parseInt(key) == count){
                        CheckBoxPreference pref = (CheckBoxPreference)findPreference(key);
                        String isCheck;
                        if(pref.isChecked()){
                            isCheck = "1";
                        }
                        else{
                            isCheck = "0";
                        }

                        int id = mCursor.getInt(idColumn);

                        ContentValues value = new ContentValues();
                        value.put(NewsContract.NewsSourceEntry.COLUMN_USE, isCheck);
                        //TODO: データの保存
                        getActivity().getContentResolver().update(NewsContract.NewsSourceEntry.CONTENT_URI, value, "_id = ?", new String[]{String.valueOf(id)});
                        break;
                    }
//                    checkBoxPreference = new CheckBoxPreference(getActivity());
//                    checkBoxPreference.setTitle(mCursor.getString(nameColumn));
//                    checkBoxPreference.setKey(String.valueOf(count));
//
//                    if(mCursor.getInt(isUseColumn) == 1){
//                        checkBoxPreference.setChecked(true);
//                    }
//
//                    cate.addPreference(checkBoxPreference);
                    ++count;
                } while (mCursor.moveToNext());
            }
//            if (key.equals(PREF_KEY_USERNAME)) {
//                EditTextPreference pref = (EditTextPreference)findPreference(key);
//                pref.setSummary(pref.getText());
//            } else if (key.equals(PREF_KEY_GENDER)) {
//                ListPreference pref = (ListPreference)findPreference(key);
//                pref.setSummary(pref.getEntry());
//            } else if (key.equals(PREF_KEY_FAVORITED_COLORS)) {
//                MultiSelectListPreference pref = (MultiSelectListPreference)findPreference(PREF_KEY_FAVORITED_COLORS);
//                pref.setSummary(multiSelectListSummary(PREF_KEY_FAVORITED_COLORS));
//            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(onPreferenceChangeListenter);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(onPreferenceChangeListenter);
    }
}
