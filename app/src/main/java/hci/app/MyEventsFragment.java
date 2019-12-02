package hci.app;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.Layout;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.Profile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * MyEventsFragment class: Calendar-like display of events the user is attending.
 *
 * @author Frederik Andersen
 * @author Kasper Borgbjerg
 */
public class MyEventsFragment extends Fragment implements View.OnClickListener{

    private RadioButton buttonAttending, buttonOwn;
    private RadioGroup radioGroup;
    private RecyclerView hostedEvents, attendeeEvents;
    private DatabaseReference hostedEventsRef, attendeeEventsRef;
    private FirebaseRecyclerOptions<EventModel> hostedOptions, attendeeOptions;
    private FirebaseRecyclerAdapter<EventModel, ViewHolder> hostedAdapter, attendeeAdapter;


    public MyEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_events, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {

        buttonAttending = view.findViewById(R.id.btn_attending);
        buttonOwn = view.findViewById(R.id.btn_own);
        radioGroup = view.findViewById(R.id.id_radiogroup);
        buttonAttending.setOnClickListener(this);
        buttonOwn.setOnClickListener(this);

        hostedEvents = getActivity().findViewById(R.id.id_my_events_hosted_events);
        attendeeEvents = getActivity().findViewById(R.id.id_my_events_attending_events);
        hostedEvents.setHasFixedSize(true);
        attendeeEvents.setHasFixedSize(true);

        hostedEventsRef = FirebaseDatabase.getInstance().getReference().child("users/" + Profile.getCurrentProfile().getId() + "/hostedEvents");
        attendeeEventsRef = FirebaseDatabase.getInstance().getReference().child("users/" + Profile.getCurrentProfile().getId() + "/attendeeEvents");

        hostedOptions = new FirebaseRecyclerOptions.Builder<EventModel>()
                .setQuery(hostedEventsRef, EventModel.class).build();
        attendeeOptions = new FirebaseRecyclerOptions.Builder<EventModel>()
                .setQuery(attendeeEventsRef, EventModel.class).build();

        hostedAdapter = new FirebaseRecyclerAdapter<EventModel, ViewHolder>(hostedOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull EventModel eventModel) {

                viewHolder.eventHeader.setText(eventModel.getEventHeader());
                viewHolder.date.setText(eventModel.getDate());

                // TODO: attendeeCounter, distance, icon
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_event_item, parent, false);

                return new ViewHolder(v);
            }
        };

        attendeeAdapter = new FirebaseRecyclerAdapter<EventModel, ViewHolder>(attendeeOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull EventModel eventModel) {
                viewHolder.eventHeader.setText(eventModel.getEventHeader());

                // TODO: attendeeCounter, distance, icon, date
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_event_item, parent, false);

                return new ViewHolder(v);
            }
        };

        LinearLayoutManager hostedLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager attendeeLayoutManager = new LinearLayoutManager(getContext());

        hostedEvents.setLayoutManager(hostedLayoutManager);
        attendeeEvents.setLayoutManager(attendeeLayoutManager);

        hostedAdapter.startListening();
        hostedEvents.setAdapter(hostedAdapter);
        attendeeAdapter.startListening();
        attendeeEvents.setAdapter(attendeeAdapter);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_attending){
            System.out.println("Button attendees pressed");
            radioGroup.setBackgroundResource(R.drawable.switch_layout_attendees);

        } else if(view.getId() == R.id.btn_own){
            System.out.println("Button own pressed");
            radioGroup.setBackgroundResource(R.drawable.switch_layout_own);
        }

    }
}
