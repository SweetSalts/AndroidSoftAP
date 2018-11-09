package com.tcl.a1.androidsoftap.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.tcl.a1.androidsoftap.R;


public class SwitchFragment extends Fragment {
    private static final String TAG = "SwitchFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_switch, container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Switch btnSwitch = getActivity().findViewById(R.id.btnSwitch_swiFra);
        //TextView netStatesText = getActivity().findViewById(R.id.netStatesText_swiFra);

        // 设置监听
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(),"switch",Toast.LENGTH_SHORT).show();
                if (btnSwitch.isChecked()){
                    // Toast.makeText(getActivity(),"switch on",Toast.LENGTH_SHORT).show();
                    // TODO: 打开状态下
                } else{
                    // Toast.makeText(getActivity(),"switch off",Toast.LENGTH_SHORT).show();
                    // TODO：关闭状态下
                }
            }
        });

        final EditText ssid_ext = getActivity().findViewById(R.id.ssid_swiFra);
        final EditText password_ext = getActivity().findViewById(R.id.password_swiFra);
        final Spinner encryptionType_spinner = getActivity().findViewById(R.id.encryptionType_swiFra);
        // 加密方式下拉框的选择监听
       /* encryptionType_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 String cardNumber = getActivity().getResources().getStringArray(R.array.encryptiontypeArr)[position];
                Toast.makeText(getActivity(),cardNumber,Toast.LENGTH_SHORT).show();
            }
        });*/
        final Button btnSure = getActivity().findViewById(R.id.sure_swiFra);
        // 确认按钮的点击事件
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                String ssid = ssid_ext.getText().toString();
                String password = password_ext.getText().toString();
                String encryptionType = encryptionType_spinner.toString();
                Log.i(TAG, "onClick: "+ssid+" "+password+" "+encryptionType);
            }
        });
        final Button btnCancel = getActivity().findViewById(R.id.btnCancel_swiFra);
        // 取消按钮
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                ssid_ext.setText("");
                password_ext.setText("");
            }
        });
    }
}
