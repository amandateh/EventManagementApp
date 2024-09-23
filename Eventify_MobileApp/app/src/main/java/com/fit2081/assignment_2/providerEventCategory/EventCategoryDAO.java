package com.fit2081.assignment_2.providerEventCategory;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.fit2081.assignment_2.entities.EventCategory;

import java.util.List;
@Dao
public interface EventCategoryDAO {
    @Query("select * from EventCategory")
    LiveData<List< EventCategory>> getAllItems();
    @Query("DELETE FROM EventCategory")
    void deleteAllCategory();
    @Insert
    void addItem(EventCategory eventCategory);
}
