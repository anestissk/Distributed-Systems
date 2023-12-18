package com.example.androiduser;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.androiduser.R;


public class SharedPreferencesHelper {
    private static final String SHARED_PREFS_NAME = "MyPrefs";
    private static final String KEY_HOSTNAME = "hostname";
    private static final String KEY_PORT = "port";
    private static final String KEY_USERNAME = "username";

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveHostname(String hostname) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_HOSTNAME, hostname);
        editor.apply();
    }

    public String getHostname() {
        return sharedPreferences.getString(KEY_HOSTNAME, "default_hostname");
    }

    public void savePort(int port) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_PORT, port);
        editor.apply();
    }

    public int getPort() {
        return sharedPreferences.getInt(KEY_PORT, 5678);
    }

    public void saveUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "default_username");
    }
}
