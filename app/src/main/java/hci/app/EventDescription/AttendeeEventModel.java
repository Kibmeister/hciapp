package hci.app.EventDescription;

public class AttendeeEventModel {

    public String attendeeId, attendeeName;

    public AttendeeEventModel(String attendeeId, String attendeeName) {
        this.attendeeId = attendeeId;
        this.attendeeName = attendeeName;
    }

    public AttendeeEventModel() {
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getAttendeeName() {
        return attendeeName;
    }

    public void setAttendeeName(String attendeeName) {
        this.attendeeName = attendeeName;
    }
}
