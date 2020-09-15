package com.celeste.celestedaylightapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.skumar.flexibleciruclarseekbar.CircularSeekBar;
import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.data.Tools;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

//import com.akaita.android.circularseekbar.CircularSeekBar;

public class AddModeActivity extends AppCompatActivity {
    private float progressValue = 10f;
    private static final float multiplier = 100f;
    private CircularSeekBar mCircularSeekBar;
    private TextView mSeekBarValue;
    private AutoCompleteTextView fromTime, toTime, presetName;
    private Button btnSave, btnPickIcon;
    private static final String TAG = "Timepickerdialog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mode);
        initToolbar();
        initCircularSeekBar();
        setup();
        setCircularSeekBarListener();
        timePickerListeners();
    }
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Custom Mode");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Tools.setSystemBarColor(this);
    }
    private void initCircularSeekBar() {
        mCircularSeekBar = findViewById(R.id.mCircularSeekBar);
        mSeekBarValue = findViewById(R.id.mSeekBarValue);
       mCircularSeekBar.setDrawMarkings(true);
       mCircularSeekBar.setDotMarkers(false);

//        mCircularSeekBar.setRingColor(getResources().getColor(R.color.colorPrimary));
        mCircularSeekBar.setRoundedEdges(true);
        mCircularSeekBar.setIsGradient(true);
        mCircularSeekBar.setPopup(false);
        mCircularSeekBar.setSweepAngle(270);
        mCircularSeekBar.setArcThickness(10);
        mCircularSeekBar.setRotation(225);
        mCircularSeekBar.setMin(0);
        mCircularSeekBar.setMax(70);

        mCircularSeekBar.setProgress(progressValue);
        mCircularSeekBar.setIncreaseCenterNeedle(20);
        mCircularSeekBar.setValueStep(2);
        mCircularSeekBar.setNeedleFrequency(0.5f);
        mCircularSeekBar.setNeedleDistanceFromCenter(32);
        mCircularSeekBar.setNeedleLengthInDP(12);
        mCircularSeekBar.setIncreaseCenterNeedle(24);
        mCircularSeekBar.setNeedleThickness(1f);
        mCircularSeekBar.setHeightForPopupFromThumb(10);

        mSeekBarValue.setText(String.valueOf(progressValue * multiplier) + "K");

    }

    private void setup() {
        fromTime = findViewById(R.id.from_time);
        toTime = findViewById(R.id.to_time);
        presetName = findViewById(R.id.preset_name);
        btnPickIcon = findViewById(R.id.btn_pick_icon);
        btnSave = findViewById(R.id.btn_save_preset);
    }

    private void timePickerListeners() {
        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                pickFromTime();
            }
        });
        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                pickToTime();
            }
        });
    }

    private void setCircularSeekBarListener() {
        mCircularSeekBar.setOnCircularSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean b) {
              //  mCircularSeekBar.setMinimumAndMaximumNeedleScale(progress - 2.5f, progress + 2.5f);
                mSeekBarValue.setText(String.valueOf(progress * multiplier) + "K");
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar circularSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar circularSeekBar) {

            }
        });
    }
    private void pickFromTime() {
        Calendar cur_calender = Calendar.getInstance();
        TimePickerDialog datePicker = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                fromTime.setText(hourOfDay + " : " + minute);
            }

        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), true);
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getSupportFragmentManager(), TAG);
    }

    private void pickToTime() {
        Calendar cur_calender = Calendar.getInstance();
        TimePickerDialog datePicker = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                toTime.setText(hourOfDay + " : " + minute);
            }
        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), true);
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getSupportFragmentManager(), TAG);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
