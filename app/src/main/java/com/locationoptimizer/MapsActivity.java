package com.locationoptimizer;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //TODO 1.1 - add these instance variables
    private GoogleMap mMap;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng mbs = new LatLng(1.2, 103);

        // TODO 1.2 modify the following line so that the marker is stored in the instance variable
        marker = mMap.addMarker(new MarkerOptions().position(mbs).title("Hotel Marker"));

//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mbs));

    }

    public void whenClick(View view){

        Geocoder geocoder;
        List<Address> addresses;

        geocoder = new Geocoder(this, Locale.getDefault());

        try{

            //loop through adding markers. from arraylist



            //TODO 2.1 - query the addresses of changi airport using the getFromLocationName method of geocoder
            addresses = geocoder.getFromLocationName("Sentosa", 1);
            //TODO 2.2 - get the latitude and longitude from the 0-th elements of the List object
            double latitude = addresses.get(0).getLatitude();
            double longitude = addresses.get(0).getLongitude();


            //TODO 2.3 - set up a new LatLng object
            LatLng ct = new LatLng(latitude, longitude);

            //TODO 2.4 - use the setPosition method of the marker object to move the marker
            marker.setPosition(ct);
            //TODO 2.5 - use the moveCamera method of mMap to move the view
            mMap.moveCamera(CameraUpdateFactory.newLatLng(ct));
            //TODO 2.6 - similarly, set an appropriate zoom level
            float zoomlevel = 15;
            mMap.moveCamera(CameraUpdateFactory.zoomTo(zoomlevel));

            marker.setTitle("Marina Bay Sands");

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
