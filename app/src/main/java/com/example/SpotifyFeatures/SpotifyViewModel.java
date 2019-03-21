package com.example.SpotifyFeatures;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SpotifyViewModel extends ViewModel {
    private MutableLiveData<String> token;
    public MutableLiveData<String> token() {
        if (token == null) {
            token = new MutableLiveData<>();
            token.setValue("");
        }
        return token;
    }

    // Create a LiveData with a String
    private MutableLiveData<String> loadingState;
    private MutableLiveData<ArrayList<JsonObject>> playlists;
    public MutableLiveData<String> loadingState() {
        if (loadingState == null) {
            loadingState = new MutableLiveData<>();
            loadingState.setValue("Unloaded");
        }
        return loadingState;
    }
    public MutableLiveData<ArrayList<JsonObject>> playlists() {
        if (playlists == null) {
            playlists = new MutableLiveData<>();
            playlists.setValue(new ArrayList<JsonObject>());
        }
        return playlists;
    }
}
