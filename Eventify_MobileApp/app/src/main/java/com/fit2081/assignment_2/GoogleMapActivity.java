package com.fit2081.assignment_2;

import static android.widget.Toast.LENGTH_LONG;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.fit2081.assignment_2.databinding.ActivityGoogleMapBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapBinding binding;
    // class variable to hold country name
    private String countryToFocus;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // set class variable using bundle data
        countryToFocus = getIntent().getExtras().getString("location", "N/A");
        geocoder = new Geocoder(this, Locale.getDefault());

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

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // place latitude-longitude values in the order specified
        LatLng monashMsia = new LatLng(3.064881562017015, 101.60132883299701);

        // adds a marker to the specified latitude-longitude
        mMap.addMarker(new MarkerOptions().position(monashMsia).title("Monash University Malaysia"));

        // use moveCamera method to move current map viewing angle to Malaysia campus
        mMap.moveCamera(CameraUpdateFactory.newLatLng(monashMsia));

        findCountryMoveCamera();
        googleMap.setOnMapClickListener(point -> {
            String msg;
            List<Address> addresses = new ArrayList<>();
            try {
                addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses.size() == 0) {
                msg = "No country";
                Toast.makeText(GoogleMapActivity.this, msg, LENGTH_LONG).show();
            }
            else {
                android.location.Address address = addresses.get(0);
                msg = "The selected country is " + address.getCountryName();
                Toast.makeText(GoogleMapActivity.this, msg, LENGTH_LONG).show();

            }
        });


    }
    private void findCountryMoveCamera(){
        // initialise Geocode to search location using String
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        // getFromLocationName method works for API 33 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            /**
             * countryToFocus: String value, any string we want to search
             * maxResults: how many results to return if search was successful
             * successCallback method: if results are found, this method will be executed
             *                          runs in a background thread
             */
            geocoder.getFromLocationName(countryToFocus, 1, addresses ->{
                // if there are results, this condition would return true
                if (!addresses.isEmpty()){
                    // run on UI thread as the user interface will update once set map location
                    runOnUiThread(() -> {
                        // define new LatLng variable using the first address from list of addresses
                        LatLng newAddressLocation = new LatLng(
                                addresses.get(0).getLatitude(),
                                addresses.get(0).getLongitude()
                        );
                        // repositions the camera according to newAddressLocation
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(newAddressLocation));

                        // just for reference add a new Marker
                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(newAddressLocation)
                                        .title(countryToFocus)
                        );
                        // set zoom level to 8.5f or any number between range of 2.0 to 21.0
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                    });

                    }else {
                    runOnUiThread(() -> Toast.makeText(this, "Category address not found", LENGTH_LONG).show());
                }
            });
        }
    }
}