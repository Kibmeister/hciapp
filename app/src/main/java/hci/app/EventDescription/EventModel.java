package hci.app.EventDescription;

import java.util.Date;

public class EventModel {

    String hostName, eventHeader, eventDescription, eventStart, attendeeLimit, hostId;

    public EventModel(String hostName, String eventHeader, String eventDescription, String eventStart, String attendeeLimit, String hostId) {
        this.hostName = hostName;
        this.eventHeader = eventHeader;
        this.eventDescription = eventDescription;
        this.eventStart = eventStart;
        this.attendeeLimit = attendeeLimit;
        this.hostId = hostId;
    }

    public EventModel() {
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getEventHeader() {
        return eventHeader;
    }

    public void setEventHeader(String eventHeader) {
        this.eventHeader = eventHeader;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventStart() {
        return eventStart;
    }

    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }

    public String getAttendeeLimit() {
        return attendeeLimit;
    }

    public void setAttendeeLimit(String attendeeLimit) {
        this.attendeeLimit = attendeeLimit;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
}
