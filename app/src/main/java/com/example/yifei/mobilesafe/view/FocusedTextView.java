package com.example.yifei.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Yifei on 2015/8/4.
 */
public class FocusedTextView extends TextView{


    //代码中new实例化时调用
    public FocusedTextView(Context context) {
        super(context);
    }

    //有样式的时候调用此方法
    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //有属性时调用此方法
    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /*
    * 强制获取焦点
    * */
    @Override
    public boolean isFocused() {
        return true;
    }
}
