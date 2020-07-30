package com.shaswat.kumar.cwph_lusip.ui.model;

public class Teacher_comment {

    public Teacher_comment() {
    }

    String audio;
    String id;
    String message;
    String time;
    String comId;

    public Teacher_comment(String audio, String id, String message, String time,String comId) {
        this.audio = audio;
        this.id = id;
        this.message = message;
        this.time = time;
        this.comId = comId;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }
}
