package com.example.SpotifyFeatures;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class PromptForLogin extends Fragment {
    private static final String CLIENT_ID = "441cf4c355dd4b16ad08fb63bb9dc0aa";
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "http://evan-brass.github.io/spotify-app";

    private Button mAuthBtn;

    private SpotifyViewModel mViewModel;

    public PromptForLogin() {
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
        View ret = inflater.inflate(R.layout.fragment_prompt_for_login, container, false);
        mAuthBtn = ret.findViewById(R.id.authorize_btn);
        mAuthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationRequest.Builder builder =
                        new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

                builder.setScopes(new String[]{"playlist-read-private"});
                AuthenticationRequest request = builder.build();

                AuthenticationClient.openLoginActivity(getActivity(), REQUEST_CODE, request);
            }
        });
        return ret;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mViewModel = ViewModelProviders.of(getActivity()).get(SpotifyViewModel.class);

        final Fragment self = this;
        mViewModel.token().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String newVal) {
                if (!newVal.equals("")) {
                    // Likely A request fell through meaning that the token expired - Get a new one
                    NavHostFragment.findNavController(self).navigate(R.id.listPlaylists);
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
