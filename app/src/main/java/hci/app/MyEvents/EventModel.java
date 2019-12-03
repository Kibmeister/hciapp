package hci.app.MyEvents;

import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventModel {

    /* CircleImageView icon;
    TextView eventDescription, date, attendeeCounter, distance;
    RelativeLayout parentLayout; */

    public String iconName, eventHeader, eventStart, eventEnd, attendeeCounter, distance;


    public EventModel(String iconName, String eventHeader, String eventStart, String attendeeCounter, String distance, String eventEnd) {
        this.iconName = iconName;
        this.eventHeader = eventHeader;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.attendeeCounter = attendeeCounter;
        this.distance = distance;
    }

    public EventModel() {
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getEventHeader() {
        return eventHeader;
    }

    public void setEventHeader(String eventHeader) {
        this.eventHeader = eventHeader;
    }

    public String getDate() {
        Date startDate = new Date(Long.valueOf(eventStart));
        Date endDate = new Date(Long.valueOf(eventEnd));

        return
                getDayOfWeek(startDate.getDay()) +
                ", " +
                startDate.getHours() +
                " - " +
                endDate.getHours();

        // "Sunday, 19-20"
    }

    private String getDayOfWeek(int i) {
        switch (i) {
            case 1: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            case 6: return "Saturday";
            case 7: return "Sunday";
        }
        return "";
    }

    public void setDate(String date) {
        this.eventStart = date;
    }

    public String getAttendeeCounter() {
        return attendeeCounter;
    }

    public void setAttendeeCounter(String attendeeCounter) {
        this.attendeeCounter = attendeeCounter;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
