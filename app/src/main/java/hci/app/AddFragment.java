package hci.app;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {
    // get a reference to the component



    public AddFragment() {
        // Required empty public constructor
    }



    private Button submit_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add, container, false);

        submit_button = v.findViewById(R.id.btn_createEvent);

        final HorizontalNumberPicker np_channel_nr = v.findViewById(R.id.np_channel_nr);
        // use value in your code
        final int nr = np_channel_nr.getValue();

        System.out.println("max value input" + nr);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        // TODO: addFragment XML, adding implementation

        // Assume view was created from a marker on the map and has a bundle with location information

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEventAtLocation();
            }
        });
    }

    private void createEventAtLocation() {

        Map<String, Object> eventMap = new HashMap<>();
        Double latitude, longitude;

        // prevent getArguments() NullPointerException
        if (this.getArguments() != null) {
            latitude = this.getArguments().getDouble("latitude");
            longitude = this.getArguments().getDouble("longitude");
        } else {
            Toast.makeText(getContext(), "Error occured while submitting event", Toast.LENGTH_LONG).show();
            return;
        }

        // Add event information to the Map object and send it to the database class
        eventMap.put("latitude", latitude);
        eventMap.put("longitude", longitude);

        Database.storeEvent(eventMap);
    }
}
