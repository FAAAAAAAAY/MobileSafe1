package com.example.yifei.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yifei.mobilesafe.R;

/**
 * Created by Yifei on 2015/8/5.
 *
 * 设置中心的自定义布局
 */
public class SettingItemView extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private String mDescOff;
    private String mDescOn;
    private String mTitle;
    private TextView tvTitle;
    private TextView tvDesc;
    private CheckBox cbStatus;

    public SettingItemView(Context context) {
        super(context);
        initLayout();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTitle = attrs.getAttributeValue(NAMESPACE,"title");
        mDescOn = attrs.getAttributeValue(NAMESPACE,"desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE,"desc_off");
        initLayout();

    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTitle = attrs.getAttributeValue(NAMESPACE,"title");
        mDescOn = attrs.getAttributeValue(NAMESPACE,"desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE,"desc_off");

        initLayout();
    }



    private void initLayout() {

        View.inflate(getContext(), R.layout.view_setting_item, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        cbStatus = (CheckBox) findViewById(R.id.cb_status);
        setTitle();
    }

    public void setTitle() {
        this.tvTitle.setText(mTitle);
    }


    public void setDesc(String desc){
        this.tvDesc.setText(desc);
    }

    public boolean isChecked(){
        return this.cbStatus.isChecked();
    }

    public void setChecked(boolean isChecked){
        this.cbStatus.setChecked(isChecked);
        if (isChecked){
            setDesc(mDescOn);
        }else{
            setDesc(mDescOff);
        }
    }

}

