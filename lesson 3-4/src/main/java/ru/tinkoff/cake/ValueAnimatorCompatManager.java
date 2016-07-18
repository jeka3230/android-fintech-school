package ru.tinkoff.cake;


import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;

import java.util.Set;

public class ValueAnimatorCompatManager {

    private Implementation implementation;

    public ValueAnimatorCompatManager() {
        implementation = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ?
                new KitkatAndAboveImpl() : new CompatImpl();
    }

    public void pause(ValueAnimator animator, String key) {
        implementation.pause(animator, key);
    }

    public void resume(ValueAnimator animator, String key) {
        implementation.resume(animator, key);
    }

    public void onSaveInstanceState(Bundle bundle) {
        implementation.onSaveInstanceState(bundle);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        implementation.onRestoreInstanceState(bundle);
    }

    private interface Implementation {

        void pause(ValueAnimator animator, String key);

        void resume(ValueAnimator animator, String key);

        void onSaveInstanceState(Bundle bundle);

        void onRestoreInstanceState(Bundle bundle);
    }

    private static class KitkatAndAboveImpl implements Implementation {

        @Override
        @TargetApi(19)
        public void pause(ValueAnimator animator, String key) {
            animator.pause();
        }

        @Override
        @TargetApi(19)
        public void resume(ValueAnimator animator, String key) {
            animator.resume();
        }

        @Override
        public void onSaveInstanceState(Bundle bundle) {
            // do nothing
        }

        @Override
        public void onRestoreInstanceState(Bundle bundle) {
            // do nothing
        }
    }

    private static class CompatImpl implements Implementation {


        private static final String STATE_KEY_KEYCHAIN = "DangerKeychain!";

        private ArrayMap<String, Long> map = new ArrayMap<>();

        @Override
        public void pause(ValueAnimator animator, String key) {
            long playTime = animator.getCurrentPlayTime();
            map.put(key, playTime);
            animator.cancel();
        }

        @Override
        public void resume(ValueAnimator animator, String key) {
            Long playTime = map.get(key);

            if (playTime != null) {
                animator.setCurrentPlayTime(playTime);
                animator.start();
            }
        }

        @Override
        public void onSaveInstanceState(Bundle bundle) {
            Set<String> keySet = map.keySet();
            String[] keychain = new String[keySet.size()];
            int keychainIndex = 0;

            for (String key : keySet) {
                Long value = map.get(key);
                bundle.putLong(key, value);
                keychain[keychainIndex++] = key;
            }

            bundle.putStringArray(STATE_KEY_KEYCHAIN, keychain);
        }

        @Override
        public void onRestoreInstanceState(Bundle bundle) {
            String[] keychain = bundle.getStringArray(STATE_KEY_KEYCHAIN);

            if (keychain != null) {
                for (String key : keychain) {
                    Long value = bundle.getLong(key, 0L);
                    map.put(key, value);
                }
            }
        }
    }


}
