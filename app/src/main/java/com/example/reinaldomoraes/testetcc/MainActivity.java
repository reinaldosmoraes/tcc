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
//    Tap tap1 = new Tap(Hand.RIGHT);
//    Tap tap2 = new Tap(Hand.LEFT);
    ArrayList<Tap> taps = new ArrayList();
    long interval;
    Tap previousTap = new Tap(new Date()); //refact

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

                Tap tap = new Tap(Hand.RIGHT, new Date());
                taps.add(tap);

//              tap1.setTime(new Date());
            }
        });

        tapLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tap tap = new Tap(Hand.LEFT, new Date());
                taps.add(tap);

                if (taps.size() >= 2){

                    for (Tap currentTap : taps) {
                        timeText.setText(String.valueOf(currentTap.getTime().getTime() - previousTap.getTime().getTime()));

                        previousTap = currentTap;
                    }

                }

//                tap2.setTime(new Date());
//                long mills = tap2.getTime().getTime() - tap1.getTime().getTime();
//
//                if(mills > 0){
//                    timeText.setText(String.valueOf(mills));
//                }

            }
        });


    }
}
