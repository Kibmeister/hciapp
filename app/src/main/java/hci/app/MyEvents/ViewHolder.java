package hci.app.MyEvents;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import hci.app.R;

public class ViewHolder extends RecyclerView.ViewHolder{

    public CircleImageView icon;
    public TextView eventHeader, date, attendeeCounter, distance;
    public RelativeLayout parentLayout;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.id_image);
        eventHeader = itemView.findViewById(R.id.id_item_event_header);
        date = itemView.findViewById(R.id.id_item_event_date);
        attendeeCounter = itemView.findViewById(R.id.id_item_event_attendee_count);
        distance = itemView.findViewById(R.id.distance);
        parentLayout = itemView.findViewById(R.id.id_parent_layout);
    }
}
