package edu.ut.softlab.rate.bean;

import org.directwebremoting.annotations.DataTransferObject;

import java.util.Date;

/**
 * Created by alex on 16-5-6.
 */
@DataTransferObject
public class UserBean {
    private String uid;
    private String sname;
    private String email;
    private String password;

    public UserBean(String uid, String sname, String email, String password){
        this.uid = uid;
        this.sname = sname;
        this.email = email;
        this.password = password;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
