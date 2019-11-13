package hci.app;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {


    public MapFragment() {
        // Required empty public constructor
    }

    private SupportMapFragment map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        System.out.println("Map ready, placing marker");

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        setMapLongClick(mMap);
        mMap.setOnInfoWindowClickListener(this);
    }

    // Save temporary marker as variable
    private Marker marker;

    public void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override

            public void onMapLongClick(LatLng latLng) {
                if (marker != null) {
                    marker.remove();
                }

                marker = map.addMarker(new MarkerOptions().position(latLng)
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
