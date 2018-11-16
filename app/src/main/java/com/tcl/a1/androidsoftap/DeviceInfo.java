package com.tcl.a1.androidsoftap;

public class DeviceInfo {
    private String mac;
    private String ip;
    private String status;

    public DeviceInfo(String mac, String ip, String status) {
        this.mac = mac;
        this.ip = ip;
        this.status = status;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
