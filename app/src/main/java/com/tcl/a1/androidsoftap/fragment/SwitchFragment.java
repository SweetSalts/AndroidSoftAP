package com.tcl.a1.androidsoftap.fragment;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.animation.LogDecelerateInterpolator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.tcl.a1.androidsoftap.R;
import com.tcl.a1.androidsoftap.WifiConnect;


public class SwitchFragment extends Fragment {
    private static final String TAG = "ysw";
    private EditText ssid_text;
    private EditText password_text;
    private TextView passwordLable;
    private Spinner encryptionType;
    private Button btnSure;
    private Button btnCancel;
    private Switch btnSwitch;
    private WifiConnect myWifi;
    private WifiManager mWifiManager;


    //默认账户名密码和加密方式
    private String apName = "a1TestAp";
    private String apPassword = "12345678";
    private String  encryptionMethod = "WPA_PSK";
    private String[] encryptionTypeList;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_switch, container, false);
        ssid_text = view.findViewById(R.id.ssid_swiFra);
        password_text = view.findViewById(R.id.password_swiFra);
        encryptionType = view.findViewById(R.id.encryptionType_swiFra);
        btnSure = view.findViewById(R.id.sure_swiFra);
        btnCancel = view.findViewById(R.id.btnCancel_swiFra);
        btnSwitch = getActivity().findViewById(R.id.btnSwitch_swiFra);
        passwordLable = view.findViewById(R.id.password_lable);
        mWifiManager = (WifiManager) getActivity()
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        myWifi = new WifiConnect(mWifiManager);
        encryptionTypeList = getResources().getStringArray(R.array.encryptiontypeArr);
        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_expandable_list_item_1, encryptionTypeList);
        encryptionType.setAdapter(adapter);
        ssid_text.setText(apName);
        password_text.setText(apPassword);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 确认按钮的点击事件
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apName = ssid_text.getText().toString();
                apPassword = password_text.getText().toString();

                if (!"".equals(apName) && (!"".equals(apPassword) ||
                        encryptionMethod.equals("NONE"))) {
                    if (mWifiManager.isWifiEnabled()) {
                        mWifiManager.setWifiEnabled(false);
                    }
                    myWifi.closeWifiAp();
                    myWifi.stratWifiAp(apName, apPassword, encryptionMethod);
                    Log.d(TAG, "onClick: " + encryptionMethod);
                    btnSwitch.setText(apName);
                    Toast.makeText(getActivity(), "开启热点成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "热点名和密码不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // 取消按钮
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                ssid_text.setText("");
                password_text.setText("");
            }
        });

        encryptionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                encryptionMethod = encryptionType.getItemAtPosition(i).toString();
                if(encryptionMethod.equals("NONE")){
                    password_text.setVisibility(View.GONE);
                    passwordLable.setVisibility(View.GONE);
                }else {
                    if(password_text.getVisibility() == View.GONE){
                        password_text.setVisibility(View.VISIBLE);
                        passwordLable.setVisibility(View.VISIBLE);
                    }
                }
                Log.d(TAG, encryptionMethod);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myWifi.closeWifiAp();
    }

}
