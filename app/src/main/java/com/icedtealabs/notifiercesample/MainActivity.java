package com.icedtealabs.notifiercesample;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.icedtealabs.notifierce.Notifierce;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnShowError;
    Button btnShowSuccess;
    Button btnShowWarning;
    Button btnShowCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowError = (Button) findViewById(R.id.btn_show_error);
        btnShowSuccess = (Button) findViewById(R.id.btn_show_success);
        btnShowWarning = (Button) findViewById(R.id.btn_show_warning);
        btnShowCustom = (Button) findViewById(R.id.btn_show_custom);

        btnShowError.setOnClickListener(this);
        btnShowSuccess.setOnClickListener(this);
        btnShowWarning.setOnClickListener(this);
        btnShowCustom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_error:
                Notifierce.builder(this)
                        .setTitle("Error")
                        .setMessage("This is the error message")
                        .setTypeface(Typeface.createFromAsset(this.getAssets(), "font/" + "RobotoCondensed-Regular.ttf"))
                        .build()
                        .error();
                break;
            case R.id.btn_show_success:
                Notifierce.builder(this)
                        .setTitle("Success")
                        .setMessage("This is the success message")
                        .build()
                        .success();
                break;
            case R.id.btn_show_warning:
                Notifierce.builder(this)
                        .setTitle("Warning")
                        .setMessage("This is the warning message")
                        .build()
                        .warning();
                break;
            case R.id.btn_show_custom:
                Notifierce.builder(this)
                        .setTitle("Notifierce")
                        .setTitleColor(Color.BLUE)
                        .setMessage("Welcome to Iced Tea Labs")
                        .setMessageColor(Color.WHITE)
                        .setBackgroundColor(Color.GRAY)
                        .setIcon(R.mipmap.ic_launcher)
                        .autoHide(false)
                        .build()
                        .show();
                break;
        }
    }
}
