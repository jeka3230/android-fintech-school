package ru.tinkoff.school.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import ru.tinkoff.school.model.Artist;
import ru.tinkoff.school.model.ArtistsResponse;

public class ArtistManager {

    private static final String DEFAULT_GRAPH_NAME = "artists.json";
    private static final String UTF8 = "UTF-8";

    @SuppressWarnings("TryFinallyCanBeTryWithResources")
    public static List<Artist> getArtists(Context context) {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(context.getAssets().open(DEFAULT_GRAPH_NAME), Charset.forName(UTF8)));
            Gson gson = new Gson();
            ArtistsResponse graph = null;
            try {
                graph = gson.fromJson(reader, ArtistsResponse.class);
            } finally {
                reader.close();
            }
            if (graph != null && graph.getData() != null) {
                return graph.getData();
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
