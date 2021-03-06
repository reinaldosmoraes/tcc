package com.example.reinaldomoraes.testetcc;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    LinearLayout metronomeLinearLayout;
    Button tapRightButton;
    Button tapLeftButton;
    TextView expectedPatternText;
    TextView bpmText;
    TextView timeText;
    TextView setHandPatternText;
    ImageView icCorrectImageView;
    ImageView icIncorrectImageView;
    ArrayList<Tap> taps = new ArrayList();
    ArrayList<Tap> pattern;
    int defaultIntensity = 10;
    int bpm = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        metronomeLinearLayout = findViewById(R.id.metronome_linear_layout_id);
        tapRightButton = findViewById(R.id.tap_right_button_id);
        tapLeftButton = findViewById(R.id.tap_left_button_id);
        expectedPatternText = findViewById(R.id.expected_pattern_text_id);
        bpmText = findViewById(R.id.bpm_text_id);
        timeText = findViewById(R.id.time_text_id);
        icCorrectImageView = findViewById(R.id.ic_correct_image_view_id);
        icIncorrectImageView = findViewById(R.id.ic_incorrect_image_view_id);

        pattern = createPattern(Hand.RIGHT, Hand.LEFT, Hand.RIGHT, Hand.RIGHT, Hand.LEFT, Hand.RIGHT, Hand.LEFT, Hand.LEFT);

        expectedPatternText.setText(getHandPattern(pattern));
        bpmText.setText("= " + bpm);

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

        metronomeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBpmPickerDialog();
            }
        });

        expectedPatternText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetHandPatternDialog();
            }
        });

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is",""+newVal);
    }

    @Override
    public void onBackPressed() {
        refreshExercice();
    }

    private void refreshExercice() {
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

            if (tap.getHand() == Hand.RIGHT) {
                pattern += "R      ";
            } else {
                pattern += "L      ";
            }
        }
        return pattern;
    }

    private boolean isInTime(long currentInterval) {
        if (currentInterval == 0) { //first tap
            return true;
        } else if (currentInterval > bpmToMs(bpm) - 200 && currentInterval < bpmToMs(bpm) + 200) { //in time
            return true;
        } else { //out of time
            return false;
        }
    }

    private long bpmToMs (int bpm) {
        long beatsPerMilliseconds = 60000 / bpm;
        return beatsPerMilliseconds;
    }

    public void showBpmPickerDialog() {
        final Dialog bpmPickerDialog = new Dialog(MainActivity.this);
        bpmPickerDialog.setTitle("NumberPicker");
        bpmPickerDialog.setContentView(R.layout.bpm_picker_dialog);

        Button okButton = (Button) bpmPickerDialog.findViewById(R.id.ok_button_bpm_picker_id);
        Button cancelButton = (Button) bpmPickerDialog.findViewById(R.id.cancel_button_bpm_picker_id);

        final NumberPicker bpmNumberPicker = (NumberPicker) bpmPickerDialog.findViewById(R.id.bpm_number_picker_id);
        bpmNumberPicker.setMaxValue(200);
        bpmNumberPicker.setMinValue(60);
        bpmNumberPicker.setWrapSelectorWheel(false);
        bpmNumberPicker.setOnValueChangedListener(this);
        bpmNumberPicker.setValue(bpm);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bpm = bpmNumberPicker.getValue();
                bpmText.setText("= " + String.valueOf(bpmNumberPicker.getValue()));
                bpmPickerDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bpmPickerDialog.dismiss();
            }
        });

        bpmPickerDialog.show();
    }

    public void showSetHandPatternDialog() {
        final Dialog setHandPatternDialog = new Dialog(MainActivity.this);
        setHandPatternDialog.setTitle("SetHandPatternDialog");
        setHandPatternDialog.setContentView(R.layout.set_hand_pattern_dialog);

        final ArrayList<Hand> newPattern = new ArrayList<>();
        Button rightHandButton = setHandPatternDialog.findViewById(R.id.right_hand_button_id);
        Button leftHandButton = setHandPatternDialog.findViewById(R.id.left_hand_button_id);
        setHandPatternText = setHandPatternDialog.findViewById(R.id.new_expected_pattern_id);

        rightHandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHandPatternText.setText(setHandPatternText.getText() + "R ");
                newPattern.add(Hand.RIGHT);

                if (newPattern.size() >= 8){
                    pattern = createPattern(newPattern.get(0), newPattern.get(1), newPattern.get(2), newPattern.get(3), newPattern.get(4), newPattern.get(5), newPattern.get(6), newPattern.get(7));
                    expectedPatternText.setText(getHandPattern(pattern));
                    setHandPatternDialog.dismiss();
                    refreshExercice();
                }
            }
        });

        leftHandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHandPatternText.setText(setHandPatternText.getText() + "L ");
                newPattern.add(Hand.LEFT);

                if (newPattern.size() >= 8){
                    pattern = createPattern(newPattern.get(0), newPattern.get(1), newPattern.get(2), newPattern.get(3), newPattern.get(4), newPattern.get(5), newPattern.get(6), newPattern.get(7));
                    expectedPatternText.setText(getHandPattern(pattern));
                    setHandPatternDialog.dismiss();
                    refreshExercice();
                }
            }
        });

        setHandPatternDialog.show();
    }
}
