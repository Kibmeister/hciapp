package hci.app;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * AddFragment class:
 * <p>
 * Responsible for adding data for newly created events to the database
 * Can only be called from pressing custom markers on the map.
 * Displays a form where the user can input data, and should verify user input
 * before sending data to the database.
 *
 * @author Frederik Andersen
 * @author Kasper Borgbjerg
 */

// TODO: Verify FORM data


public class AddFragment extends Fragment {
    // get a reference to the component

    private Button mDateDecoratedButton, mDateEndDecoratedButton;

    private SimpleDateFormat mSimpleDateFormat;
    private Calendar mCalendar, mCalendarEnd;
    private TextView mDate, mDateEnd;
    private String replyDateEnd, replyDateStart;

    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private boolean selectingStartDate, selectingEndDate;
    private HashMap<String, Integer> fromDate, toDate;

    public AddFragment() {
        // Required empty public constructor
    }


    View v;

    EditText event_description;
    HorizontalNumberPicker attendeeLimit;
    private Button submit_button;

    DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_add, container, false);

        submit_button = v.findViewById(R.id.btn_createEvent);
        mDateDecoratedButton = v.findViewById(R.id.btn_endTime);
        mDateEndDecoratedButton = v.findViewById(R.id.btn_startTime);

        mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy k:mm ", Locale.getDefault());

        mDate = v.findViewById(R.id.contentMain);
        mDateEnd = v.findViewById(R.id.contentMain2);

        mDateDecoratedButton.setOnClickListener(textListener);
        mDateEndDecoratedButton.setOnClickListener(textListener);

        this.fromDate = new HashMap<>();
        this.toDate = new HashMap<>();

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        // Assume view was created from a marker on the map and has a bundle with location information
        event_description = v.findViewById(R.id.edit_txt_eventdescription);
        attendeeLimit = v.findViewById(R.id.np_channel_nr);
        attendeeLimit.setMin(1);
        attendeeLimit.setMax(8);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEventAtLocation();
            }
        });
    }
    /**
     * Responsible for sending the form information to the database.
     */
    private void createEventAtLocation() {

        Map<String, String> eventMap = new HashMap<>();
        Double latitude, longitude;

        // prevent getArguments() NullPointerException
        if (this.getArguments() != null) {
            latitude = this.getArguments().getDouble("latitude");
            longitude = this.getArguments().getDouble("longitude");
        } else {
            Toast.makeText(getContext(), "Error occured while submitting event", Toast.LENGTH_LONG).show();
            return;
        }

        if (formDataIsValid()) {

            // Add event information to the Map object and send it to the database class
            eventMap.put("latitude", latitude.toString());
            eventMap.put("longitude", longitude.toString());
            eventMap.put("hostId", Profile.getCurrentProfile().getId());
            // eventMap.put();
            eventMap.put("attendeeLimit", String.valueOf(attendeeLimit.getValue()));
            eventMap.put("eventDescription", event_description.getText().toString());
            eventMap.put("eventHeader", "Event Header");

            String eventKey = eventsRef.push().getKey();
            DatabaseReference event = eventsRef.child(eventKey);
            event.setValue(eventMap);

            // Add date data:
            DatabaseReference fromDateRef = event.child("fromDate");
            fromDateRef.setValue(this.fromDate);

            DatabaseReference toDateRef = event.child("toDate");
            toDateRef.setValue(this.toDate);

            // Add reference to event in host reference
            FirebaseDatabase.getInstance().getReference(
                    "users/" +
                    Profile.getCurrentProfile().getId() +
                    "/hostedEvents/" +
                    eventKey)
                        .setValue(eventKey);
        } else {
            System.out.println("Please fill in the reuired fields!");
        }
    }

    // TODO: Verify the form input before submitting to firebase
    // Check dates, attendees, text fields.
    private boolean formDataIsValid() {
        if (attendeeLimit.getMin() > attendeeLimit.getValue() && attendeeLimit.getMax() < attendeeLimit.getValue()) {
            Toast.makeText(getContext(), "Invalid number of attendees", Toast.LENGTH_LONG).show();
            return false;
        }





        return true;
    }
    /**
     * DatePicker Listeners
     */

    /* Define the onClickListener, and start the DatePickerDialog with users current time */
    private final View.OnClickListener textListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_startTime) {
                //calender start time
                mCalendar = Calendar.getInstance();
                startDatePickerDialog = new DatePickerDialog(getActivity(), mDateDataSet, mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                startDatePickerDialog.show();
                selectingStartDate = true;
            } else if (v.getId() == R.id.btn_endTime) {
                //calender end time
                mCalendarEnd = Calendar.getInstance();
                endDatePickerDialog = new DatePickerDialog(getActivity(), mDateDataSet, mCalendarEnd.get(Calendar.YEAR),
                        mCalendarEnd.get(Calendar.MONTH), mCalendarEnd.get(Calendar.DAY_OF_MONTH));
                endDatePickerDialog.show();
                selectingEndDate = true;
            }
        }
    };

    /* After user decided on a date, store those in our calendar variable and then start the TimePickerDialog immediately */
    private final DatePickerDialog.OnDateSetListener mDateDataSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if (selectingStartDate) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fromDate.put("year", year);
                fromDate.put("month", monthOfYear);
                fromDate.put("day", dayOfMonth);
                startTimePickerDialog = new TimePickerDialog(getActivity(), mTimeDataSet, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
                startTimePickerDialog.show();
            } else if (selectingEndDate) {
                mCalendarEnd.set(Calendar.YEAR, year);
                mCalendarEnd.set(Calendar.MONTH, monthOfYear);
                mCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                toDate.put("year", year);
                toDate.put("month", monthOfYear);
                toDate.put("day", dayOfMonth);
                endTimePickerDialog = new TimePickerDialog(getActivity(), mTimeDataSet, mCalendarEnd.get(Calendar.HOUR_OF_DAY), mCalendarEnd.get(Calendar.MINUTE), true);
                endTimePickerDialog.show();
            }
        }
    };

    /* After user decided on a time, save them into our calendar instance, and now parse what our calendar has into the TextView */
    private final TimePickerDialog.OnTimeSetListener mTimeDataSet = new TimePickerDialog.OnTimeSetListener() {
        @Override

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (selectingStartDate) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                fromDate.put("hour", hourOfDay);
                fromDate.put("minute", minute);
                mDate.setText(mSimpleDateFormat.format(mCalendar.getTime()));
                replyDateStart = mSimpleDateFormat.format(mCalendar.getTime());
                selectingStartDate = false;
            } else if (selectingEndDate) {
                mCalendarEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendarEnd.set(Calendar.MINUTE, minute);
                toDate.put("hour", hourOfDay);
                toDate.put("minute", minute);
                mDateEnd.setText(mSimpleDateFormat.format(mCalendarEnd.getTime()));
                replyDateEnd = mSimpleDateFormat.format(mCalendarEnd.getTime());
                selectingEndDate = false;
            }

        }
    };
}