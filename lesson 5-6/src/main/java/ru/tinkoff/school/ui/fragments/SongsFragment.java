package ru.tinkoff.school.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.tinkoff.school.R;

/**
 * Created by evgeny on 03.08.16.
 */

public class SongsFragment extends Fragment {

    private static String SONGS = "songs";
    public static String TAG = "SongsFragment";

    public static Fragment newInstance(List<String> songs) {
        SongsFragment songsFragment = new SongsFragment();
        Bundle bundle = new Bundle();
        String songsString = listToString(songs);
        bundle.putString(SONGS, songsString);
        songsFragment.setArguments(bundle);
        return songsFragment;
    }

    private static String listToString(List<String> songs) {
        String songsString = "";
        int len = songs.size() - 1;
        for (int i=0; i < len; i++) {
            songsString += songs.get(i) + ", ";
        }
        songsString += songs.get(len);
        return songsString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        String stringSongs = getArguments().getString(SONGS);
        TextView songsView = (TextView) view.findViewById(R.id.songs);
        songsView.setText(stringSongs);
        return view;
    }
}
