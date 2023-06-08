package com.example.stopwach;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.stopwach.databinding.FragmentFirstBinding;


public class TimerFragment extends Fragment {

    private TextView textView;
    private Button startButton;
    private Button stopButton;
    private CountDownTimer countDownTimer;

    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private NumberPicker secondPicker;
    private boolean isTimerRunning = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_timer, container, false);

        hourPicker = view.findViewById(R.id.hourPicker);
        minutePicker = view.findViewById(R.id.minutePicker);
        secondPicker = view.findViewById(R.id.secondPicker);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);

        startButton = view.findViewById(R.id.startButton);
        stopButton = view.findViewById(R.id.stopButton);
        textView = view.findViewById(R.id.dateTextView);



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) startTimer();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) stopTimer();
            }
        });

        resetTimer();

        return view;
    }
    private void updateCountdownText(long millisUntilFinished) {
        int hours = (int) (millisUntilFinished / (1000 * 60 * 60)) % 24;
        int minutes = (int) (millisUntilFinished / (1000 * 60)) % 60;
        int seconds = (int) (millisUntilFinished / 1000) % 60;

        textView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    private void startTimer(){

        int hours = hourPicker.getValue();
        int minutes = minutePicker.getValue();
        int seconds = secondPicker.getValue();
        long totalMilliseconds = (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);

        if(totalMilliseconds > 0){
            countDownTimer = new CountDownTimer(totalMilliseconds, 1000) {

                public void onTick(long millisUntilFinished) {
                    updateCountdownText(millisUntilFinished);
                }

                public void onFinish() {
                    textView.setText("Таймер завершен!");
                    resetTimer();
                }
            }.start();

            isTimerRunning = true;
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            hourPicker.setEnabled(false);
            minutePicker.setEnabled(false);
            secondPicker.setEnabled(false);
        }
    }
    private void stopTimer(){
        countDownTimer.cancel();
        textView.setText("Таймер остановлен!");
        resetTimer();
    }

    private void resetTimer() {
        isTimerRunning = false;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        hourPicker.setEnabled(true);
        minutePicker.setEnabled(true);
        secondPicker.setEnabled(true);
        hourPicker.setValue(0);
        minutePicker.setValue(0);
        secondPicker.setValue(0);
    }
}