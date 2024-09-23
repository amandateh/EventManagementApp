package com.fit2081.assignment_2.providerEvent;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fit2081.assignment_2.entities.Event;

import java.util.List;

public class EventViewModel extends AndroidViewModel {
    // reference to CardRepository
    private EventRepository repository;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<Event>> allEventLiveData;
    public EventViewModel(@NonNull Application application) {
        super(application);

        // get reference to the repository class
        repository = new EventRepository(application);

        // get all items by calling method defined in repository class
        allEventLiveData = repository.getAllEvents();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEventLiveData;
    }
    public void insert(Event event) {
        repository.insert(event);
    }
    public void deleteAll() {
        repository.deleteAll();
    }
}
