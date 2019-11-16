package hci.app;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.*;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


/**
 * A simple {@link Fragment} subclass.
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

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
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
            }
        });
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // Reload fragment if permission was granted
                if ( grantResults[0] != PackageManager.PERMISSION_GRANTED ) {
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }
            }
        }
    }


    // Save temporary marker as variable
    private Marker marker;


    public void setMapLongClick(final GoogleMap mMap) {
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override

            public void onMapLongClick(LatLng latLng) {
                if (marker != null) {
                    marker.remove();
                }

                marker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .title("Create new event here?"));
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

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
    }

}
