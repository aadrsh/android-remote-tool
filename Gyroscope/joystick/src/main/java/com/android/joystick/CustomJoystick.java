package com.android.joystick;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class CustomJoystick extends FrameLayout implements View.OnTouchListener {

    ImageView base, center;
    View rootView;

    float scale = 1;

    float anchorX = 0;
    float anchorY = 0;

    float[] raw = new float[2];
    float[] down = new float[2];


    OnMovementListener listener = new OnMovementListener() {
        @Override
        public void onMovement(String action) {

        }
    };

    public CustomJoystick(Context context) {
        super(context);
        init(context, 0, 0, 1);
    }

    public CustomJoystick(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomJoystick);

        int x = a.getInt(R.styleable.CustomJoystick_setX, 0);
        int y = a.getInt(R.styleable.CustomJoystick_setY, 0);
        float scale = a.getFloat(R.styleable.CustomJoystick_Joystick_Scale, 1);

        a.recycle();
        init(context, x, y, scale);
    }


    private void init(Context context, int x, int y, float scale) {
        rootView = inflate(context, R.layout.mylayout, this);

        base = rootView.findViewById(R.id.com_android_joystick_base);
        center = rootView.findViewById(R.id.com_android_joystick_center);

        this.scale = scale;
        setXY(x, y);
        setHeightWidth(scale);

        rootView.setOnTouchListener(this);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        raw[0] = event.getRawX();
        raw[1] = event.getRawY();


        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            anchorX = center.getX();
            anchorY = center.getY();

            down[0] = raw[0];
            down[1] = raw[1];

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            float dx = raw[0] - down[0];
            float dy = raw[1] - down[1];


            double mag = Math.sqrt((dx * dx) + (dy * dy));

            movement(dx, dy,false);

            if (mag < scale * 100) {
                center.setX(dx + anchorX);
                center.setY(dy + anchorY);
            } else {
                //unit vector multiplied by 100
                center.setX(anchorX + (float) (scale * 100 * dx / mag));
                center.setY(anchorY + (float) (scale * 100 * dy / mag));
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            center.setY(anchorY);
            center.setX(anchorX);
            movement(0,0,true);
        }
        return true;
    }

    private void setXY(float x, float y) {
        center.setX(x);
        center.setY(y);

        base.setX(x);
        base.setY(y);
    }

    private void setHeightWidth(float scale) {

        base.setScaleX(scale);
        base.setScaleY(scale);
    }

    public abstract static class OnMovementListener {
        public abstract void onMovement(String action);
    }

    public void addOnMovementListener(OnMovementListener listener) {
        this.listener = listener;
    }

    private float prec(double value) {
        String str = String.format("%.4f", value);
        return Float.parseFloat(str);
    }

    void movement(float dx, float dy,boolean UP) {
        double degrees = Math.toDegrees(Math.atan2(dy, dx));
//        Log.w("Degrees", " D" + degrees + " D/30:" + (int) (degrees / 22.5));

        String ACTION="";

        switch ((int) (degrees / 22.5)) {
            case 0:
                ACTION="D";
                break;
            case -1:
            case -2:
                ACTION="DW";
                break;
            case -3:
            case -4:

                ACTION="W";
                break;
            case -5:
            case -6:
                ACTION="AW";
                break;
            case -7:
            case 7:
                ACTION="A";
                break;
            case 6:
            case 5:
                ACTION="AS";
                break;
            case 4:
            case 3:
                ACTION="S";
                break;
            case 2:
            case 1:
                ACTION="DS";
                break;
        }
        if(UP)
            ACTION="";

        listener.onMovement(ACTION);
    }

}
