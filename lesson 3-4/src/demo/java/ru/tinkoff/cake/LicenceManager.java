package ru.tinkoff.cake;

import android.content.Context;
import android.content.SharedPreferences;

public class LicenceManager {

    private int LIMIT = 4;
    private String SHARED_VALUE_LIMIT = "limit";
    private int DEFAULT_VALUE = 0;

    private SharedPreferences sharedPreferences;

    public LicenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

    }

    public boolean isUserCanPlay() {

        int gamePLayed = sharedPreferences.getInt(SHARED_VALUE_LIMIT, DEFAULT_VALUE);
        gamePLayed++;
        sharedPreferences.edit().putInt(SHARED_VALUE_LIMIT, gamePLayed).apply();
        if (gamePLayed < LIMIT) {
            return true;
        } else {
            return false;
        }
    }
}
