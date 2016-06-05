package edu.ut.softlab.rate.model;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by alex on 16-4-11.
 */
@Entity(name = "Currency")
@Table(name = "currency")
@DataTransferObject
public class Currency implements Serializable {
    private static final long serialVersionUID = 1L;
    public Currency(){super();}

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "cid", unique = true)
    private String cid;

    @Column(name = "code", length = 5)
    private String code;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
