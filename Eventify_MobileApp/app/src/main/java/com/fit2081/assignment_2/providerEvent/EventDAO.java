package com.fit2081.assignment_2.providerEvent;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.fit2081.assignment_2.entities.Event;

import java.util.List;
@Dao
public interface EventDAO {
    @Query("select * from event")
    LiveData<List<Event>> getAllEvent();
    @Insert
    void addItem(Event event);
    @Query("DELETE FROM event")
    void deleteAllCategory();
}
