package com.fit2081.assignment_2;

import static com.fit2081.assignment_2.Utils.TAG_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.assignment_2.entities.Event;
import com.fit2081.assignment_2.entities.EventCategory;
import com.fit2081.assignment_2.providerEvent.EventViewModel;
import com.fit2081.assignment_2.providerEventCategory.EventCategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DashboardActivity extends AppCompatActivity implements IMessage{
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    Toolbar toolbar;

    EditText editTextEventId;
    EditText editTextEventName;
    EditText editTextCategoryId;
    Switch isActive;

    EditText editTextTicketsAvailable;

    SharedPreferences sP;

    ArrayList<Event> events;
    Gson gson = new Gson();

    EventViewModel eventViewModel;
    EventCategoryViewModel eventCategoryViewModel;
    private GestureDetectorCompat mDetector;


    ArrayList<EventCategory> eventCategories;
    String eventCategoriesString, eventString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_main);

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventCategoryViewModel = new ViewModelProvider(this).get(EventCategoryViewModel.class);

        editTextEventId = findViewById(R.id.editTextEventId);
        editTextEventName = findViewById(R.id.editTextEventName);
        editTextCategoryId = findViewById(R.id.editTextCategoryId);
        isActive = findViewById(R.id.isActive);
        editTextTicketsAvailable = findViewById(R.id.editTextTicketsAvailable);

        View touchpad = findViewById(R.id.touchpad);
        // initialise new instance of CustomGestureDetector class
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        // register GestureDetector and set listener as CustomGestureDetector
        mDetector = new GestureDetectorCompat(this, customGestureDetector);

        mDetector.setOnDoubleTapListener(customGestureDetector);
        touchpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });


        sP = getSharedPreferences(Keys.FILE_NAME, MODE_PRIVATE);

        eventCategoriesString = sP.getString(Keys.CATEGORY_LIST, "[]");
        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
        eventCategories = gson.fromJson(eventCategoriesString, type);

        eventString = sP.getString(Keys.EVENT_LIST, "[]");
        Type type2 = new TypeToken<ArrayList<Event>>() {}.getType();
        events = gson.fromJson(eventString, type2);

        ActivityCompat.requestPermissions(this, new String[]{"android.permission.SEND_SMS",
                "android.permission.RECEIVE_SMS", "android.permission.READ_SMS"}, 0);
        SMSReceiver.bindListener(this);

        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            saveRecordSave();
        });
        loadData();
    }

    public void addItem(Event event) {
        eventViewModel.insert(event);
    }

    private void loadData(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, FragmentListCategory.newInstance("", ""))
                .commit();
    }

    private void clearFields(){
        updateFields("", "", "", false);
        editTextEventId.setText("");
//        loadData();
    }

    private void deleteEvents(){
        SharedPreferences.Editor editor = sP.edit();
        editor.putString(Keys.EVENT_LIST, "[]");
        editor.apply();
        loadData();
    }

    private void deleteCategories(){
        SharedPreferences.Editor editor = sP.edit();
        editor.putString(Keys.CATEGORY_LIST, "[]");
        editor.apply();
        loadData();
    }



    @Override
    public void messageReceived(String message) {
        Log.d("Assignment_1_TAG", "MessageReceived: " + message);

        try {
            int commandIndex = message.indexOf(':');
            String command = message.substring(0, commandIndex);
            String params = message.substring(commandIndex + 1);
            StringTokenizer sT = new StringTokenizer(params, ";");
            if (command.equalsIgnoreCase("event")) {
                String eventName = sT.nextToken();
                String categoryId = sT.nextToken();
                String ticketsAvailable = sT.nextToken();
                String isActiveSt = sT.nextToken();
                if (isActiveSt.equalsIgnoreCase("TRUE") || isActiveSt.equalsIgnoreCase("FALSE")) {
                    boolean isActive = isActiveSt.equalsIgnoreCase("TRUE");
                    updateFields(eventName, categoryId, ticketsAvailable, isActive);
                } else {
                    throw new Exception("Invalid Format");
                }
            } else {
                Toast.makeText(this, "Unknown or invalid command", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Unknown or invalid command", Toast.LENGTH_SHORT).show();
        }

    }
    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            clearFields();
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            saveRecordSave();
            return super.onDoubleTap(e);
        }
    }

    private EventCategory findCategoryById(String categoryId){
        Log.d(TAG_KEY,"Searching for: "+categoryId);
        Log.d(TAG_KEY,"eventCategories size: "+eventCategories.size());
        for (EventCategory e : eventCategories) {
            Log.d(TAG_KEY,"CategoryID: "+e.getCategoryId());
            if (e.getCategoryId().equalsIgnoreCase(categoryId)) {
                return e;
            }
        }
        return null;
    }

    private void saveRecordSave() {

        String eventName = editTextEventName.getText().toString();
        String categoryId = editTextCategoryId.getText().toString();
        if (Utils.isNumeric(eventName) || !Utils.isAlphaNumeric(eventName)){
            Toast.makeText(this, "Invalid event name", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Integer.parseInt(editTextTicketsAvailable.getText().toString());
        } catch (Exception e){
            Toast.makeText(this, "Tickets available, valid integer value expected", Toast.LENGTH_SHORT).show();
            return;
        }

        // check category Id exists
        EventCategory categoryById = findCategoryById(categoryId);
        if (categoryById == null){
            Toast.makeText(this, "Category does not exist", Toast.LENGTH_SHORT).show();
            return;
        }



        ArrayList<EventCategory> newEventCategories = new ArrayList<>();
        for (EventCategory e : eventCategories) {
            if (e.getCategoryId().equalsIgnoreCase(categoryId)) {
                e.setEventCountIncrement();
            }

            newEventCategories.add(e);
        }

        eventCategories = newEventCategories;
//        String categoryId = Utils.generateCategoryId();
        Event newEvent = new Event(
                eventName,
                categoryId,
                Integer.parseInt(editTextTicketsAvailable.getText().toString()),
                isActive.isChecked());
        // insert new record to database
        addItem(newEvent);

        //String categoryId = editTextCategoryId.getText().toString();
        String eventIdId = Utils.generateEventId();

        SharedPreferences.Editor editor = sP.edit();
        editor.putString(Keys.EVENT_LIST, gson.toJson(events));
        editor.putString(Keys.CATEGORY_LIST, gson.toJson(eventCategories));

        editor.apply();

        editTextEventId.setText(eventIdId);

        Toast.makeText(this, "Event saved: " + eventIdId + " to "+ categoryId, Toast.LENGTH_SHORT).show();

        loadData();
    }

    private void updateFields(String _eventName, String _categoryId, String _ticketsAvailable, boolean _isActive) {
        editTextEventName.setText(_eventName);
        editTextCategoryId.setText(_categoryId);
        editTextTicketsAvailable.setText(_ticketsAvailable);
        isActive.setChecked(_isActive);
    }

    public void onEventBtnClick(View view){
        Intent intent=new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    public void onCategoryBtnClick(View view){
        Intent intent=new Intent(this, EventCategoryActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.option_menu_refresh) {
            loadData();
        }
        else if (id == R.id.option_menu_clear) {
            clearFields();
        } else if (id == R.id.option_menu_del_cat){
            eventCategoryViewModel.deleteAll();
            deleteCategories();
        } else if (id == R.id.option_menu_del_events){
            eventViewModel.deleteAll();
            deleteEvents();
        }
        return true;
    }

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();
            if (id == R.id.nav_menu_add_category) {
                onCategoryBtnClick(null);
            } else if (id == R.id.nav_menu_all_categories) {
                Intent intent = new Intent(getApplicationContext(), ListCategoryActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_menu_all_events) {
                Intent intent = new Intent(getApplicationContext(), ListEventActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_menu_logout) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }

            // close the drawer
            drawerlayout.closeDrawers();
            // tell the OS
            return true;
        }
    }
}