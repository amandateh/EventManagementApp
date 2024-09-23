package com.fit2081.assignment_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.assignment_2.entities.EventCategory;
import com.fit2081.assignment_2.providerEventCategory.EventCategoryViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class EventCategoryActivity extends AppCompatActivity implements IMessage {

    EditText editTextCategoryId, editTextCategoryName, editTextEventCount, editTextLocation;
    Switch isActive;

    ArrayList<EventCategory> eventCategories;
    String eventCategoriesString;

    SharedPreferences sP;
    Gson gson = new Gson();
    EventCategoryViewModel eventCategoryViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_category);

        editTextCategoryId = findViewById(R.id.editTextCategoryId);
        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        isActive = findViewById(R.id.isActive);
        editTextEventCount = findViewById(R.id.editTextEventCount);
        editTextLocation = findViewById(R.id.editTextLocation);

        sP = getSharedPreferences(Keys.FILE_NAME, MODE_PRIVATE);

        eventCategoriesString = sP.getString(Keys.CATEGORY_LIST, "[]");
        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
        eventCategories = gson.fromJson(eventCategoriesString, type);

        ActivityCompat.requestPermissions(this, new String[]{"android.permission.SEND_SMS",
                "android.permission.RECEIVE_SMS", "android.permission.READ_SMS"}, 0);
        SMSReceiver.bindListener(this);

        eventCategoryViewModel = new ViewModelProvider(this).get(EventCategoryViewModel.class);
    }

    private void updateFields(String _categoryName, String _eventCount, boolean _isActive) {
        editTextCategoryName.setText(_categoryName);
        editTextEventCount.setText(_eventCount);
        isActive.setChecked(_isActive);
    }

    @Override
    public void messageReceived(String message) {
        Log.d("Assignment_1_TAG", "MessageReceived: " + message);

        try {
            int commandIndex = message.indexOf(':');
            String command = message.substring(0, commandIndex);
            String params = message.substring(commandIndex + 1);
            StringTokenizer sT = new StringTokenizer(params, ";");
            if (command.equals("category")) {
                String categoryName = sT.nextToken();
                String eventCount = sT.nextToken();
                String isActiveSt = sT.nextToken();

                if (!Utils.isNumeric(eventCount)) {
                    throw new Exception("Invalid Format");
                }

                if (isActiveSt.equals("TRUE") || isActiveSt.equals("FALSE")) {
                    boolean isActive = isActiveSt.equals("TRUE");
                    updateFields(categoryName, eventCount, isActive);
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

    public void onSaveClick(View view) {
        saveRecordSave();
    }

    public void addItem(EventCategory eventCategory) {
        eventCategoryViewModel.insert(eventCategory);
    }

    private void saveRecordSave() {
        String categoryName = editTextCategoryName.getText().toString();
        String locationString = editTextLocation.getText().toString();
        String eventCountString = editTextEventCount.getText().toString();
        boolean isActiveBoolean = isActive.isChecked();

        if (!(categoryName.isEmpty() || eventCountString.isEmpty() || locationString.isEmpty())) {
            if (Utils.isNumeric(categoryName) || !Utils.isAlphaNumeric(categoryName)) {
                Toast.makeText(this, "Invalid category name", Toast.LENGTH_SHORT).show();
                return;
            }

            int eventCount;
            try {
                eventCount = Integer.parseInt(eventCountString);
            } catch (Exception e) {
                Toast.makeText(this, "Event count, valid integer value expected", Toast.LENGTH_SHORT).show();
                return;
            }

            String categoryIDString = Utils.generateCategoryId();
            editTextCategoryId.setText(categoryIDString);

            EventCategory newEventC = new EventCategory(
                    categoryName,
                    eventCount,
                    isActiveBoolean,
                    locationString
            );

            addItem(newEventC);
            eventCategories.add(newEventC);

            SharedPreferences.Editor editor = sP.edit();
            editor.putString(Keys.CATEGORY_LIST, gson.toJson(eventCategories));
            editor.putString(Keys.CATEGORY_NAME, categoryName);
            editor.putBoolean(Keys.CATEGORY_IS_ACTIVE, isActiveBoolean);
            editor.putInt(Keys.CATEGORY_EVENT_COUNT, eventCount);
            editor.apply();

            Toast.makeText(this, String.format("Category Event Saved: %s", categoryIDString), Toast.LENGTH_LONG).show();

            finish();
        } else {
            Toast.makeText(this, "Missing Inputs", Toast.LENGTH_LONG).show();
        }
    }
}
