package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.yifei.mobilesafe.R;

public abstract class BaseSetupActivity extends Activity {
    private GestureDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


                if ((e2.getRawX() - e1.getRawX()) > 200 && Math.abs(velocityX) > 100 &&
                        Math.abs(e2.getRawY() - e1.getRawY()) < 100) {
                    showPrevious();
                }


                if ((e1.getRawX() - e2.getRawX()) > 200 && Math.abs(velocityX) > 100 &&
                        Math.abs(e2.getRawY() - e1.getRawY()) < 100) {
                    showNext();
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public void next(View view) {
        showNext();

    }

    public void previous(View view) {
        showPrevious();

    }


    public abstract void showPrevious();

    public abstract void showNext();


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


}
