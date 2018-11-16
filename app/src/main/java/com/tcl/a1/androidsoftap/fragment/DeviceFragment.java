package com.tcl.a1.androidsoftap.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.tcl.a1.androidsoftap.DeviceInfo;
import com.tcl.a1.androidsoftap.MainActivity;
import com.tcl.a1.androidsoftap.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DeviceFragment extends Fragment {

    private static final String TAG = "ysw";
    private ListView device_list;
    private DeviceAdapter adapter;
    private ArrayList<DeviceInfo> deviceInfoList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        device_list = view.findViewById(R.id.device_view);
        deviceInfoList = getDeviceInfo();
        adapter = new DeviceAdapter(getContext(),R.layout.device_item, deviceInfoList);
        device_list.setAdapter(adapter);
        refreshDeviceList();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void refreshDeviceList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
        }).start();
    }

    private ArrayList getDeviceInfo() {
        ArrayList<DeviceInfo> deviceInfoList = new ArrayList<>();
        try {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader("/proc/net/arp"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String line = "";
            String ip = "";
            String status = "";
            String mac = "";

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() < 63) continue;
                if (line.toUpperCase(Locale.US).contains("IP")) continue;
                ip = line.substring(0, 17).trim();
                status = line.substring(29, 32).trim();
                mac = line.substring(41, 63).trim();
                if (mac.contains("00:00:00:00:00:00")) continue;
                if(!ip.substring(8, 10).equals("43")) continue;
                if(status.equals("0x2")){
                    DeviceInfo info = new DeviceInfo(mac, ip, status);
                    deviceInfoList.add(info);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deviceInfoList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class ViewHolder{
        TextView ip_text;
        TextView mac_text;
        TextView status_text;
        LinearLayout device_info;
    }

    public class DeviceAdapter extends ArrayAdapter<DeviceInfo>{

        private int resourceId;

        public DeviceAdapter(Context context, int resource, List<DeviceInfo> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DeviceInfo deviceInfo = getItem(position);
            ViewHolder viewHolder;
            View view;
            if (convertView == null){
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.ip_text = view.findViewById(R.id.ip_address);
                viewHolder.mac_text = view.findViewById(R.id.mac_address);
                viewHolder.status_text = view.findViewById(R.id.status);
                viewHolder.device_info = view.findViewById(R.id.device_info);
                view.setTag(viewHolder);
            }else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.ip_text.setText(deviceInfo.getIp());
            viewHolder.mac_text.setText(deviceInfo.getMac());
            if(deviceInfo.getStatus().equals("0x2")){
                viewHolder.status_text.setText("已连接");
            }else{
                viewHolder.status_text.setText("未连接");
            }
            return view;
        }
    }
}
