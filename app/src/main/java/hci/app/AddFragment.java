package hci.app;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {


    public AddFragment() {
        // Required empty public constructor
    }

    private Button submit_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add, container, false);

        submit_button = v.findViewById(R.id.btn_createEvent);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        // TODO: addFragment XML, adding implementation

        // Assume view was created from a marker on the map and has a bundle with location information
        // TODO: Call createEventAtLocation with values inserted in addFragment fields

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEventAtLocation();
            }
        });
    }

    public void createEventAtLocation() {

        JSONObject eventJSON = new JSONObject();

        Double latitude = this.getArguments().getDouble("latitude");
        Double longitude = this.getArguments().getDouble("longitude");
        try {
            eventJSON.put("latitude", latitude);
            eventJSON.put("longitude", longitude);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }

        Database.storeEvent(eventJSON);
    }
}
