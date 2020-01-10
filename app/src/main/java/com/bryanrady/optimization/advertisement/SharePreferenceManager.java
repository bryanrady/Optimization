package com.bryanrady.optimization.advertisement;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class SharePreferenceManager {

    private static SharedPreferences sp = null;

    private static final String KEY_LAST_REQUEST_TIME = "last_request_time";
    private static final String KEY_RESOURCE_URL_JSON = "resource_url_json";

    public static void init(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static void setLastRefreshTime(long refreshTime) {
        if (sp != null) {
            sp.edit().putLong(KEY_LAST_REQUEST_TIME, refreshTime).apply();
        }
    }

    public static long getLastRefreshTime() {
        if (sp != null) {
            return sp.getLong(KEY_LAST_REQUEST_TIME,0);
        }
        return 0;
    }

    public static void setResourceUrlJson(String urlJson) {
        if (sp != null) {
            sp.edit().putString(KEY_RESOURCE_URL_JSON, urlJson).apply();
        }
    }

    public static String getResourceUrlJson() {
        if (sp != null) {
            return sp.getString(KEY_RESOURCE_URL_JSON,null);
        }
        return null;
    }

}
