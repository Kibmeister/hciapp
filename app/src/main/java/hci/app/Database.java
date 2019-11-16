package hci.app;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling various database calls
 * via static methods
 */

// TODO: Database should handle storing/returning events

public class Database {


    // Stores an events in the database
    public static void storeEvent(JSONObject eventJSON) {
        // Debugging terminal output:
        System.out.println(eventJSON);
    }

    // Returns all events saved in the database
    public static List<JSONObject> getEvents() {
        List<JSONObject> events = new ArrayList<>();



        // TODO: Add implementation or variable list for testing

        return events;
    }
}
