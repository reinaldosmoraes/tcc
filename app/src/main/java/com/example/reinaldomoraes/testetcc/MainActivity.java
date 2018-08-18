package com.example.reinaldomoraes.testetcc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button tapRightButton;
    Button tapLeftButton;
    TextView expectedPatternText;
    TextView expectedIntervalText;
    TextView timeText;
    ImageView icCorrectImageView;
    ImageView icIncorrectImageView;
    ArrayList<Tap> taps = new ArrayList();
    ArrayList<Tap> pattern;
    int defaultIntensity = 10;
    long expectedInterval = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tapRightButton = findViewById(R.id.tap_right_button_id);
        tapLeftButton = findViewById(R.id.tap_left_button_id);
        expectedPatternText = findViewById(R.id.expected_pattern_text_id);
        expectedIntervalText = findViewById(R.id.expected_interval_text_id);
        timeText = findViewById(R.id.time_text_id);
        icCorrectImageView = findViewById(R.id.ic_correct_image_view_id);
        icIncorrectImageView = findViewById(R.id.ic_incorrect_image_view_id);

        pattern = createPattern(Hand.RIGHT, Hand.LEFT, Hand.RIGHT, Hand.RIGHT, Hand.LEFT, Hand.RIGHT, Hand.LEFT, Hand.LEFT);

        expectedPatternText.setText("Padrão esperado: " + getHandPattern(pattern));
        expectedIntervalText.setText("Intervalo entre toques: " + expectedInterval + " ms");

        tapRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertTapOnListWithInterval(Hand.RIGHT);
                timeText.setText(concatenateIntervals());
                verifyTap(pattern.get((taps.size()-1) % pattern.size()), taps.get(taps.size()-1));
            }
        });

        tapLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertTapOnListWithInterval(Hand.LEFT);
                timeText.setText(concatenateIntervals());
                verifyTap(pattern.get((taps.size()-1) % pattern.size()), taps.get(taps.size()-1));
            }
        });

    }

    @Override
    public void onBackPressed() {
        taps.clear();
        timeText.setText("");
        Toast.makeText(getApplicationContext(), "Reiniciado", Toast.LENGTH_SHORT).show();
    }

    private String concatenateIntervals() {
        String text = "";
        for (Tap tap : taps) {
            text += String.valueOf(tap.getInterval()) + ", ";
        }
        return text;
    }

    private void insertTapOnListWithInterval(Hand hand) {
        Tap currentTap;

        if (taps.isEmpty()) {
            currentTap = new Tap(hand, defaultIntensity, new Date(), 0);
        } else {

            Tap previousTap = taps.get(taps.size() - 1);
            currentTap = new Tap(hand, defaultIntensity, new Date(), 0);
            long interval = currentTap.getTapTime().getTime() - previousTap.getTapTime().getTime();
            currentTap.setInterval(interval);
        }
        taps.add(currentTap);
    }

    private ArrayList<Tap> createPattern(Hand... hands) {
        ArrayList<Tap> pattern = new ArrayList();

        for (Hand hand : hands) {
            Tap tap = new Tap(hand, defaultIntensity, 500);
            pattern.add(tap);
        }
        return pattern;
    }

    private boolean verifyTap(Tap correctTap, Tap currentTap) {
        if(correctTap.getHand() == currentTap.getHand() && isInTime(currentTap.getInterval())){
            icCorrectImageView.setVisibility(View.VISIBLE);
            icIncorrectImageView.setVisibility(View.INVISIBLE);
            return true;
        } else {
            icCorrectImageView.setVisibility(View.INVISIBLE);
            icIncorrectImageView.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private String getHandPattern(ArrayList<Tap> taps) {
        String pattern = "";

        for (Tap tap : taps) {
            pattern += tap.getHand() + ", ";
        }
        return pattern;
    }

    private boolean isInTime(long currentInterval) {
        if (currentInterval == 0) { //first tap
            return true;
        } else if (currentInterval > expectedInterval - 200 && currentInterval < expectedInterval + 200) { //in time
            return true;
        } else { //out of time
            return false;
        }
    }
}
