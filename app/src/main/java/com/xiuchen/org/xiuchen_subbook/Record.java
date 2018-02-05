package com.xiuchen.org.xiuchen_subbook;

import java.io.Serializable;
import java.util.Date;

public class Record implements Serializable {

    Integer id;
    String name;
    Double charge;
    Date date;
    String comments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", charge=" + charge +
                ", date=" + date +
                ", comments='" + comments + '\'' +
                '}';
    }
}
