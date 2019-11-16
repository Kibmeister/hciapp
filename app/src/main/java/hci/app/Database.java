package hci.app;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for handling various database calls
 * via static methods
 */

// TODO: Database should handle storing/returning events

public class Database {


    // Stores an events in the database
    public static void storeEvent(Map eventMap) {
        // Debugging terminal output:
        System.out.println(eventMap);
    }

    // Returns all events saved in the database
    public static List< Map<String, Object> > getEvents() {
        List< Map<String, Object> > events = new ArrayList<>();

        // TODO: Add implementation or variable list for testing

        return events;
    }
}
