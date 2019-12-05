package hci.app.EventDescription;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.Profile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.Date;

import hci.app.R;


/**
 * EventDescriptionFragment class: Responsible for displaying information about specific events to the user.
 *
 * @author Frederik Andersen
 * @author Kasper Borgbjerg
 */
public class EventDescripionFragment extends Fragment implements View.OnClickListener{


    public EventDescripionFragment() {
        // Required empty public constructor
    }

    private String eventKey;
    DatabaseReference eventRef;

    private TextView hostName, eventHeader, eventDescription, date, attendees;
    private ImageView hostProfileImage;
    private Button attendButton;

    private RecyclerView attendeeList;
    private DatabaseReference attendeeRef;
    private FirebaseRecyclerOptions<AttendeeEventModel> options;
    private FirebaseRecyclerAdapter<AttendeeEventModel, AttendeeViewHolder> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_descripion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        if (this.getArguments() != null) {
            this.eventKey = this.getArguments().getString("eventKey");
        } else {
            Toast.makeText(getContext(), "Error loading event", Toast.LENGTH_LONG).show();
        }

        this.eventRef = FirebaseDatabase.getInstance().getReference().child(
                "events/" +
                        eventKey);

        this.hostName = v.findViewById(R.id.event_host_name);
        this.eventHeader = v.findViewById(R.id.event_event_header);
        this.eventDescription = v.findViewById(R.id.event_event_description);
        this.date = v.findViewById(R.id.event_date_start);
        this.attendees = v.findViewById(R.id.event_attendees);
        this.hostProfileImage = v.findViewById(R.id.event_host_profile_image);
        this.attendButton = v.findViewById(R.id.event_attend_button);
        this.attendButton.setOnClickListener(this);

        loadEventData();

        attendeeList = getActivity().findViewById(R.id.event_description_attendee_recycler_view);
        attendeeList.setHasFixedSize(true);

        attendeeRef = eventRef.child("/attendees/");
        System.out.println(attendeeRef);
        options = new FirebaseRecyclerOptions.Builder<AttendeeEventModel>()
                .setQuery(attendeeRef, AttendeeEventModel.class).build();
        adapter = new FirebaseRecyclerAdapter<AttendeeEventModel, AttendeeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AttendeeViewHolder attendeeViewHolder, int i, @NonNull AttendeeEventModel attendeeEventModel) {
                attendeeViewHolder.attendeeName.setText(attendeeEventModel.getAttendeeName());
                Glide.with(getContext())
                        .load("https://graph.facebook.com/" + attendeeEventModel.attendeeId + "/picture?type=large")
                        .into(attendeeViewHolder.attendeeImage);
            }

            @NonNull
            @Override
            public AttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(getContext()).inflate(R.layout.event_description_attendee_layout, parent, false);

                return new AttendeeViewHolder(v);
            }
        };



        /**
         *                 viewHolder.eventHeader.setText(eventModel.getEventHeader());
         *                 viewHolder.date.setText(eventModel.getDate());
         *
         *                 // TODO: attendeeCounter, distance, icon
         *             }
         *
         *             @NonNull
         *             @Override
         *             public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         *
         *                 View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_event_item, parent, false);
         *
         *                 return new ViewHolder(v);
         *             }
         *         };
         */
    }

    private void loadEventData() {

        eventRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {

                hostName.setText(ds.getValue(EventModel.class).getHostName());
                eventHeader.setText(ds.getValue(EventModel.class).getEventHeader());
                eventDescription.setText(ds.getValue(EventModel.class).getEventDescription());
                date.setText(createDateString(Long.valueOf(ds.getValue(EventModel.class).getEventStart())));
                String imageUrl = "https://graph.facebook.com/" + ds.getValue(EventModel.class).getHostId() + "/picture?type=large";
                Picasso.get().load(imageUrl).into(hostProfileImage);
                attendees.setText(ds.getValue(EventModel.class).getAttendeeLimit()); // TODO: Calculate attendees out of total?
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    //TODO: Fix date format
    private String createDateString(long dateLong) {
        Date date = new Date(dateLong);
        return
                getDayOfWeek(date.getDay()) +
                        ", " +
                        date.getDate() +
                        "th " +
                        getMonthOfYear(date.getMonth()).substring(0, 3) +
                        ", " +
                        date.getHours() +
                        ":" +
                        getDateMinute(date.getMinutes());
    }

    private String getDateMinute(int minute) {
        String returnMinute;
        if (minute < 10) {
            return "0" + minute;
        } else {
            return String.valueOf(minute);
        }
    }

    private String getDayOfWeek(int i) {
        switch (i) {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            case 7:
                return "Sunday";
        }
        return "";
    }

    private String getMonthOfYear(int i) {
        switch (i) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.event_attend_button): {
                // Add the user to the list of attendees
                String hostId = Profile.getCurrentProfile().getId();
                eventRef.child("attendees/" + hostId + "/attendeeId").setValue(hostId);
                eventRef.child("attendees/" + hostId + "/attendeeName").setValue(Profile.getCurrentProfile().getFirstName());
            }
        }
    }
}
