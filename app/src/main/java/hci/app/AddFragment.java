package hci.app;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;

import com.facebook.Profile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
 * <p>
 * Date and Time picker:spawns a date picker followed up by a time picker
 * that sends the data back to the previous fragment and stores it as a variable.
 * Code inspired from source:
 * GitHub :https://github.com/Kiarasht/Android-Templates/tree/master/Templates/DatePickerDialog
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
    private Date replyDateEnd, replyDateStart;
    private Spinner categorySpinner;

    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    private boolean selectingStartDate, selectingEndDate;

    public AddFragment() {


    }

    View v;

    EditText event_description, event_header;
    HorizontalNumberPicker attendeeLimit;
    private Button submit_button;

    DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");
    DatabaseReference hostHostedEvents = FirebaseDatabase.getInstance().getReference(
            "users/" +
                    Profile.getCurrentProfile().getId() +
                    "/hostedEvents");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_add, container, false);

        submit_button = v.findViewById(R.id.btn_createEvent);

        mDateDecoratedButton = v.findViewById(R.id.btn_endTime);
        mDateEndDecoratedButton = v.findViewById(R.id.btn_startTime);

        // Code responsible for creating the category spinner
        List<String> list = new ArrayList<String>();
        list.add("");
        list.add("Restaurant");
        list.add("Cinema");
        list.add("Drinks");
        list.add("Sports");
        list.add("Other");

        categorySpinner = v.findViewById(R.id.id_spinner_category);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_custom, list);

        dataAdapter.setDropDownViewResource(R.layout.drop_down_resource);

        categorySpinner.setAdapter(dataAdapter);

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
        event_description = v.findViewById(R.id.edit_txt_eventDescription);
        event_header = v.findViewById(R.id.edit_txt_eventHeader);
        attendeeLimit = v.findViewById(R.id.np_channel_nr);
        attendeeLimit.setMin(1);
        attendeeLimit.setMax(8);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formDataIsValid()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Submit event")
                            .setMessage("Submit event?")
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    createEventAtLocation();
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
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

            String hostId = Profile.getCurrentProfile().getId();

            // Add event information to the Map object and send it to the database class
            eventMap.put("latitude", latitude.toString());
            eventMap.put("longitude", longitude.toString());
            eventMap.put("category", categorySpinner.getSelectedItem().toString().toLowerCase());
            eventMap.put("hostId", hostId);
            eventMap.put("attendeeLimit", String.valueOf(attendeeLimit.getValue()));
            eventMap.put("eventDescription", event_description.getText().toString());
            eventMap.put("eventHeader", event_header.getText().toString());
            eventMap.put("eventStart", String.valueOf(replyDateStart.getTime()));
            eventMap.put("eventEnd", String.valueOf(replyDateEnd.getTime()));

            System.out.println(eventMap);

            // Generate unique event ID, and save it in the events database section
            String eventKey = eventsRef.push().getKey();
            eventMap.put("eventKey", eventKey);
            DatabaseReference eventRef = eventsRef.child(eventKey);
            eventRef.setValue(eventMap);
            // Add the host to the list of attendees
            DatabaseReference attendeeRef = eventRef.child("attendees" + hostId);
            attendeeRef.child("/attendeeName").setValue(Profile.getCurrentProfile().getFirstName());
            attendeeRef.child("/attendeeId").setValue(hostId);

            // Add the event to the host' hosted events:
            hostHostedEvents.child(eventKey).setValue(eventMap);
    }

    /**
     * @return true if all input data is valid
     * <p>
     * Checks event Description, event Header, dates, number of people and that a category has been selected.
     */
    private boolean formDataIsValid() {
        if (event_header.getText().length() < 4 || event_header.getText().length() > 30) {
            Toast.makeText(getContext(), "Invalid event header", Toast.LENGTH_LONG).show();
            return false;
        }
        if (event_description.getText().length() < 10 || event_description.getText().length() > 100) {
            Toast.makeText(getContext(), "Invalid event description", Toast.LENGTH_LONG).show();
            return false;
        }
        if (replyDateStart == null) {
            Toast.makeText(getContext(), "Invalid start date", Toast.LENGTH_LONG).show();
            return false;
        }
        if (replyDateEnd == null) {
            Toast.makeText(getContext(), "Invalid starting date", Toast.LENGTH_LONG).show();
            return false;
        }
        if (replyDateStart.after(replyDateEnd)) {
            Toast.makeText(getContext(), "Invalid end date", Toast.LENGTH_LONG).show();
            return false;
        }
        if (attendeeLimit.getValue() < 1 || attendeeLimit.getValue() > 8) {
            Toast.makeText(getContext(), "Invalid amount of attendees", Toast.LENGTH_LONG).show();
            return false;
        }
        if (categorySpinner.getSelectedItem().toString().equals("")) {
            Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_LONG).show();
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
                replyDateStart = mCalendar.getTime();
                System.out.println("Date start : " + replyDateStart);
                mCalendar = null;
                selectingStartDate = false;
            } else if (selectingEndDate) {
                mCalendarEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendarEnd.set(Calendar.MINUTE, minute);

                mDateEnd.setText(mSimpleDateFormat.format(mCalendarEnd.getTime()));
                replyDateEnd = mCalendarEnd.getTime();
                System.out.println("Date end : " + replyDateEnd);
                mDateEnd = null;
                selectingEndDate = false;
            }

        }
    };
}
