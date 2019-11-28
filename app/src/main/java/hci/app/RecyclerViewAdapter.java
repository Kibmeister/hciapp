package hci.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList <String> mIconName ;
    private ArrayList <String> mEventDescription ;
    private Context mContext;

    public RecyclerViewAdapter (Context context, ArrayList<String> iconName, ArrayList<String> eventDescription){
        this.mIconName = iconName;
        this.mEventDescription = eventDescription;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_item,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Log.d(TAG, "onBindViewHolder: called");
        Glide.with(mContext)
                .asBitmap()
                .load(mIconName.get(position))
                .into(holder.icon);

        holder.eventDescription.setText(mEventDescription.get(position));

    }

    @Override
    public int getItemCount() {
        return mEventDescription.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView icon;
        TextView eventDescription;
        RelativeLayout parentLayout;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.id_image);
            eventDescription = itemView.findViewById(R.id.id_item_event_description);
            parentLayout = itemView.findViewById(R.id.id_parent_layout);

        }
    }
}
