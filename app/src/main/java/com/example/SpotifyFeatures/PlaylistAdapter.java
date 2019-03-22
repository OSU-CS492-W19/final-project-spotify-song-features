package com.example.SpotifyFeatures;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.Holder> {
    ArrayList<JSONObject> data;
    class Holder extends RecyclerView.ViewHolder {
        private TextView name;
        public Holder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
        }
        public void bind(JSONObject data) {
            name.setText(data.optString("name"));
        }
    }
    public PlaylistAdapter(Fragment owner, LiveData<ArrayList<JSONObject>> input) {
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
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.playlist_holder, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(data.get(position));
    }
}
