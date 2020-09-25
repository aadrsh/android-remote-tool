package com.android.gyroscope.unusable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.TextView;

import com.android.gyroscope.R;

public class MainActivity extends AppCompatActivity implements SensorEventListener , View.OnClickListener {
TextView tv1;
Button btn_rest,btn_next;
AbsoluteLayout absoluteLayout;
double currentX=0,currentY=0,currentZ=0;
float objectX=0,objectY=0,objectZ=0;
    SensorManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1=findViewById(R.id.tv1);
        btn_rest =findViewById(R.id.reset);
        btn_next=findViewById(R.id.touch);

        absoluteLayout =findViewById(R.id.layout);

        objectX=tv1.getX();
        objectY=tv1.getY();


        ApplicationInfo applicationInfo=null;
        try{
            applicationInfo=getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        Log.w("DIR",applicationInfo.packageName+" ");


        btn_rest.setOnClickListener(this);

        sm=(SensorManager) getSystemService(SENSOR_SERVICE);
        assert sm != null;
        sm.registerListener(this,sm.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR),SensorManager.SENSOR_DELAY_UI);

        btn_next.setOnClickListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        getGyro(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void getGyro(SensorEvent se){
        float [] values=se.values;

        double x= Math.toDegrees(values[0]);
        double y=Math.toDegrees(values[1]);
        double z=Math.toDegrees(values[2]);

        objectX+=(currentX-x)*15;
        objectY+=(currentY-y)*15;
        objectZ+=(currentZ-z)*15;

//        Log.w("SENSOR",currentX+"   "+objectX+"  "+x);

        tv1.setX(objectX);
        tv1.setY(objectY);
//        tv1.setZ(objectZ);

        currentX=x;
        currentY=y;
        currentZ=z;

    }

    void reset(){
        objectX= 640;
        objectY= 200;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.touch:
            startActivity(new Intent(MainActivity.this, TouchActivity.class));
            sm.unregisterListener(this);
            finish();


            break;
            case R.id.reset:
                reset();
                break;
        }
        }
}
