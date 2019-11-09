package com.example.ppolr1;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity";
    private static int BALL_STEP = 20;

    private ImageView ballView;
    private ImageView holeView;
    private TextView startLabelView;
    private Timer timer = new Timer();
    private boolean wasTouched = false;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Float prevX = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ballView = findViewById(R.id.ball);
        holeView = findViewById(R.id.hole);
        startLabelView = findViewById(R.id.startLabel);

        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "OnCreate: Registered accelerometer listener");
    }


    private void makeStepToBottom() {
        if (ballView.getY() > 0) {
            this.ballView.setY(this.ballView.getY() + BALL_STEP);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (!wasTouched) {
            this.startLabelView.setVisibility(View.GONE);
            wasTouched = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    makeStepToBottom();
                    checkGameEndConditions();
                }
            }, 0, 50);
        }
        return true;
    }

    private void startResultActivity(String message) {
        Intent intent = new Intent(getApplicationContext(), Result.class);
        intent.putExtra(Result.EXTRA_KEY, message);
        startActivity(intent);
        timer.cancel();
    }

    private void checkGameEndConditions() {
        if (distanceBetweenBallAndHole() < 50) {
            startResultActivity("WIN");
        } else if (ballView.getY() > (holeView.getY() + holeView.getHeight())) {
            startResultActivity("LOOSE");
        }
    }

    private double distanceBetweenBallAndHole() {
        float ballX = ballView.getX();
        float ballY = ballView.getY();
        float holeX = holeView.getX();
        float holeY = holeView.getY();

        return Math.sqrt(Math.pow(ballX - holeX, 2) + Math.pow(ballY - holeY, 2));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!wasTouched) {
            return;
        }

        float currentX = event.values[0];
        if (prevX == null) {
            prevX = currentX;
            return;
        }


        float deviation = prevX - currentX;
        boolean isSignificant = Math.abs(prevX - currentX) > 0.1;
        if (isSignificant) {
            handlePhoneRotation(deviation);
        }
    }

    private void handlePhoneRotation(float deviation) {
        float currentX = ballView.getX();
        if (deviation > 0) {
            ballView.setX(currentX + BALL_STEP);
        } else {
            ballView.setX(currentX - BALL_STEP);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
