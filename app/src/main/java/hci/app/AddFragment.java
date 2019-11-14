package hci.app;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {


    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // TODO: addFragment XML, adding implementation

        // Assume view was created from a marker on the map and has a bundle with location information
        if (this.getArguments() != null) {
            createEventAtLocation(this.getArguments().getDouble("latitude"), this.getArguments().getDouble("longitude"));
        }
    }

    public void createEventAtLocation(double latitude, double longitude) {
        System.out.println("Creating new event at: " + latitude + ", " + longitude);
    }
}
