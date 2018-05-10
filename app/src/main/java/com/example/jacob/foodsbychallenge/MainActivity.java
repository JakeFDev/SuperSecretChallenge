package com.example.jacob.foodsbychallenge;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Model model;
    int day;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String json = readDeliveriesJSON();
        model = new GsonBuilder().create().fromJson(json, Model.class);

        setTodayButton();
        setDeliveriesInView(day);
    }

    public void onDayClick(View view) {
        int id = view.getId();
        Button tappedDayBtn = null;

        switch (id) {
            case R.id.mondayBtn :
                setDeliveriesInView(Calendar.MONDAY);
                resetAllButtons();
                tappedDayBtn = findViewById(R.id.mondayBtn);
                break;
            case R.id.tuesdayBtn :
                setDeliveriesInView(Calendar.TUESDAY);
                resetAllButtons();
                tappedDayBtn = findViewById(R.id.tuesdayBtn);
                break;
            case R.id.wednesdayBtn :
                setDeliveriesInView(Calendar.WEDNESDAY);
                resetAllButtons();
                tappedDayBtn = findViewById(R.id.wednesdayBtn);
                break;
            case R.id.thursdayBtn :
                setDeliveriesInView(Calendar.THURSDAY);
                resetAllButtons();
                tappedDayBtn = findViewById(R.id.thursdayBtn);
                break;
            case R.id.fridayBtn :
                setDeliveriesInView(Calendar.FRIDAY);
                resetAllButtons();
                tappedDayBtn = findViewById(R.id.fridayBtn);
                break;
            default:
                break;
        }

        if (tappedDayBtn != null) {
            tappedDayBtn.setBackgroundResource(R.drawable.button_active);
            tappedDayBtn.setTextColor(Color.WHITE);
            tappedDayBtn.setElevation(100);
        }
        else
            Log.e("MainActivity", "Day button clicked does not have a valid id.");
    }

    public void setTodayButton() {
        Calendar calender = Calendar.getInstance();
        day = calender.get(Calendar.DAY_OF_WEEK);
        Button todayBtn = findViewById(R.id.mondayBtn);
        switch (day) {
            case Calendar.MONDAY:
                todayBtn = findViewById(R.id.mondayBtn);
                break;
            case Calendar.TUESDAY:
                todayBtn = findViewById(R.id.tuesdayBtn);
                break;
            case Calendar.WEDNESDAY:
                todayBtn = findViewById(R.id.wednesdayBtn);
                break;
            case Calendar.THURSDAY:
                todayBtn = findViewById(R.id.thursdayBtn);
                break;
            case Calendar.FRIDAY:
                todayBtn = findViewById(R.id.fridayBtn);
                break;
            default:
                break;
        }
        todayBtn.setText(R.string.today);
        todayBtn.setBackgroundResource(R.drawable.button_active);
        todayBtn.setElevation(100);
        todayBtn.setTextColor(Color.WHITE);
    }

    public String readDeliveriesJSON() {
        String json = null;
        try {
            InputStream is = this.getResources().openRawResource(R.raw.deliveries_sample);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Model", "Failed to open deliveries json file!");
            return null;
        }
        return json;
    }

    public void setDeliveriesInView(int day) {

        String dayOfWeek = "";
        switch (day) {
            case Calendar.MONDAY:
                dayOfWeek = "Monday";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "Friday";
                break;
            default:
                break;
        }

        boolean containsDeliveries = false;

        //Get the list of restaurant names for our adapter (needed to populate listview)
        String[] restNames;
        for (int i = 0; i < model.dropOffs.size(); i++) {
            if (model.dropOffs.get(i).day.equals(dayOfWeek)) {
                containsDeliveries = true;
                restNames = new String[model.dropOffs.get(i).deliveries.size()];
                int j = 0;
                for (Model.Delivery delivery : model.dropOffs.get(i).deliveries) {
                    restNames[j] = delivery.restaurantName;
                    j++;
                }
                DeliveryListAdapter deliveryAdapter = new DeliveryListAdapter(this, model.dropOffs.toArray(), dayOfWeek, restNames);
                listView = (ListView) findViewById(R.id.listViewContent);
                listView.setAdapter(deliveryAdapter);

                break;
            }
        }

        if (!containsDeliveries) {
            listView = (ListView) findViewById(R.id.listViewContent);
            listView.setAdapter(null);
        }
    }

    public void resetAllButtons() {
        LinearLayout buttonContainer = findViewById(R.id.buttonContainer);
        for (int i = 0; i < buttonContainer.getChildCount(); i++) {
            Button button = (Button) buttonContainer.getChildAt(i);
            button.setBackgroundResource(R.drawable.button_focus);
            button.setTextColor(Color.BLACK);
            button.setElevation(0);
        }

    }


}
