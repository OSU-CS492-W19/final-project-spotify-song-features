package com.example.SpotifyFeatures;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    private ArrayList<JSONObject> data;

    class SongHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView artist;
        private TextView album;
        private features
        public SongHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            name = v.findViewById(R.id.artist);
            name = v.findViewById(R.id.album_name);
        }
        public void bind(JSONObject data) {
            String name_val = data.optString("name");
//            artist.setText(data.optJSONArray("artists").optJSONObject(0).optString("name"));
            name.setText(name_val);
        }
    }

    public SongAdapter(Fragment owner, LiveData<ArrayList<JSONObject>> input) {
        input.observe(owner, new Observer<ArrayList<JSONObject>>() {
            @Override
            public void onChanged(ArrayList<JSONObject> newData) {
                data = newData;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public SongAdapter.SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.playlist_holder, parent, false);
        return new SongAdapter.SongHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongAdapter.SongHolder holder, int position) {
        holder.bind(data.get(position));
    }
}
