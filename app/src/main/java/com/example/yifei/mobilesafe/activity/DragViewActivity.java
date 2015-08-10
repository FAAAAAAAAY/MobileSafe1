package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yifei.mobilesafe.R;

public class DragViewActivity extends Activity {
    private TextView tvTop;
    private TextView tvBottom;
    private ImageView ivDrag;
    private int startX;
    private int startY;
    private SharedPreferences mPref;
    long[]mHits = new long[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);
        mPref = getSharedPreferences("config",MODE_PRIVATE);
        tvTop = (TextView) findViewById(R.id.tv_top);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);
        ivDrag = (ImageView) findViewById(R.id.iv_drag);

        int top = mPref.getInt("dragTop",0);
        int left = mPref.getInt("dragLeft",0);

        final int winWidth = getWindowManager().getDefaultDisplay().getWidth();
        final int winHeight = getWindowManager().getDefaultDisplay().getHeight();

        /*ivDrag.layout(left,top,ivDrag.getWidth() + left,ivDrag.getHeight() + top);*/



        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();
        layoutParams.leftMargin = left;
        layoutParams.topMargin = top;
        ivDrag.setLayoutParams(layoutParams);

        if (top>winHeight/2){
            tvTop.setVisibility(View.VISIBLE);
            tvBottom.setVisibility(View.INVISIBLE);
        }else {
            tvTop.setVisibility(View.INVISIBLE);
            tvBottom.setVisibility(View.VISIBLE);
        }

        ivDrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.arraycopy(mHits, 1,mHits,0,mHits.length -1);
                mHits[mHits.length -1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)){
                    ivDrag.layout(winWidth/2 - ivDrag.getWidth()/2,ivDrag.getTop(),winWidth/2 + ivDrag.getWidth()/2,ivDrag.getBottom());
                }
            }
        });

        ivDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();
                        int dx = endX - startX;
                        int dy = endY - startY;
                        int l = ivDrag.getLeft() + dx;
                        int r = ivDrag.getRight() + dx;
                        int t = ivDrag.getTop() + dy;
                        int b = ivDrag.getBottom() + dy;

                        if (l < 0 || r > winWidth || t < 0 || b > winHeight) {
                            break;
                        }

                        if (t > winHeight / 2) {
                            tvTop.setVisibility(View.VISIBLE);
                            tvBottom.setVisibility(View.INVISIBLE);
                        } else {
                            tvTop.setVisibility(View.INVISIBLE);
                            tvBottom.setVisibility(View.VISIBLE);
                        }

                        ivDrag.layout(l, t, r, b);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        mPref.edit().putInt("dragTop", ivDrag.getTop()).apply();
                        mPref.edit().putInt("dragLeft", ivDrag.getLeft()).apply();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }


}
