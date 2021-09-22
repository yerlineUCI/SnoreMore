package com.example.snoremore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SharedPreferenceManager  {
    private static  Context mContext;
    private static SharedPreferences mSettings;
    protected SharedPreferences.Editor mEditor;

    private static final String MY_PREFERENCE_NAME = "com.example.snoremore";
    private static final String PREF_KEY = "pref_total_key";

    public SharedPreferenceManager(Context context, String text) {
        mContext = context;
        mSettings = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    public void setValue(String key, String text) {
        mEditor.putString(key, text);
        mEditor.commit();
    }

    public void setValue_bool(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public void setValue_int(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void setValue_double(String key, double value) {
        setValue(key, Double.toString(value));
    }

    public void setValue_Calendar(String key, Date value) {
        mEditor.putLong(key, value.getTime());
        mEditor.commit();
    }

    public void setValue_long(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public String getValue(String key, String text) {
        return mSettings.getString(key, "");
    }

    public long getValue_long(String key, long value) {
        return mSettings.getLong(key, 0L);
    }

    public int getValue_int(String key, int value) {
        return mSettings.getInt(key, 0);
    }

    public boolean getValue_bool(String key, boolean Value) {
        return mSettings.getBoolean(key, false);
    }

    public static void removeText(Context context, String text) {
        SharedPreferences preferences = context.getSharedPreferences(MY_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
