package com.example.yifei.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yifei.mobilesafe.R;

/**
 * Created by Yifei on 2015/8/5.
 *
 * 设置中心的自定义布局
 */
public class SettingClickView extends RelativeLayout {
    private TextView tvTitle;
    private TextView tvDesc;


    public SettingClickView(Context context) {
        super(context);
        initLayout();
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initLayout();

    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        initLayout();
    }



    private void initLayout() {

        View.inflate(getContext(), R.layout.view_setting_click, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);

    }

    public void setTitle(String title) {
        this.tvTitle.setText(title);
    }


    public void setDesc(String desc){
        this.tvDesc.setText(desc);
    }



}

