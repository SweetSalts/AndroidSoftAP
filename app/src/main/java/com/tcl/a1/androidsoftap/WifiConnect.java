package com.tcl.a1.androidsoftap;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * wifi 连接和创建AP的类
 * @author bobo
 *
 */
public class WifiConnect {

	private WifiManager wifiManager;


	// 构造函数
	public WifiConnect(WifiManager wifiManager) {
		this.wifiManager = wifiManager;
	}

	/**
	 * 开启wifi AP
	 * @param mSSID
	 * @param mPasswd
	 */
	public void stratWifiAp(String mSSID, String mPasswd, String authAlogrithm) {
		Method method1 = null;
		try {
			method1 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
			WifiConfiguration netConfig = new WifiConfiguration();
			netConfig.SSID = mSSID;

			switch (authAlogrithm){
				case "NONE":
					netConfig.allowedAuthAlgorithms.set(WifiConfiguration.KeyMgmt.NONE);
					break;
				case "WPA_PSK":
					netConfig.preSharedKey = mPasswd;
					netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
					netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
					netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
					netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
					netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
					netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
					netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
					netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
					break;
			}
			method1.invoke(wifiManager, netConfig, true);
			Log.d("ysw","热点配置成功");

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block  
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block  
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block  
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭wifi AP共享
	 */
	public void closeWifiAp() {
		//if (wifiManager.isWifiEnabled())
		{
			try {
				Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
				method.setAccessible(true);
				WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
				Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class,
						boolean.class);
				method2.invoke(wifiManager, config, false);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block  
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block  
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block  
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block  
				e.printStackTrace();
			}
		}
	}

}
