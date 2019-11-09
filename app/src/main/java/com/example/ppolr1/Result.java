package com.example.ppolr1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Result extends AppCompatActivity {
    public final static String EXTRA_KEY = "RESULT_MESSAGE";
    private TextView resultLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultLabel = findViewById(R.id.resultLabel);
        resultLabel.setText(getIntent().getStringExtra(EXTRA_KEY));
    }
}
