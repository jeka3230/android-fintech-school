package ru.tinkoff.school.utils;

import android.annotation.SuppressLint;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ru.tinkoff.school.R;

public class FragmentHelper {

    @SuppressLint("CommitTransaction")
    public static void addFragment(FragmentManager fragmentManager, @IdRes int container, Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction().replace(container, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }
}
