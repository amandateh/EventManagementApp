package com.fit2081.assignment_2.providerEventCategory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fit2081.assignment_2.entities.EventCategory;

import java.util.List;

public class EventCategoryViewModel extends AndroidViewModel {
    // reference to CardRepository
    private com.fit2081.assignment_2.providerEventCategory.EventCategoryRepository repository;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<EventCategory>> allEventCategoryLiveData;
    public EventCategoryViewModel(@NonNull Application application) {
        super(application);

        // get reference to the repository class
        repository = new com.fit2081.assignment_2.providerEventCategory.EventCategoryRepository(application);

        // get all items by calling method defined in repository class
        allEventCategoryLiveData = repository.getAllEventCategory();
    }
    public LiveData<List<EventCategory>> getAllEventCategory() {
        return allEventCategoryLiveData;
    }

    public void insert(EventCategory eventCategory) {
        repository.insert(eventCategory);
    }
    public void deleteAll() {
        repository.deleteAll();
    }

}
