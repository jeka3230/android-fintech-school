package ru.tinkoff.school.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.tinkoff.school.R;
import ru.tinkoff.school.model.Artist;
import ru.tinkoff.school.utils.FragmentHelper;

//  создать и добавить новый вложенный в детали фрагмент со списком песен исполнителя
public class ArtistDetailsFragment extends Fragment {

    public static final String TAG = "ArtistDetailsFragment";
    private static final String EXTRA_ARTIST = "artist";

    private Toolbar toolbar;
    private ImageView imageView;
    private TextView textBio;
    private FloatingActionButton fab;

    private Artist artist;

    public static ArtistDetailsFragment newInstance(Artist artist) {
        ArtistDetailsFragment fragment = new ArtistDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_ARTIST, artist);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_details, container, false);
        artist = (Artist) getArguments().getSerializable(EXTRA_ARTIST);
        initViews(view);
        fillViewsWithContent();
        return view;
    }

    private void initViews(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        imageView = (ImageView) view.findViewById(R.id.image_view);
        textBio = (TextView) view.findViewById(R.id.text_bio);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        FragmentHelper.addFragment(getActivity().getSupportFragmentManager(),
                R.id.songsInfo, SongsFragment.newInstance(artist.getSongs()),
                SongsFragment.TAG, false);
    }

    private void fillViewsWithContent() {
        toolbar.setTitle(artist.getArtist());
        textBio.setText(artist.getBio());
        fab.setImageResource(artist.isFaves() ? R.drawable.ic_fave : R.drawable.ic_not_fave);
        Picasso.with(getContext()).load(artist.getUrl()).into(imageView);
    }
}
