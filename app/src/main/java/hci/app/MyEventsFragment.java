package hci.app;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Profile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * MyEventsFragment class: Calendar-like display of events the user is attending.
 *
 * @author Frederik Andersen
 * @author Kasper Borgbjerg
 */
public class MyEventsFragment extends Fragment implements View.OnClickListener{

    private RadioButton buttonAttendees, buttonOwn;
    private RadioGroup radioGroup;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;


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
        // Assume view was created from a marker on the map and has a bundle with location information

        buttonAttendees = view.findViewById(R.id.btn_attendees);
        buttonOwn = view.findViewById(R.id.btn_own);
        radioGroup = view.findViewById(R.id.id_radiogroup);
        recyclerView = view.findViewById(R.id.id_recyclerView);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();

        buttonAttendees.setOnClickListener(this);
        buttonOwn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_attendees){
            System.out.println("Button attendees pressed");
            radioGroup.setBackgroundResource(R.drawable.switch_layout_attendees);
        } else if(view.getId() == R.id.btn_own){
            System.out.println("Button own pressed");
            radioGroup.setBackgroundResource(R.drawable.switch_layout_own);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        CircleImageView icon;
        TextView eventDescription;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.id_image);
            eventDescription = itemView.findViewById(R.id.id_item_event_header);
            parentLayout = itemView.findViewById(R.id.id_parent_layout);

        }
    }

    // fetch firebase event data
    private void fetch() {
        String userHostedRef = "/users/" + Profile.getCurrentProfile().getId() + "/hostedEvents";
    }
}
