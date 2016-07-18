package ru.tinkoff.cake.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.tinkoff.cake.R;

public class ModelFragment extends Fragment {

    public static final int[] MODELS = {
            R.drawable.player_model_1,
            R.drawable.player_model_2,
            R.drawable.player_model_3,
            R.drawable.player_model_4,
    };

    private static final String ARGS_POSITION = "position";

    public static ModelFragment newInstance(int position) {
        ModelFragment fragment = new ModelFragment();
        Bundle b = new Bundle();
        b.putInt(ARGS_POSITION, position);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_model, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int position = getArguments().getInt(ARGS_POSITION);

        ImageView imageView = (ImageView) view.findViewById(R.id.choose_model_img);
        imageView.setImageResource(MODELS[position]);
    }

}
