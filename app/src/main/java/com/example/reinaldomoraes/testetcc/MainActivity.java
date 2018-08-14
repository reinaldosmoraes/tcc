package com.example.reinaldomoraes.testetcc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button tapRightButton;
    Button tapLeftButton;
    TextView timeText;
    ArrayList<Tap> taps = new ArrayList();
    Tap previousTap = new Tap(new Date()); //refact
    Tap currentTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tapRightButton = findViewById(R.id.tap_right_button_id);
        tapLeftButton = findViewById(R.id.tap_left_button_id);
        timeText = findViewById(R.id.time_text_id);

        tapRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertTapOnListWithInterval(Hand.RIGHT);
                timeText.setText(concatenateIntervals());
            }
        });

        tapLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertTapOnListWithInterval(Hand.LEFT);
                timeText.setText(concatenateIntervals());
            }
        });

    }

    private String concatenateIntervals() {
        String text = "";
        for (Tap tap : taps) {
            text += String.valueOf(tap.getInterval()) + ", ";
        }
        return text;
    }

    private void insertTapOnListWithInterval(Hand left) {
        if (taps.isEmpty()) {
            currentTap = new Tap(left, 10, new Date(), 0);
        } else {

            previousTap = taps.get(taps.size() - 1);
            currentTap = new Tap(left, 10, new Date(), 0);
            long interval = currentTap.getTapTime().getTime() - previousTap.getTapTime().getTime();
            currentTap.setInterval(interval);
        }
        taps.add(currentTap);
    }
}
