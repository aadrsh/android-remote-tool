package com.android.gyroscope.classes;

import android.view.MotionEvent;

public abstract class OnActionChangeListener {
    public abstract void onActionDown(MotionEvent event);

    public abstract void onActionUp(MotionEvent event);

    public abstract void onActionMove(MotionEvent event, float deltaX, float deltaY);

    public static OnActionChangeListener getEmptyInstance(){
        return new OnActionChangeListener() {
            @Override
            public void onActionDown(MotionEvent event) {

            }

            @Override
            public void onActionUp(MotionEvent event) {

            }

            @Override
            public void onActionMove(MotionEvent event, float deltaX, float deltaY) {

            }
        };
    }
}