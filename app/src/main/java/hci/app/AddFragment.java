package hci.app;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.SyncFailedException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * AddFragment class:
 *
 * Responsible for adding data for newly created events to the database
 * Can only be called from pressing custom markers on the map.
 * Displays a form where the user can input data, and should verify user input
 * before sending data to the database.
 *
 * @author Frederik Andersen
 * @author Kasper Borgbjerg
 */

// TODO: Verify FORM data


public class AddFragment extends Fragment implements View.OnClickListener {
    // get a reference to the component



    public AddFragment() {
        // Required empty public constructor
    }


    View v;

    EditText event_description;
    HorizontalNumberPicker attendeeLimit;
    private Button submit_button, buttonTimeStart, buttonTimeEnd;

    DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_add, container, false);

        submit_button = v.findViewById(R.id.btn_createEvent);
        buttonTimeStart = v.findViewById(R.id.btn_timeStart);
        buttonTimeEnd = v.findViewById(R.id.btn_timeEnd);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        // Assume view was created from a marker on the map and has a bundle with location information

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEventAtLocation();
            }
        });

        event_description = v.findViewById(R.id.edit_txt_eventdescription);
        attendeeLimit = v.findViewById(R.id.np_channel_nr);
        attendeeLimit.setMin(1);
        attendeeLimit.setMax(8);

       buttonTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("tada");
                Navigation.findNavController(v).navigate(R.id.action_addFragment_to_dateAndTimePicker2);
            }
        });

        //buttonTimeStart.setOnClickListener(this);

    }


    /**
     * Responsible for sending the form information to the database.
     */
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
        eventMap.put("hostId", Profile.getCurrentProfile().getId());
        List<String> attendeeIds = new ArrayList<>();
        // eventMap.put("attendeeIds", attendeeIds.toString()); // TODO: Cant push lists to database, needs workaround
        eventMap.put("attendeeLimit", attendeeLimit.getValue());
        eventMap.put("eventDescription", event_description.getText());
        eventMap.put("eventHeader", "Event Header");
        eventMap.put("timestamp", 1576188000);

        System.out.println(eventMap);

        String eventKey = eventsRef.push().getKey();
        eventsRef.child(eventKey).setValue(eventMap);

    }

    @Override
    public void onClick(View view) {
       /*if (v.getId() == R.id.btn_timeStart) {
            System.out.println("Time start button");
            Navigation.findNavController(v).navigate(R.id.action_addFragment_to_dateAndTimePicker2);
        }*/
    }
}
