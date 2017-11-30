package com.locationoptimizer;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    MapView mapView;
    GoogleMap googleMap;
    Geocoder geocoder;
    ArrayList<LatLng> latLngList = new ArrayList<>();
    ArrayList<String> locations;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        locations = LocationAdapter.getSelectedLocations();

        // Get Lat Lng of locations
        for (String i : locations) {
            Log.i("Locations Map", i);
            geocoder = new Geocoder(getContext(), Locale.ENGLISH);
            try {
                List<Address> addresses = geocoder.getFromLocationName(i, 5);
                Address location = addresses.get(0);
                LatLng latLngAdd = new LatLng(location.getLatitude(), location.getLongitude());
                latLngList.add(latLngAdd);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        View rootview = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) rootview.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        if (mapView != null) {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    googleMap = map;

                    for(int i = 0; i < locations.size(); i++) {
                        googleMap.addMarker(
                                new MarkerOptions()
                                .position(latLngList.get(i))
                                .title(locations.get(i)));


                    }

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(1.290270, 103.851959)).zoom(10).build();

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
            });
        }






        return rootview;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
