package com.fit2081.assignment_2.providerEventCategory;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fit2081.assignment_2.entities.EventCategory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Database(entities = {EventCategory.class}, version = 1)
public abstract class EventCategoryDatabase extends RoomDatabase {
    // database name, this is important as data is contained inside a file named "card_database"
    public static final String CATEGORY_DATABASE = "category_database";

    public abstract com.fit2081.assignment_2.providerEventCategory.EventCategoryDAO eventCategoryDAO();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile EventCategoryDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Since this class is an absract class, to get the database reference we would need
     * to implement a way to get reference to the database.
     *
     * @param context Application of Activity Context
     * @return a reference to the database for read and write operation
     */
    static EventCategoryDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EventCategoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    EventCategoryDatabase.class, CATEGORY_DATABASE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}