package com.example.clever;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.DisplayUnitListener;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.example.getauditreport.ReportGenerate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends android.app.Activity implements DisplayUnitListener {

    CleverTapAPI clevertapDefaultInstance;

    Dialog dialog;

    View view1, view2, view3;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clevertapDefaultInstance = CleverTapAPI.getDefaultInstance(getApplicationContext());

        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
        profileUpdate.put("Name", "Avinash Kalani");    // String
        profileUpdate.put("Identity", 9766609);      // String or number
        profileUpdate.put("Email", "avinash@android.com"); // Email address of the user
        profileUpdate.put("Phone", "+919766609447");   // Phone (with the country code, starting with +)
        profileUpdate.put("Gender", "M");             // Can be either M or F
        profileUpdate.put("DOB", new Date());         // Date of Birth. Set the Date object to the appropriate value first
        // optional fields. controls whether the user will be sent email, push etc.
        profileUpdate.put("MSG-email", false);        // Disable email notifications
        profileUpdate.put("MSG-push", true);          // Enable push notifications
        profileUpdate.put("MSG-sms", false);          // Disable SMS notifications
        profileUpdate.put("MSG-whatsapp", true);

        clevertapDefaultInstance.onUserLogin(profileUpdate);

        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put("Product Name", "Casio Chronograph Watch");
        prodViewedAction.put("Category", "Mens Accessories");
        prodViewedAction.put("Price", 59.99);
        prodViewedAction.put("Date", new java.util.Date());

        clevertapDefaultInstance.pushEvent("Product viewed", prodViewedAction);

        CleverTapAPI.createNotificationChannel(getApplicationContext(), "123", "avi", "PNTest", NotificationManager.IMPORTANCE_MAX, true);
        ReportGenerate.run(context);
        CleverTapAPI.setDebugLevel(3);

        CleverTapAPI.getDefaultInstance(this).setDisplayUnitListener(this); // to activate the display unit listener

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.tooltip);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT
                ,WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT
        ));

        view1 = dialog.findViewById(R.id.view1);
        view2 = dialog.findViewById(R.id.view2);
        view3 = dialog.findViewById(R.id.view3);
        textView = dialog.findViewById(R.id.text_view);


        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clevertapDefaultInstance.pushEvent("boo");
            }
        });

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clevertapDefaultInstance.pushEvent("foo");
            }
        });

        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void displayDialog(ArrayList<CleverTapDisplayUnit> units) {
        dialog.show();

        //first campaign call
        CleverTapDisplayUnit unit = units.get(0);
        ArrayList<CleverTapDisplayUnitContent> contents = unit.getContents();

        if((String.valueOf(contents.get(0).getTitle()).equals("Hey this is Shrestha"))) {
            textView.setText(String.valueOf(contents.get(0).getTitle()));
        }
        else if ((String.valueOf(contents.get(0).getTitle()).equals("Title for tooltip2"))){
            view1.setVisibility(View.INVISIBLE);
            view2.setVisibility(View.VISIBLE);
            textView.setText(String.valueOf(contents.get(0).getTitle()));
        }
        else if((String.valueOf(contents.get(0).getTitle()).equals("Title for tooltip3!!!!!"))){
            view2.setVisibility(View.INVISIBLE);
            view3.setVisibility(View.VISIBLE);
            textView.setText(String.valueOf(contents.get(0).getTitle()));
        }
    }

    @Override
    public void onDisplayUnitsLoaded(ArrayList<CleverTapDisplayUnit> units) {
        // you will get display units here
        for (int i = 0; i < units.size(); i++) {
            CleverTapDisplayUnit unit = units.get(i);
            displayDialog(units);
        }
    }
}