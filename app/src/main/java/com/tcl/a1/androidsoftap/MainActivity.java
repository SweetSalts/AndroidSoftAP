package com.tcl.a1.androidsoftap;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tcl.a1.androidsoftap.fragment.DeviceFragment;
import com.tcl.a1.androidsoftap.fragment.IPFragment;
import com.tcl.a1.androidsoftap.fragment.SpeedFragment;
import com.tcl.a1.androidsoftap.fragment.SwitchFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button menuBtn_switch;
    private Button menuBtn_IP;
    private Button menuBtn_speed;
    private Button menuBtn_device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 菜单按钮并绑定监听
        initButton();
        replaceFragment(new SwitchFragment());
    }

    // 菜单按钮并绑定监听
    private void initButton(){
        Log.i(TAG, "initButton: init menu button");
        menuBtn_switch = findViewById(R.id.menuBtn_switch);
        menuBtn_switch.setOnClickListener(this);
        menuBtn_IP = findViewById(R.id.menuBtn_IP);
        menuBtn_IP.setOnClickListener(this);
        menuBtn_speed = findViewById(R.id.menuBtn_speed);
        menuBtn_speed.setOnClickListener(this);
        menuBtn_device = findViewById(R.id.menuBtn_device);
        menuBtn_device.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 点击事件
        switch (v.getId()){
            case R.id.menuBtn_switch:
                Log.i(TAG, "onClick: menuBtn_switch");
                replaceFragment(new SwitchFragment());
                break;
            case R.id.menuBtn_IP:
                Log.i(TAG, "onClick: menuBtn_IP");
                replaceFragment(new IPFragment());
                break;
            case R.id.menuBtn_speed:
                Log.i(TAG, "onClick: menuBtn_speed");
                replaceFragment(new SpeedFragment());
                break;
            case R.id.menuBtn_device:
                Log.i(TAG, "onClick: menuBtn_device");
                replaceFragment(new DeviceFragment());
                break;
            default:
                Toast.makeText(MainActivity.this,"123",Toast.LENGTH_LONG).show();
                break;
        }
    }

    // 替换Fragment
    private void replaceFragment(Fragment fragment){
        Log.i(TAG, "replaceFragment: "+fragment.getClass());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


/*
    // 连续两次点击返回键退出程序
    private long mExitTime;
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出每日新闻", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }*/

}
