package ru.tinkoff.school.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.tinkoff.school.R;
import ru.tinkoff.school.model.Artist;
import ru.tinkoff.school.ui.adapters.ArtistsAdapter;
import ru.tinkoff.school.utils.ArtistManager;
import ru.tinkoff.school.utils.FragmentHelper;

public class ArtistListFragment extends Fragment {

    public static final String TAG = "ArtistListFragment";

    private List<Artist> artists;

    public static ArtistListFragment newInstance() {
        return new ArtistListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_list, container, false);
        artists = ArtistManager.getArtists(getContext());
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ArtistsAdapter adapter = new ArtistsAdapter(artists, new ArtistsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                itemClick(position);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void itemClick(int position) {
        getActivity().getSupportFragmentManager().popBackStack();
        FragmentHelper.addFragment(
                getActivity().getSupportFragmentManager(),
                R.id.container_details, ArtistDetailsFragment.newInstance(artists.get(position)),
                ArtistDetailsFragment.TAG, true
        );
    }
}
