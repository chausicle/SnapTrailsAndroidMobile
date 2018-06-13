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

    public MapsFragment() {
        Log.d(TAG, "MapsFragment: start");
        Log.d(TAG, "MapsFragment: end");
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        Bundle bundle = getArguments();

        if (bundle != null) {
            latitude = bundle.getFloat("latitude");
            longitude = bundle.getFloat("longitude");
            location = bundle.getString("location");
        }

        Log.d(TAG, "onCreateView: end");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: start");
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
        Log.d(TAG, "onViewCreated: end");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: start");
        System.out.println("onMapReady: start");
        map = googleMap;

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title(location);
        map.addMarker(options);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        System.out.println("onMapReady: end");
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: start");
        System.out.println("onAttach: start");
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        Log.d(TAG, "onAttach: end");
    }

    @Override
    public void onDetach() {
        System.out.println("onDetach: start");
        super.onDetach();
        mListener = null;
        System.out.println("onDetach: end");
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
