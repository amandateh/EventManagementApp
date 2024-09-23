package com.fit2081.assignment_2.providerEventCategory;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.fit2081.assignment_2.entities.EventCategory;

import java.util.List;

public class EventCategoryRepository {
    // private class variable to hold reference to DAO
    private com.fit2081.assignment_2.providerEventCategory.EventCategoryDAO eventCategoryDAO;

    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<EventCategory>> allEventCategoryLiveData;
    // constructor to initialise the repository class
    EventCategoryRepository(Application application) {
        // get reference/instance of the database
        com.fit2081.assignment_2.providerEventCategory.EventCategoryDatabase db = EventCategoryDatabase.getDatabase(application);

        // get reference to DAO, to perform CRUD operations
        eventCategoryDAO = db.eventCategoryDAO();

        // once the class is initialised get all the items in the form of LiveData
        allEventCategoryLiveData = eventCategoryDAO.getAllItems();
    }

    LiveData<List<EventCategory>> getAllEventCategory() {
        return allEventCategoryLiveData;
    }


    void insert(EventCategory eventCategory) {
        com.fit2081.assignment_2.providerEventCategory.EventCategoryDatabase.databaseWriteExecutor.execute(() -> eventCategoryDAO.addItem(eventCategory));
    }

    void deleteAll() {
        EventCategoryDatabase.databaseWriteExecutor.execute(() -> eventCategoryDAO.deleteAllCategory());
    }
}
