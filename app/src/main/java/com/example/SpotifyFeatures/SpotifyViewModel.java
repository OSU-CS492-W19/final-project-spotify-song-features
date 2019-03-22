package com.example.SpotifyFeatures;

import android.app.Application;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import static java.security.AccessController.getContext;

public class SpotifyViewModel extends AndroidViewModel {
    private RequestQueue queue;

    SpotifyViewModel(Application app) {
        super(app);
    }
    private MutableLiveData<String> token = new MutableLiveData<>();
    public MutableLiveData<String> token() {
        return token;
    }
    public void setToken(String newVal) {
        token.setValue(newVal);
    }

    // Create a LiveData with a String
    private MutableLiveData<ArrayList<JSONObject>> playlists;
    public MutableLiveData<ArrayList<JSONObject>> playlists() {
        if (playlists == null) {
            playlists = new MutableLiveData<>();
        }
        return playlists;
    }
    public void fetchPlaylists() {
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplication());
        }

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://api.spotify.com/v1/me/playlists",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("items");
                            ArrayList<JSONObject> ret = new ArrayList<>();
                            int i = 0;
                            while (items.optJSONObject(i) != null) {
                                ret.add(items.getJSONObject(i++));
                            }
                            playlists.setValue(ret);

                        } catch(JSONException e) {
                            // Pretend that all errors mean a problem with the token:
                            token.setValue("");
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Pretend that all errors mean a problem with the token:
                        token.setValue("");
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token.getValue());

                return params;
            }
        };

        // Add JsonObjectRequest to the RequestQueue
        queue.add(jsonObjectRequest);
    }

    // Create a LiveData with a String
    private MutableLiveData<ArrayList<JSONObject>> songs;
    public MutableLiveData<ArrayList<JSONObject>> songs() {
        if (songs == null) {
            songs = new MutableLiveData<>();
            songs.setValue(new ArrayList<JSONObject>());
        }
        return songs;
    }
    public void clearSongs() {
        songs.setValue(new ArrayList<JSONObject>());
    }
    public void fetchSongs(final String url, final int count) {
        if (url.equals("null")) {
            return;
        }
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplication());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url + "",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<JSONObject> ret = new ArrayList<>();

                            JSONArray tracks = response.getJSONArray("items");
                            int i = 0;
                            while (tracks.optJSONObject(i) != null) {
                                ret.add(tracks.getJSONObject(i++).getJSONObject("track"));
                            }
                            songs().setValue(ret);
                            if (response.optString("next") != null) {
                                fetchSongs(response.getString("next"), count);
                            }

                        } catch(JSONException e) {
                            // Pretend that all errors mean a problem with the token:
                            token.setValue("");
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Pretend that all errors mean a problem with the token:
                        token.setValue("");
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token.getValue());

                return params;
            }
        };

        // Add JsonObjectRequest to the RequestQueue
        queue.add(jsonObjectRequest);
    }
}
