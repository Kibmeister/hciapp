package hci.app;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * MyEventsFragment class: Calendar-like display of events the user is attending.
 *
 * @author Frederik Andersen
 * @author Kasper Borgbjerg
 */
public class MyEventsFragment extends Fragment {


    public MyEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_events, container, false);
    }

}
