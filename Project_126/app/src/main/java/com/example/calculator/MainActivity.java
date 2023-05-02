package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String oldValue = "";
    String newValue = "";
    String value = "";
    String action = "";
    TextView textView;
    TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
    }
    public void click(String value){
        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:

                break;
            case R.id.button2:
                oldValue = value = "";
                action = "";
                textView.setText("");
                textView2.setText("0");
                break;
            case R.id.button3:

                break;
            case R.id.button4:

                break;
            case R.id.button5:

                break;
            case R.id.button6:

                break;
            case R.id.button7:

                break;
            case R.id.button8:
                oldValue = value;
                value = "";
                action = "/";
                textView.setText(String.valueOf(oldValue + " " + action));
                break;
            case R.id.button9:
                value += "7";
                textView2.setText(value);
                break;
            case R.id.button10:
                value += "8";
                textView2.setText(value);
                break;
            case R.id.button11:
                value += "9";
                textView2.setText(value);
                break;
            case R.id.button12:
                oldValue = value;
                value = "";
                action = "*";
                textView.setText(String.valueOf(oldValue + " " + action));
                break;
            case R.id.button13:
                value += "4";
                textView2.setText(value);
                break;
            case R.id.button14:
                value += "5";
                textView2.setText(value);
                break;
            case R.id.button15:
                value += "6";
                textView2.setText(value);
                break;
            case R.id.button16:
                oldValue = value;
                value = "";
                action = "-";
                textView.setText(String.valueOf(oldValue + " " + action));
                break;
            case R.id.button17:
                value += "1";
                textView2.setText(value);
                break;
            case R.id.button18:
                value += "2";
                textView2.setText(value);
                break;
            case R.id.button19:
                value += "3";
                textView2.setText(value);
                break;
            case R.id.button20:
                oldValue = value;
                value = "";
                action = "+";
                textView.setText(String.valueOf(oldValue + " " + action));
                break;
            case R.id.button21:

                break;
            case R.id.button22:

                break;
            case R.id.button23:

                break;
            case R.id.button24:
                result();

                break;
        }
    }
    public void result(){
        textView.setText(oldValue + " " + action + " " + value + " =");

        if(action == "+"){
            newValue = String.format("%.0f", Double.parseDouble(oldValue) + Double.parseDouble(value));
        }
        else if(action == "-"){
            newValue = String.format("%.0f", Double.parseDouble(oldValue) - Double.parseDouble(value));
        }
        else if(action == "/"){
            newValue = String.format("%.0f", Double.parseDouble(oldValue) / Double.parseDouble(value));
        }
        else if(action == "*"){
            newValue = String.format("%.0f", Double.parseDouble(oldValue) * Double.parseDouble(value));
        }
        oldValue = newValue;
        textView2.setText(newValue);
    }
}