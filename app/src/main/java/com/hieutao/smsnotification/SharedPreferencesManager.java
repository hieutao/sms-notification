package com.hieutao.smsnotification;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String APP_SETTINGS = "APP_SETTINGS";

    private static final String UNIQUE_ID = "UNIQUE_ID";

    private SharedPreferencesManager() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static String getUniqueId(Context context) {
        return getSharedPreferences(context).getString(UNIQUE_ID, null);
    }

    public static void setUniqueId(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(UNIQUE_ID, newValue);
        editor.apply();
    }
}
