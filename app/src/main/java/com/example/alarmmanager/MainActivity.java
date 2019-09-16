package com.example.alarmmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {

    TextView textOnceDate;
    TextView textOnceTime;
    EditText inputOnceMessage;
    ImageButton buttonOnceDate;
    ImageButton buttonOnceTime;
    AlarmReceiver alarmReceiver;
    Button buttonSetOnce;

    TextView textRepeatingTime;
    EditText inputRepeatingMessage;
    ImageButton buttonRepeatTime;
    Button buttonSetRepeating;

    final String DATE_PICKER_TAG= "DatePicker";
    final String TIME_PICKER_ONCE_TAG= "TimePickerOnce";
    final String TIME_PICKER_REPEAT_TAG= "TimePickerRepeat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmReceiver= new AlarmReceiver();
        setContentView(R.layout.activity_main);
        textOnceDate= findViewById(R.id.text_once_date);
        buttonOnceDate= findViewById(R.id.button_once_data);
        textOnceTime= findViewById(R.id.text_once_time);
        buttonOnceTime= findViewById(R.id.button_once_time);
        inputOnceMessage= findViewById(R.id.input_once_message);
        buttonSetOnce= findViewById(R.id.button_set_once_alarm);
        buttonOnceDate.setOnClickListener(this);
        buttonOnceTime.setOnClickListener(this);
        buttonSetOnce.setOnClickListener(this);

        textRepeatingTime= findViewById(R.id.text_repeating_time);
        buttonSetRepeating= findViewById(R.id.button_set_repeating_alarm);
        inputRepeatingMessage= findViewById(R.id.input_repeating_message);
        buttonRepeatTime= findViewById(R.id.button_repeating_time);
        buttonRepeatTime.setOnClickListener(this);
        buttonSetRepeating.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            // ketika button date tanggal diklik
            case R.id.button_once_data:
                DatePickerFragment datePickerFragment= new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
                break;
            case R.id.button_once_time:
                TimePickerFragment timePickerFragment= new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), TIME_PICKER_ONCE_TAG);
                break;
            case R.id.button_set_once_alarm:
                String onceDate= textOnceDate.getText().toString();
                String onceTime= textOnceTime.getText().toString();
                String onceMessage= inputOnceMessage.getText().toString();
                alarmReceiver.setOneTimeAlarm(this, AlarmReceiver.TYPE_ONE_TIME,
                        onceDate,
                        onceTime,
                        onceMessage);
                break;
            case R.id.button_repeating_time:
                TimePickerFragment timePickerFragment1= new TimePickerFragment();
                timePickerFragment1.show(getSupportFragmentManager(), TIME_PICKER_REPEAT_TAG);
                break;
            case R.id.button_set_repeating_alarm:
                String repeatTime= textRepeatingTime.getText().toString();
                String repeatMessage= inputRepeatingMessage.getText().toString();
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_ONE_REPEATING,
                        repeatTime,
                        repeatMessage);
                break;
        }
    }

    @Override
    public void onDialogDataSet(String tag, int year, int month, int dayOfMonth) {
        Calendar calendar= Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        textOnceDate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onDialogTimeSet(String tag, int hourOfDay, int minute) {
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat dateFormat= new SimpleDateFormat("HH:mm", Locale.getDefault());
        switch (tag){
            case TIME_PICKER_ONCE_TAG:
                textOnceTime.setText(dateFormat.format(calendar.getTime()));
                break;
            case  TIME_PICKER_REPEAT_TAG:
                // set text from activity time picker
                textRepeatingTime.setText(dateFormat.format(calendar.getTime()));
                default:
                    break;
        }
    }
}
