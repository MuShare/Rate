package edu.ut.softlab.rate.bean;

import edu.ut.softlab.rate.model.Device;


import java.util.Date;

/**
 * Created by alex on 16-8-3.
 */
public class DeviceBean {
    private String did;
    private Date lastLoginTime;
    private String deviceId;
    private String deviceType;
    private String loginToken;
    private String pushToken;
    private String lastLoginIp;
    private String osVersion;
    private UserBean userBean;

    public DeviceBean(Device device){
        this.did = device.getDid();
        this.lastLoginTime = device.getLastLoginTime();
        this.deviceId = device.getDeviceId();
        this.deviceType = device.getDeviceType();
        this.loginToken = device.getLoginToken();
        this.pushToken = device.getDeviceToken();
        this.lastLoginIp = device.getLastLoginIp();
        this.osVersion = device.getOsVersion();
        this.userBean = new UserBean(device.getUser());
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
