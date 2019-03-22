package com.example.SpotifyFeatures;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.util.ArrayList;

public class ListSongs extends Fragment {
    private ProgressBar loading;
    private RecyclerView songs;
    private SpotifyViewModel mViewModel;
    private String href;
    private ListSongsArgs args;

    public ListSongs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list_songs, container, false);
        loading = root.findViewById(R.id.spinner);
        songs = root.findViewById(R.id.songs);

        args = ListSongsArgs.fromBundle(getArguments());

        mViewModel = ViewModelProviders.of(getActivity()).get(SpotifyViewModel.class);
        final NavController navigationController = NavHostFragment.findNavController(this);
        mViewModel.token().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String newVal) {
                if (newVal.equals("")) {
                    // Likely A request fell through meaning that the token expired - Get a new one
                    navigationController.navigate(R.id.promptForLogin);
                }
            }
        });
        mViewModel.playlists().observe(this, new Observer<ArrayList<JSONObject>>() {
            @Override
            public void onChanged(ArrayList<JSONObject> newPlaylists) {
                if (newPlaylists.size() == 0) {
                    // Haven't received the playlists (or the person might have no playlists)
                    loading.setVisibility(View.VISIBLE);
                    songs.setVisibility(View.INVISIBLE);
                } else {
                    loading.setVisibility(View.INVISIBLE);
                    songs.setVisibility(View.VISIBLE);
                }
            }
        });
        SongAdapter adapter = new SongAdapter(this, mViewModel.songs());
        songs.setAdapter(adapter);
        songs.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.fetchSongs(args.getPlaylistUrl(), args.getPlaylistSize());
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mViewModel.clearSongs();
    }

}
