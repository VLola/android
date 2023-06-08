package com.example.stopwach;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class StopwatchFragment extends Fragment {


    private TextView textView;
    private Button startButton;
    private Button stopButton;
    private Button pauseButton;
    private Timer timer;
    private boolean isStopwatchRunning = false;
    long milliseconds = 0;
    boolean isStopwatchPause = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_stopwatch, container, false);

        startButton = view.findViewById(R.id.startButton);
        stopButton = view.findViewById(R.id.stopButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        textView = view.findViewById(R.id.dateTextView);

        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStopwatchRunning) startStopwatch();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStopwatchRunning) stopStopwatch();
            }
        });


        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseStopwatch();
            }
        });

        return view;
    }

    private void startStopwatch(){
        isStopwatchPause = false;
        isStopwatchRunning = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateText(milliseconds);
                milliseconds+=1000;
            }
        }, 0, 1000);

        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        pauseButton.setEnabled(true);
    }
    private void stopStopwatch(){
        isStopwatchRunning = false;
        if(timer!= null){
            timer.cancel();
            timer = null;
        }
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
        milliseconds = 0;
        updateText(milliseconds);
    }
    private void updateText(long millisUntilFinished) {
        int hours = (int) (millisUntilFinished / (1000 * 60 * 60)) % 24;
        int minutes = (int) (millisUntilFinished / (1000 * 60)) % 60;
        int seconds = (int) (millisUntilFinished / 1000) % 60;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }
        });
    }
    private void pauseStopwatch(){
        if(isStopwatchPause){
            isStopwatchPause = false;
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    updateText(milliseconds);
                    milliseconds+=1000;
                }
            }, 0, 1000);
        }
        else{
            isStopwatchPause = true;
            timer.cancel();
            timer = null;
        }
    }
}