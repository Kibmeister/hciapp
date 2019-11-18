package hci.app;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * EventDescriptionFragment class: Responsible for displaying information about specific events to the user.
 *
 * @author Frederik Andersen
 * @author Kasper Borgbjerg
 */
public class EventDescripionFragment extends Fragment {


    public EventDescripionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_descripion, container, false);
    }

}
