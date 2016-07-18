package ru.tinkoff.cake.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ru.tinkoff.cake.data.Score;

public class PreferencesHelper {

    private static final String KEY_NAME = "SP_KEY_PLAYER_NAME";
    private static final String KEY_SCORE = "SP_KEY_PLAYER_SCORE";
    private static final String KEY_SCORES_COUNT = "SP_KEY_PLAYER_SCORES_COUNT";
    private static final String KEY_LAST_USED_NAME = "SP_KEY_LAST_USED_NAME";

    private final SharedPreferences preferences;

    public PreferencesHelper(Context context) {
        preferences = context.getSharedPreferences("top_score", Context.MODE_PRIVATE);
    }

    public List<Score> getTopScores() {
        List<Score> results = new ArrayList<>();
        int savedScoresCount = preferences.getInt(KEY_SCORES_COUNT, 0);
        for (int i = 0; i < savedScoresCount; i++) {
            String name = preferences.getString(KEY_NAME + i, null);
            int score = preferences.getInt(KEY_SCORE + i, -1);
            if (name != null && score != -1) {
                results.add(new Score(name, score));
            }
        }
        return results;
    }

    public void saveTopScores(List<Score> topScores) {
        SharedPreferences.Editor editor = preferences.edit();
        int size = topScores.size();
        for (int i = 0; i < size; i++) {
            Score score = topScores.get(i);
            editor.putString(KEY_NAME + i, score.getName());
            editor.putInt(KEY_SCORE + i, score.getScore());
        }
        editor.putInt(KEY_SCORES_COUNT, size);
        editor.apply();
    }

    @Nullable
    public String getLastUsedName() {
        return preferences.getString(KEY_LAST_USED_NAME, null);
    }

    public void saveLastUsedName(@NonNull String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_LAST_USED_NAME, name);
        editor.apply();
    }
}
