package hci.app;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
 * Date and Time picker:spawns a date picker followed up by a time picker
 * that sends the data back to the previous fragment and stores it as a variable.
 * Code inspired from source:
 * GitHub :https://github.com/Kiarasht/Android-Templates/tree/master/Templates/DatePickerDialog
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
    private Spinner categorySpinner;

    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private boolean selectingStartDate, selectingEndDate;

    public AddFragment() {


    }
    View v;

    EditText event_description;
    HorizontalNumberPicker attendeeLimit;
    private Button submit_button;

    DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");
    DatabaseReference hostHostedEvents = FirebaseDatabase.getInstance().getReference(
            "users/" +
            Profile.getCurrentProfile().getId() +
            "hostedEvents");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_add, container, false);

        //Intent startDatePicker = new Intent(getActivity(), DateAndTimePicker.class);
        //startActivity(startDatePicker);

        submit_button = v.findViewById(R.id.btn_createEvent);
        mDateDecoratedButton = v.findViewById(R.id.btn_endTime);
        mDateEndDecoratedButton = v.findViewById(R.id.btn_startTime);

        // Code responsible for creating the category spinner
        Resources res = getResources();
        String[] myBooks = res.getStringArray(categories);

        categorySpinner = v.findViewById(R.id.id_spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), categories, android.R.layout.simple_spinner_item);


        mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy k:mm ", Locale.getDefault());

        mDate = v.findViewById(R.id.contentMain);
        mDateEnd = v.findViewById(R.id.contentMain2);

        mDateDecoratedButton = v.findViewById(R.id.btn_startTime);
        mDateEndDecoratedButton = v.findViewById(R.id.btn_endTime);

        mDateDecoratedButton.setOnClickListener(textListener);
        mDateEndDecoratedButton.setOnClickListener(textListener);

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

        if (formDataIsValid()) {

            // Add event information to the Map object and send it to the database class
            eventMap.put("latitude", latitude);
            eventMap.put("longitude", longitude);
            eventMap.put("hostId", Profile.getCurrentProfile().getId());
            eventMap.put("attendeeLimit", attendeeLimit.getValue());
            eventMap.put("eventDescription", event_description.getText());
            eventMap.put("eventHeader", "Event Header");
            eventMap.put("eventStart", replyDateStart);
            eventMap.put("eventEnd", replyDateEnd);

            System.out.println(eventMap);

            // Generate unique event ID, and save it in the events database section
            String eventKey = eventsRef.push().getKey();
            eventsRef.child(eventKey).setValue(eventMap);

            // Add the event to the host' hosted events:
            hostHostedEvents.child(eventKey).setValue(eventMap);

        } else {
            System.out.println("Please fill in the reuired fields!");
        }
    }

    private boolean formDataIsValid() {
        return false;
    }
    /**
     * DatePicker Listeners
     */

    /* Define the onClickListener, and start the DatePickerDialog with users current time */
    private final View.OnClickListener textListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_startTime) {
                System.out.println("Start time chosen");
                //calender start time
                mCalendar = Calendar.getInstance();
                startDatePickerDialog = new DatePickerDialog(getActivity(), mDateDataSet, mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                startDatePickerDialog.show();
                selectingStartDate = true;
            } else if (v.getId() == R.id.btn_endTime) {
                System.out.println("End time chosen");
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
            System.out.println("View id: " + view.getId());
            if (selectingStartDate) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startTimePickerDialog = new TimePickerDialog(getActivity(), mTimeDataSet, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);
                startTimePickerDialog.show();
            } else if (selectingEndDate) {
                mCalendarEnd.set(Calendar.YEAR, year);
                mCalendarEnd.set(Calendar.MONTH, monthOfYear);
                mCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
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

                mDate.setText(mSimpleDateFormat.format(mCalendar.getTime()));
                replyDateStart = mSimpleDateFormat.format(mCalendar.getTime());
                System.out.println("Date start : " + replyDateStart);
                mCalendar = null;
                selectingStartDate = false;
            } else if (selectingEndDate) {
                mCalendarEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendarEnd.set(Calendar.MINUTE, minute);

                mDateEnd.setText(mSimpleDateFormat.format(mCalendarEnd.getTime()));
                replyDateEnd = mSimpleDateFormat.format(mCalendarEnd.getTime());
                System.out.println("Date end : " + replyDateEnd);
                mDateEnd = null;
                selectingEndDate = false;
            }

        }
    };
}