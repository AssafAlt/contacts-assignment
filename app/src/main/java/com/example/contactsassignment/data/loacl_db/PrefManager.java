package com.example.contactsassignment.data.loacl_db;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String PREF_NAME = "LoginPref";
    private static final String KEY_USER_ID = "userId";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    // Constructor
    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Save user ID
    public void saveUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    // Retrieve user ID
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1); // Default value -1 if not found
    }
}
