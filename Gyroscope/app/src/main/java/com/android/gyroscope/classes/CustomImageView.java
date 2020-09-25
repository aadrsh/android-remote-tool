package com.android.gyroscope.classes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class CustomImageView extends androidx.appcompat.widget.AppCompatImageView implements View.OnTouchListener {
    private OnActionChangeListener action;
    private boolean isPressed = false;

    private float[] delta = new float[3];
    private float[] down = new float[3];
    private float[] raw = new float[3];


    public CustomImageView(Context context) {
        super(context);
        init();
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){

        action = OnActionChangeListener.getEmptyInstance();
        this.setOnTouchListener(this);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        Toast.makeText(getContext(), "Move device to adjust while holding reset", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        raw[0] = event.getRawX();
        raw[1] = event.getRawY();

        if (event.getAction() == MotionEvent.ACTION_DOWN && !isPressed) {
            isPressed = true;
            down[0] = raw[0];
            down[1] = raw[1];

            action.onActionDown(event);

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            delta[0] = raw[0] - down[0];
            delta[1] = down[1]-raw[1];

            down[0] = raw[0];
            down[1] = raw[1];

            action.onActionMove(event, delta[0], delta[1]);
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {

            down[0] = 0;
            down[1] = 0;

            delta[0] = 0;
            delta[1] = 0;

            isPressed = false;
            action.onActionUp(event);
        }
        return true;
    }



    public void setOnActionChangeListener(OnActionChangeListener action) {
        this.action = action;
    }
}
