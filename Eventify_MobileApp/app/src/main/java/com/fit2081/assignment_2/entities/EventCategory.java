package com.fit2081.assignment_2.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fit2081.assignment_2.Utils;
@Entity(tableName = "eventcategory")
public class EventCategory {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "categoryId")
    private String categoryId;
    @ColumnInfo(name = "eventCount")
    private int eventCount;
    @ColumnInfo(name = "isActive")
    private boolean isActive;
    @ColumnInfo(name = "location")
    String location;

    public EventCategory(String name, int eventCount, boolean isActive,String location) {
        this.categoryId = Utils.generateCategoryId();
        this.name = name;
        this.eventCount = eventCount;
        this.isActive = isActive;
        this.location = location;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCountIncrement() {
        this.eventCount += 1;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
