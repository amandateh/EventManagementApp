package com.fit2081.assignment_2.providerEvent;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.fit2081.assignment_2.entities.Event;

import java.util.List;

public class EventRepository {
    // private class variable to hold reference to DAO
    private EventDAO eventDAO;

    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<Event>> allEventLiveData;

    // constructor to initialise the repository class
    EventRepository(Application application) {
        // get reference/instance of the database
        EventDatabase db = EventDatabase.getDatabase(application);

        // get reference to DAO, to perform CRUD operations
        eventDAO = db.eventDAO();
        // once the class is initialised get all the items in the form of LiveData
        allEventLiveData = eventDAO.getAllEvent();
    }

    LiveData<List<Event>> getAllEvents() {
        return allEventLiveData;
    }

    /**
     * Repository method to insert one single item
     */
    void insert(Event event) {
        EventDatabase.databaseWriteExecutor.execute(() -> eventDAO.addItem(event));
    }

    void deleteAll() {
        EventDatabase.databaseWriteExecutor.execute(() -> eventDAO.deleteAllCategory());
    }
}
