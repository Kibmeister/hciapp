package hci.app.EventDescription;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import hci.app.R;

public class AttendeeViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView attendeeImage;
    public TextView attendeeName;
    public RelativeLayout parentLayout;

    public AttendeeViewHolder(@NonNull View itemView) {
        super(itemView);
        attendeeImage = itemView.findViewById(R.id.event_description_attendee_profile_image);
        attendeeName = itemView.findViewById(R.id.event_description_attendee_name);
        parentLayout = itemView.findViewById(R.id.id_attendee_parent_layout);
    }
}
