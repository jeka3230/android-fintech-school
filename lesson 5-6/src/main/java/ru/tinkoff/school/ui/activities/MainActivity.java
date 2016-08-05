package ru.tinkoff.school.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.tinkoff.school.ui.fragments.ArtistListFragment;
import ru.tinkoff.school.R;
import ru.tinkoff.school.utils.FragmentHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentHelper.addFragment(
                getSupportFragmentManager(),
                R.id.container_list, ArtistListFragment.newInstance(),
                ArtistListFragment.TAG, false
        );
    }
}
