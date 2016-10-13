package edu.ut.softlab.rate.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by alex on 16-8-3.
 */
@Entity(name = "Device")
@Table(name = "device")
public class Device implements Serializable{
    private static final long serialVersionUID = 1L;
    public Device(){super();}

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "did", unique = true)
    private String did;

    @Column(name="last_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;

    @Column(name="device_uuid")
    private String deviceId;

    @Column(name="device_type")
    private String deviceType;

    @Column(name="login_token")
    private String loginToken;

    @Column(name="device_token")
    private String deviceToken;

    @Column(name="last_login_ip")
    private String lastLoginIp;

    @Column(name="os_version")
    private String osVersion;

    @Column(name="notify")
    private Boolean isNotify = true;

    @Column(name = "lan")
    private String lan = "en";

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public Boolean getIsNotify() {
        return isNotify;
    }

    public void setIsNotify(Boolean isNotify) {
        this.isNotify = isNotify;
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

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
