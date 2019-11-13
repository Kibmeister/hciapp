package hci.app;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


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
        mMap.setOnMarkerClickListener(this);
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
    public boolean onMarkerClick(final Marker marker) {
        Toast.makeText(getContext(), "Marker clicked", Toast.LENGTH_LONG).show();
        return false;
    }
}
