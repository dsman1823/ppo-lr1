package com.example.ppolr1;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static int BALL_STEP = 20;

    private ImageView ballView;
    private ImageView holeView;
    private TextView startLabelView;
    private Timer timer = new Timer();
    private boolean wasTouched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ballView = findViewById(R.id.ball);
        holeView = findViewById(R.id.hole);
        startLabelView = findViewById(R.id.startLabel);
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
        } else {
            handleTouch(me);
        }

        return true;
    }

    private void checkGameEndConditions() {
        if (distanceBetweenBallAndHole() < 50) {
            timer.cancel();
        } else if (ballView.getY() > (holeView.getY() + holeView.getHeight())) {
            timer.cancel();
        }
    }

    private double distanceBetweenBallAndHole() {
        float ballX = ballView.getX();
        float ballY = ballView.getY();
        float holeX = holeView.getX();
        float holeY = holeView.getY();

        return Math.sqrt(Math.pow(ballX - holeX, 2) + Math.pow(ballY - holeY, 2));
    }


    private void handleTouch(MotionEvent me) {
    }
}
