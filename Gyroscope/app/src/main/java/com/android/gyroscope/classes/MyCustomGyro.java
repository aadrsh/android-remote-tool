package com.android.gyroscope.classes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.content.Context.SENSOR_SERVICE;

public class MyCustomGyro implements SensorEventListener {


    private static float pastX = 0, pastY = 0, pastZ = 0;
    private static float objectX = 0, objectY = 0, objectZ = 0;
    private GyroEventListener listener;


    public MyCustomGyro(Context context){
        SensorManager sm;
        sm = (SensorManager) context.getSystemService(SENSOR_SERVICE);

        listener=new GyroEventListener() {
            @Override
            public void onChangeRotation(float x, float y) {

            }
        };

        assert sm != null;
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        getGyro(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void getGyro(SensorEvent se) {
        float[] values = new float[3];
        System.arraycopy(se.values, 0, values, 0, 3);

        float x = precision(Math.toDegrees(values[0]));
        float y = precision(Math.toDegrees(values[1]));
        float z = precision(Math.toDegrees(values[2]));

//        Log.e("XYZ", x + " " + y + " " + z);


        //
        if (x > 0.5f || x < -0.5f)
            objectX += (pastX - x);
        else objectX = 0;

        if (y > 0.5f || y < -0.5f)
            objectY += (pastY - y);
        else objectY = 0;

        objectZ += (pastZ - z);


//        Add listener method here
        if(objectX!=0 && objectY!=0)
        listener.onChangeRotation(objectX,objectY);

        pastX = x * 1;
        pastY = y * 1;
        pastZ = z * 1;

    }

    public void addGyroEventListener(GyroEventListener listener){
        this.listener=listener;
    }

    private float precision(double value) {
        String str = String.format("%.4f", value);
        return Float.parseFloat(str);
    }

    public abstract static class GyroEventListener{
       public abstract void onChangeRotation(float x,float y);
    }

}
