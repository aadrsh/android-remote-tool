package com.android.gyroscope;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.os.Bundle;

import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.gyroscope.classes.CustomButton;
import com.android.gyroscope.classes.CustomImageView;
import com.android.gyroscope.classes.MyCustomGyro;
import com.android.gyroscope.classes.MySocket;
import com.android.gyroscope.classes.OnActionChangeListener;
import com.android.joystick.CustomJoystick;

import java.io.IOException;

public class SocketActivity extends AppCompatActivity {

    Thread myThread;
    TextView ip_tv, status_tv;
    MySocket mySocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        status_tv = findViewById(R.id.status_tv);
        ip_tv = findViewById(R.id.ip_address_tv);
        ip_tv.setText(MySocket.getIPAddress(true));

        mySocket = new MySocket(this, status_tv);
        mySocket.connect();

        initImageView();
        initGyro();
        initJoystick();
        initFireButton();
        initScopeButton();
        initJumpButton();
        initProneButton();
        initCrouchButton();
    }


    @Override
    protected void onDestroy() {
        mySocket.close();
        super.onDestroy();
    }


    public void initGyro() {
        MyCustomGyro gyro = new MyCustomGyro(this);
        gyro.addGyroEventListener(new MyCustomGyro.GyroEventListener() {
            @Override
            public void onChangeRotation(float x, float y) {
                mySocket.sendData("MO:MOVE:" + x + "," + y);
            }
        });
    }

    public void initImageView() {
        CustomImageView imageView = findViewById(R.id.swipe_view);
        imageView.setOnActionChangeListener(new OnActionChangeListener() {
            @Override
            public void onActionDown(MotionEvent event) {
                //No user
            }

            @Override
            public void onActionUp(MotionEvent event) {
                //No use
            }

            @Override
            public void onActionMove(MotionEvent event, float deltaX, float deltaY) {
                    mySocket.sendData("MO:MOVE:" + deltaX + "," + deltaY);
            }
        });
    }

    public void initJoystick() {
        CustomJoystick cj = findViewById(R.id.my_joystick);
        cj.addOnMovementListener(new CustomJoystick.OnMovementListener() {
            @Override
            public void onMovement(String action) {
                mySocket.sendData("JS"+":MOVE:"+action);
            }
        });
    }

    private void initFireButton(){
        CustomButton imageButton=findViewById(R.id.fire_button);
        imageButton.addOnActionChangeListener(getListener("MO","LEFT"));
    }

    private void initScopeButton(){
        CustomButton scope=findViewById(R.id.scope_button);
        scope.addOnActionChangeListener(getListener("MO","RIGHT"));
    }

    private void initJumpButton(){
        CustomButton scope=findViewById(R.id.jump_button);
        scope.addOnActionChangeListener(getListener("JS"," "));
    }
    private void initProneButton(){
        CustomButton scope=findViewById(R.id.prone_button);
        scope.addOnActionChangeListener(getListener("JS","Z"));
    }
    private void initCrouchButton(){
        CustomButton scope=findViewById(R.id.crouch_button);
        scope.addOnActionChangeListener(getListener("JS","C"));
    }


    OnActionChangeListener getListener(final String device, final String keypress){
        return new OnActionChangeListener() {
            @Override
            public void onActionDown(MotionEvent event) {
                mySocket.sendData(device+":PRES:"+keypress);
            }

            @Override
            public void onActionUp(MotionEvent event) {
                mySocket.sendData(device+":RLSE:"+keypress);
            }

            @Override
            public void onActionMove(MotionEvent event, float deltaX, float deltaY) {

            }
        };
    }

}