package com.tcl.a1.androidsoftap;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.tcl.a1.androidsoftap.fragment.DeviceFragment;
import com.tcl.a1.androidsoftap.fragment.IPFragment;
import com.tcl.a1.androidsoftap.fragment.SpeedFragment;
import com.tcl.a1.androidsoftap.fragment.SwitchFragment;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button menuBtn_switch;
    private Button menuBtn_IP;
    private Button menuBtn_speed;
    private Button menuBtn_device;
    private Switch btnSwitch;
    private LinearLayout apManage;

    private WifiConnect myWifi;
    private WifiManager mWifiManager;

    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 菜单按钮并绑定监听
        initButton();
        mWifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        myWifi = new WifiConnect(mWifiManager);
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
        btnSwitch = findViewById(R.id.btnSwitch_swiFra);
        btnSwitch.setOnClickListener(this);
        apManage = findViewById(R.id.ap_manage);
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
            case R.id.btnSwitch_swiFra:
                if (btnSwitch.isChecked()) {
                    if (mWifiManager.isWifiEnabled()) {
                        mWifiManager.setWifiEnabled(false);
                    }
                    try {
                        Method method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
                        WifiConfiguration apConfig = (WifiConfiguration) method.invoke(mWifiManager);
                        if(apConfig.preSharedKey == null){
                            myWifi.stratWifiAp(apConfig.SSID,"null" , "NONE");
                        }
                        else{
                            myWifi.stratWifiAp(apConfig.SSID, apConfig.preSharedKey, "WPA_PSK");
                        }
                        btnSwitch.setText(apConfig.SSID);
                        apManage.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "开启热点成功", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    myWifi.closeWifiAp();
                    btnSwitch.setText("打开/关闭热点");
                    apManage.setVisibility(View.GONE);
                }
                break;
            default:
                Toast.makeText(MainActivity.this,"点错了",Toast.LENGTH_LONG).show();
                break;
        }
    }

    // 替换Fragment
    public void replaceFragment(Fragment fragment){
        Log.i(TAG, "replaceFragment: "+fragment.getClass());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_layout,fragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myWifi.closeWifiAp();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
