package com.android.gyroscope.unusable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.core.view.InputDeviceCompat;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.gyroscope.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;

public class TouchActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);

        Button up=findViewById(R.id.touchup);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTapTap();
            }
        });




//        new CountDownTimer(4000, 2000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                try {
//                    Touch touch=new Touch();
//                    touch.injectMotionEvent(InputDeviceCompat.SOURCE_TOUCHSCREEN,MotionEvent.ACTION_DOWN,SystemClock.uptimeMillis(),85,175,1.0f);
//                    Toast.makeText(TouchActivity.this, "Done", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                try {
//                    Touch touch=new Touch();
//                    touch.injectMotionEvent(InputDeviceCompat.SOURCE_TOUCHSCREEN,MotionEvent.ACTION_MOVE,SystemClock.uptimeMillis(),100,225,1.0f);
//                    Toast.makeText(TouchActivity.this, "Done", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();
//


    }

    void startTapTap(){

        ApplicationInfo applicationInfo=null;
        try{
            applicationInfo=getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String packageStr=applicationInfo.sourceDir;
        String commandToExec="";
        commandToExec="su -c \"CLASSPATH="+packageStr+" /system/bin/app_process32 /system/bin com.android.gyroscope.unusable.TapTap\"\n";
        try {

            Process process = Runtime.getRuntime().exec("su");
            Log.w("CMD",commandToExec);
            DataOutputStream outputStream=new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes(commandToExec);
            outputStream.flush();
            Toast.makeText(this, commandToExec, Toast.LENGTH_LONG).show();
            //os.writeBytes("exit\n");
            //os.flush();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
