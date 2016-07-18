package ru.tinkoff.cake;

import android.content.Context;

public class LicenceManager {

    private final PreferencesHelper preferencesHelper;

    public LicenceManager(Context context) {
        preferencesHelper = new PreferencesHelper(context);
    }

    public boolean isUserCanPlay() {
        return preferencesHelper.getTopScores().size() < 3;
    }
}
