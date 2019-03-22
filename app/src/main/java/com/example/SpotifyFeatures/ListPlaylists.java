package com.example.SpotifyFeatures;

import android.content.Context;
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

public class ListPlaylists extends Fragment {
    private ProgressBar loading;
    private RecyclerView playlists;
    private SpotifyViewModel mViewModel;

    public ListPlaylists() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list_playlists, container, false);
        loading = root.findViewById(R.id.spinner);
        playlists = root.findViewById(R.id.playlists);

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
                    playlists.setVisibility(View.INVISIBLE);
                } else {
                    loading.setVisibility(View.INVISIBLE);
                    playlists.setVisibility(View.VISIBLE);
                }
            }
        });
        PlaylistAdapter adapter = new PlaylistAdapter(this, mViewModel.playlists());
        playlists.setAdapter(adapter);
        playlists.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel.fetchPlaylists();
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
