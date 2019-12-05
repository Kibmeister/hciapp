package hci.app;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.*;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hci.app.EventDescription.EventDescripionFragment;


/**
 * MapFragment class: Responsible for displaying the map, events, location
 * and provides navigation to several other fragments.
 *
 * Much of the code here depends on Googles Map API, and has been found here:
 * https://developers.google.com/maps/documentation/android-sdk/
 *
 * @author Frederik Andersen
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {


    public MapFragment() {
        // Required empty public constructor
    }

    private SupportMapFragment map;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 42;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng currentLocation;

    DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        System.out.println("Drawing map");

        map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        return v;
    }

    @Override
    public void onMapReady(final GoogleMap mMap) {
        System.out.println("onMapReady()");

        requestLocationPermissions(mMap);

        setMapLongClick(mMap);
        mMap.setOnInfoWindowClickListener(this);

        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
                }
            }
        });

        loadEvents(mMap);
    }

    // Request the user for location permissions
    private void requestLocationPermissions(GoogleMap mMap) {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        // Request the permission if it's not granted
        else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    // Check location permission request callback to see if permissions were granted
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // Reload fragment if permission was granted
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }
            }
        }
    }


    /**
     * Code partly found on https://stackoverflow.com/a/49739813
     * <p>
     * Adds all markers from the database to the map overview.
     *
     * @param mMap: Map variable for adding event pins to the map
     */
    private void loadEvents(final GoogleMap mMap) {
        System.out.println();
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data != null) {
                        Marker marker;

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(
                                        Double.valueOf(data.child("latitude").getValue().toString()),
                                        Double.valueOf(data.child("longitude").getValue().toString())))
                                .title(data.child("eventHeader").getValue().toString())
                                .icon(BitmapDescriptorFactory.fromResource(
                                        getResources().getIdentifier(
                                                data.child("category").getValue().toString(),
                                                "drawable",
                                                getActivity().getPackageName())
                                ));
                        marker = mMap.addMarker(markerOptions);
                        marker.setTag(data.getKey());
                        System.out.println(markerOptions.getIcon());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // Save temporary marker as variable
    private Marker marker;


    // Set map long click to enable the user to place a pin where he wants to add an event
    public void setMapLongClick(final GoogleMap mMap) {
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override

            public void onMapLongClick(LatLng latLng) {
                if (marker != null) {
                    marker.remove();
                }

                marker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .snippet("Custom Marker")
                        .title("Create new event here?"));
                marker.setTag("Custom Marker");
            }
        });
    }

    // Adds onClickListener on the info windows to each marker.
    @Override
    public void onInfoWindowClick(Marker marker) {

        if (marker.getTag().equals("Custom Marker")) {
            // Custom marker is pressed, send to Add Event page

            // Open add fragment and pass the location as parameter
            // Execution inspired partly by https://stackoverflow.com/a/40949016

            LatLng location = marker.getPosition();
            Bundle bundle = new Bundle();
            bundle.putDouble("latitude", location.latitude);
            bundle.putDouble("longitude", location.longitude);
            AddFragment addFragment = new AddFragment();
            addFragment.setArguments(bundle);

            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, addFragment)
                        .commit();
            } else {
                Toast.makeText(getContext(), "An error occured", Toast.LENGTH_LONG).show();
            }
        } else {
            // Event marker is pressed, send to event description page
            // Save the event id in bundle and start the EventDescriptionFragment
            Bundle bundle = new Bundle();
            bundle.putString("eventKey", marker.getTag().toString());
            EventDescripionFragment fragment = new EventDescripionFragment();
            fragment.setArguments(bundle);

            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .commit();
            }
        }
    }
}
