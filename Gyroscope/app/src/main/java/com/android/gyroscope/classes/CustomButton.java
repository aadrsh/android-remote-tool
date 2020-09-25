package com.android.gyroscope.classes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CustomButton extends androidx.appcompat.widget.AppCompatImageButton implements View.OnTouchListener {

    OnActionChangeListener listener;

    public CustomButton(Context context) {
        super(context);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        listener=OnActionChangeListener.getEmptyInstance();
        this.setOnTouchListener(this);
    }

    public void addOnActionChangeListener(OnActionChangeListener listener){
        this.listener=listener;
    }

    @Override
    public boolean performClick(){
        super.performClick();
        Toast.makeText(getContext(), "Move device to adjust while holding reset", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int ACTION=event.getAction();

        switch (ACTION){
            case MotionEvent.ACTION_DOWN:
                listener.onActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                listener.onActionMove(event,event.getX(),getY());
                break;
            case MotionEvent.ACTION_UP:
                listener.onActionUp(event);
                break;
            default:
                listener.onActionUp(event);
        }

        return true;
    }


}
