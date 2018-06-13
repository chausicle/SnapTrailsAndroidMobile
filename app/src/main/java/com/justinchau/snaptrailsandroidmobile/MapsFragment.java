package com.justinchau.snaptrailsandroidmobile;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapsFragment";
    private GoogleMap map;
    private OnFragmentInteractionListener mListener;
    private double latitude;
    private double longitude;
    private String location;
    private Boolean getAll;

    public MapsFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.getString("getAll") == "getAll") {
                getAll = true;
//                getPosts();
            } else if (bundle.getString("getOne") == "getOne") {
                location = bundle.getString("location");
                latitude = bundle.getDouble("latitude");
                longitude = bundle.getDouble("longitude");
                getAll = false;
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (getAll) {
            String url = "https://hidden-thicket-31298.herokuapp.com/posts";
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request
                    .Builder()
                    .url(url)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i(TAG, "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String postJson = null;

                            try {
                                postJson = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Post[] posts = new Gson().fromJson(postJson, Post[].class);

                            LatLng latLng;
                            for (Post post : posts) {
                                latLng = new LatLng(post.getLatitude(), post.getLongitude());
                                MarkerOptions options = new MarkerOptions();
                                options.position(latLng).title(post.getLocation());
                                map.addMarker(options);
                            }
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.7749, -122.4194), 3));
                        }
                    });
                }
            });
        } else {
            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions options = new MarkerOptions();
            options.position(latLng).title(location);
            map.addMarker(options);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
