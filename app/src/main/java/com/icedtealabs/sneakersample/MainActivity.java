package com.icedtealabs.sneakersample;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.icedtealabs.notifierce.Notifierce;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btShowError;
    Button btShowSuccess;
    Button btShowWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btShowError = (Button) findViewById(R.id.btShowError);
        btShowSuccess = (Button) findViewById(R.id.btShowSuccess);
        btShowWarning = (Button) findViewById(R.id.btShowWarning);

        btShowError.setOnClickListener(this);
        btShowSuccess.setOnClickListener(this);
        btShowWarning.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btShowError:
                Notifierce.builder(this)
                        .setTitle("Error!!")
                        .setMessage("This is the error message")
                        .setTypeface(Typeface.createFromAsset(this.getAssets(), "font/" + "Slabo27px-Regular.ttf"))
                        .build()
                        .error();
                break;
            case R.id.btShowSuccess:
                Notifierce.builder(this)
                        .setTitle("Success!!")
                        .setMessage("This is the success message")
                        .build()
                        .success();
                break;
            case R.id.btShowWarning:
                Notifierce.builder(this)
                        .setTitle("Warning!!")
                        .setMessage("This is the warning message")
                        .build()
                        .warning();
                break;
        }
    }
}
