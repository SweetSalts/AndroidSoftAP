package com.tcl.a1.androidsoftap;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ysw";
    private static final int OK = 1;

    private Button menuBtn_switch;
    private Button menuBtn_IP;
    private Button menuBtn_speed;
    private Button menuBtn_device;
    private Switch btnSwitch;
    private LinearLayout apManage;

    private WifiConnect myWifi;
    private WifiManager mWifiManager;
    private static NodeList bookList;
    private long firstTime = 0;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case OK:
                    Toast.makeText(MainActivity.this, "程序初始化完毕！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 菜单按钮并绑定监听
        initButton();
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        myWifi = new WifiConnect(mWifiManager);
        replaceFragment(new SwitchFragment());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建一个DocumentBuilderFactory的对象
                    //创建一个DocumentBuilder的对象
                    //创建DocumentBuilder对象
                    //通过DocumentBuilder对象的parser方法加载books.xml文件到当前项目下
                    //获取所有book节点的集合
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document document = db.parse("file:/data/user/0/com.tcl.a1.androidsoftap/files/macaddress.xml");
                    bookList = document.getElementsByTagName("record");
                    Message message = new Message();
                    message.what = OK;
                    handler.sendMessage(message);
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 菜单按钮并绑定监听
    private void initButton() {
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
        switch (v.getId()) {
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
                        if (apConfig.preSharedKey == null) {
                            myWifi.stratWifiAp(apConfig.SSID, "null", "NONE");
                        } else {
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
                Toast.makeText(MainActivity.this, "点错了", Toast.LENGTH_LONG).show();
                break;
        }
    }

    // 替换Fragment
    public void replaceFragment(Fragment fragment) {
        Log.i(TAG, "replaceFragment: " + fragment.getClass());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        myWifi.closeWifiAp();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public static String getCoName(String mac) {
        //截取mac地址前三个地址
        mac = mac.substring(0, 8);
        mac = mac.toUpperCase();
        //通过nodelist的getLength()方法可以获取bookList的长度
        //遍历每一个book节点
        for (int i = 0; i < bookList.getLength(); i++) {
            //System.out.println("=================下面开始遍历第" + (i + 1) + "本书的内容=================");
            //通过 item(i)方法 获取一个book节点，nodelist的索引值从0开始
            Node book = bookList.item(i);
            //解析book节点的子节点
            NodeList childNodes = book.getChildNodes();
            //遍历childNodes获取每个节点的节点名和节点值
            //System.out.println("第" + (i+1) + "本书共有" + childNodes.getLength() + "个子节点");
            //如果查找了对应的mac地址，将公司名返回
            if (childNodes.item(1).getFirstChild().getNodeValue().substring(0, 8).equals(mac)) {
                String str = childNodes.item(5).getFirstChild().getNodeValue();
                if (str.indexOf(" ") != -1) {
                    str = str.substring(0, str.indexOf(" "));
                    if (str.indexOf(",") != -1)
                        return str.substring(0, str.indexOf(","));
                    else
                        return str;
                } else
                    return str;
            }
        }
        return "Unknown Device";
    }
}
