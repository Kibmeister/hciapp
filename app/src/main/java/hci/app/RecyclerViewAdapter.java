package hci.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 *
 *
 * @author Kasper Borgbjerg
 * @author Frederik Andersen
 *
 * Code inspired from source:
 * YouTube :https://www.youtube.com/watch?v=Vyqz_-sJGFk
 * Firebase adapter: https://stackoverflow.com/questions/49356408/how-to-load-firebase-data-into-recyclerview ?
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList <String> mIconNames = new ArrayList<>();
    private ArrayList <String> mEventDescription = new ArrayList<>();
    private ArrayList <String> mDate = new ArrayList<>();
    private ArrayList <String> mAttendeeCounter = new ArrayList<>();
    private ArrayList <String> mDistance = new ArrayList<>();

    private Context mcontext;

    public RecyclerViewAdapter(ArrayList<String> mIconNames,
                               ArrayList<String> mEventDescription,
                               ArrayList<String> mDate, ArrayList<String> mAttendeeCounter,
                               ArrayList<String> mDistance,
                               Context mcontext) {
        this.mIconNames = mIconNames;
        this.mEventDescription = mEventDescription;
        this.mDate = mDate;
        this.mAttendeeCounter = mAttendeeCounter;
        this.mDistance = mDistance;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mIconNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView icon;
        TextView eventDescription, date, attendeeCounter, distance;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.id_image);
            eventDescription = itemView.findViewById(R.id.id_item_event_header);
            date = itemView.findViewById(R.id.id_item_event_date);
            attendeeCounter = itemView.findViewById(R.id.id_item_event_attendee_count);
            distance = itemView.findViewById(R.id.distance);
            parentLayout = itemView.findViewById(R.id.id_parent_layout);
        }
    }
}
