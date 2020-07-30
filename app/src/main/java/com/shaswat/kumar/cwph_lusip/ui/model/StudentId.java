package com.shaswat.kumar.cwph_lusip.ui.model;

public class StudentId {

    public StudentId(){

    }

    String id;

    String time;

    public StudentId(String id, String time) {
        this.id = id;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
