package com.example.ylmusic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class Viewpager extends ViewPager {
    private boolean isCanScroll = true;

    public Viewpager(Context context) {
        super(context);
    }

    public Viewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onTouchEvent(ev);

    }
}
