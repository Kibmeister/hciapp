package hci.app;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * MyEventsFragment class: Calendar-like display of events the user is attending.
 *
 * @author Frederik Andersen
 * @author Kasper Borgbjerg
 */
public class MyEventsFragment extends Fragment implements View.OnClickListener{

    private RadioButton buttonAttendees, buttonOwn;
    private RadioGroup radioGroup;


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
}
