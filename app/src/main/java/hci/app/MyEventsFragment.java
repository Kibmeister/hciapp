package hci.app;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * MyEventsFragment class: Calendar-like display of events the user is attending.
 *
 * @author Frederik Andersen
 * @author Kasper Borgbjerg
 */
public class MyEventsFragment extends Fragment implements View.OnClickListener {

    private RadioButton buttonAttending, buttonOwn;
    private RadioGroup radioGroup;
    private RecyclerView recyclerView;
    private TextView eventDescriptionTxt;
    private ArrayList<String> mTextDescriptionOwn, mIconUrlsOwn, mTextDescriptionAttending, mIconUrlsAttending;


    public MyEventsFragment() {
        // Required empty public constructor
        mTextDescriptionOwn = new ArrayList<>();

        mIconUrlsOwn = new ArrayList<>();

        mTextDescriptionAttending = new ArrayList<>();

        mIconUrlsAttending = new ArrayList<>();
    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_events, container, false);
        buttonAttending = v.findViewById(R.id.btn_attending);
        buttonOwn = v.findViewById(R.id.btn_own);


        recyclerView = v.findViewById(R.id.id_recyclerView);


        initIconBitMapOwn();
        initIconBitMapAttending();
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        // Assume view was created from a marker on the map and has a bundle with location information
        buttonAttending = view.findViewById(R.id.btn_attending);
        buttonOwn = view.findViewById(R.id.btn_own);
        radioGroup = view.findViewById(R.id.id_radiogroup);

        buttonAttending.setOnClickListener(this);
        buttonOwn.setOnClickListener(this);
    }

    private void initIconBitMapOwn() {
        Log.d(TAG, "initIconBitMap: started");

        mIconUrlsOwn.add("https://i.pinimg.com/originals/4e/24/f5/4e24f523182e09376bfe8424d556610a.png");
        mTextDescriptionOwn.add("Dinner baby");

        mIconUrlsOwn.add("https://i.pinimg.com/originals/4e/24/f5/4e24f523182e09376bfe8424d556610a.png");
        mTextDescriptionOwn.add("Breakfast");

        mIconUrlsOwn.add("https://i.pinimg.com/originals/4e/24/f5/4e24f523182e09376bfe8424d556610a.png");
        mTextDescriptionOwn.add("Lunch");

        mIconUrlsOwn.add("https://i.pinimg.com/originals/4e/24/f5/4e24f523182e09376bfe8424d556610a.png");
        mTextDescriptionOwn.add("Munchies");

        mIconUrlsOwn.add("https://i.pinimg.com/originals/4e/24/f5/4e24f523182e09376bfe8424d556610a.png");
        mTextDescriptionOwn.add("Food");

        mIconUrlsOwn.add("https://i.pinimg.com/originals/4e/24/f5/4e24f523182e09376bfe8424d556610a.png");
        mTextDescriptionOwn.add("No sausage - veggie");

        initRecyclerView(getContext(), mIconUrlsOwn, mTextDescriptionOwn);

    }


    private void initIconBitMapAttending() {
        Log.d(TAG, "initIconBitMap: started");

        mIconUrlsAttending.add("https://i.dlpng.com/static/png/1738103_thumb.png");
        mTextDescriptionAttending.add("Dinner baby");

        mIconUrlsAttending.add("https://i.dlpng.com/static/png/1738103_thumb.png");
        mTextDescriptionAttending.add("Breakfast");

        mIconUrlsAttending.add("https://i.dlpng.com/static/png/1738103_thumb.png");
        mTextDescriptionAttending.add("Lunch");

        mIconUrlsAttending.add("https://i.dlpng.com/static/png/1738103_thumb.png");
        mTextDescriptionAttending.add("Munchies");

        mIconUrlsAttending.add("https://i.dlpng.com/static/png/1738103_thumb.png");
        mTextDescriptionAttending.add("Food");

        mIconUrlsAttending.add("https://i.dlpng.com/static/png/1738103_thumb.png");
        mTextDescriptionAttending.add("No sausage - veggie");

        //initRecyclerView();

    }

    public void initRecyclerView(Context context, ArrayList<String> url, ArrayList<String> description) {

        Log.d(TAG, "initRecyclerView: true");
        //System.out.print("inti recycler view" + recyclerView.getId());

        RecyclerViewAdapter recyclerViewAdapterAttending = new RecyclerViewAdapter(context, url, description);

        recyclerView.setAdapter(recyclerViewAdapterAttending);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO : recycleview.getAdapter().getViewholder().findViewById(R.id.id_event_description).setTextColor(Color.parseColor("#00B100");

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_attending) {
            System.out.println("Button attendees pressed");

            radioGroup.setBackgroundResource(R.drawable.switch_layout_attendees);

            initRecyclerView(getContext(), mIconUrlsAttending, mTextDescriptionAttending);


           // eventDescriptionTxt = recycleview.getAdapter().getViewholder().findViewById(R.id.id_event_description).setTextColor(Color.parseColor("#00B100");

            //eventDescriptionTxt.setTextColor(Color.parseColor("#00B100"));

        } else if (view.getId() == R.id.btn_own) {
            System.out.println("Button own pressed");

            radioGroup.setBackgroundResource(R.drawable.switch_layout_own);

            initRecyclerView(getContext(), mIconUrlsOwn, mTextDescriptionOwn);
        }

    }
}
