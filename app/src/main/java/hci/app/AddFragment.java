package hci.app;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.*;


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


public class AddFragment extends Fragment {
    // get a reference to the component

    private Button mDateDecoratedButton, mDateEndDecoratedButton;

    private SimpleDateFormat mSimpleDateFormat;
    private Calendar mCalendar, mCalendarEnd;
    private TextView mDate, mDateEnd;
    private String replyDateEnd, replyDateStart;
    private EditText event_description, targetGroup;
    private HorizontalNumberPicker attendeeLimit;
    private Button submit_button;

    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private boolean selectingStartDate, selectingEndDate;
    private Date fromDate, toDate;

    public AddFragment() {
        // Required empty public constructor
    }

    View v;

    DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");
    DatabaseReference userHostedEventsRef = FirebaseDatabase.getInstance().getReference("users/" + Profile.getCurrentProfile().getId() + "/hostedEvents/");

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

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        // Assume view was created from a marker on the map and has a bundle with location information
        event_description = v.findViewById(R.id.edit_txt_eventdescription);
        targetGroup = v.findViewById(R.id.edit_txt_targetgroup);
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
        if (formDataIsValid()) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Confirm event submission")
                    .setMessage("Do you wish to submit this event?")
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            submitEvent();
                            Toast.makeText(getContext(), "Event submitted!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .show();
        } else {
            System.out.println("Please fill in the reuired fields!");
        }
    }

    private void submitEvent() {
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
        // Add event information to the Map object and send it to the database class
        eventMap.put("latitude", latitude.toString());
        eventMap.put("longitude", longitude.toString());
        eventMap.put("hostId", Profile.getCurrentProfile().getId());
        // eventMap.put();
        eventMap.put("attendeeLimit", String.valueOf(attendeeLimit.getValue()));
        eventMap.put("eventHeader", event_description.getText().toString());
        eventMap.put("eventText", targetGroup.getText().toString());
        //TODO: Marker icons input

        String eventKey = eventsRef.push().getKey();
        DatabaseReference event = eventsRef.child(eventKey);
        event.setValue(eventMap);

        // Add date data in long form:
        DatabaseReference fromDateRef = event.child("fromDate");
        fromDateRef.setValue(this.fromDate.getTime());

        DatabaseReference toDateRef = event.child("toDate");
        toDateRef.setValue(this.toDate.getTime());

        // Add reference to event in host reference
        userHostedEventsRef.child(eventKey).setValue(eventKey);
        // TODO: Keep integer of hosted events counter in database?
    }

    // Check dates, attendees, text fields.
    private boolean formDataIsValid() {
        if (attendeeLimit.getMin() > attendeeLimit.getValue() && attendeeLimit.getMax() < attendeeLimit.getValue()) {
            Toast.makeText(getContext(), "Invalid number of attendees", Toast.LENGTH_LONG).show();
            return false;
        }
        if (event_description.getText().toString().length() > 40) {
            Toast.makeText(getContext(), "Too long event name!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (targetGroup.toString().length() > 200) {
            Toast.makeText(getContext(), "Event description too long!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (fromDate == null) {
            Toast.makeText(getContext(), "Invalid starting date", Toast.LENGTH_LONG).show();
            return false;
        }
        if (toDate == null) {
            Toast.makeText(getContext(), "Invalid ending date", Toast.LENGTH_LONG).show();
            return false;
        }
        if (fromDate.after(toDate)) {
            Toast.makeText(getContext(), "Your event can't end before it begins!", Toast.LENGTH_LONG).show();
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
                fromDate = mCalendar.getTime();
                mDate.setText(mSimpleDateFormat.format(mCalendar.getTime()));
                replyDateStart = mSimpleDateFormat.format(mCalendar.getTime());
                selectingStartDate = false;
            } else if (selectingEndDate) {
                mCalendarEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendarEnd.set(Calendar.MINUTE, minute);
                toDate = mCalendarEnd.getTime();
                mDateEnd.setText(mSimpleDateFormat.format(mCalendarEnd.getTime()));
                replyDateEnd = mSimpleDateFormat.format(mCalendarEnd.getTime());
                selectingEndDate = false;
            }
        }
    };
}