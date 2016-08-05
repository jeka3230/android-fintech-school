package ru.tinkoff.school.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.tinkoff.school.R;
import ru.tinkoff.school.model.Artist;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {

    private List<Artist> dataSet;
    private OnItemClickListener listener;

    public ArtistsAdapter(List<Artist> dataSet, OnItemClickListener listener) {
        this.dataSet = dataSet == null ? new ArrayList<Artist>() : dataSet;
        this.listener = listener;
    }

    @Override
    public ArtistsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Artist artist = dataSet.get(position);
        holder.title.setText(artist.getArtist());
        holder.subtitle.setText(artist.getGenre());
        holder.bind(listener, position);
        //  вывести информацию об исполнителе в элемент списка
        //  по клику на элемент списка должна показываться подробная информация об исполнителе
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    //  реализовать паттерн ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView subtitle;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
        }

        public void bind(final OnItemClickListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view, position);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }
}