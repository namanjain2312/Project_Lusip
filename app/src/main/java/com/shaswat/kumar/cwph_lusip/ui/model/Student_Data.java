package com.shaswat.kumar.cwph_lusip.ui.model;

public class Student_Data {

    public Student_Data() {
    }

    String audio;
    String id;
    String message;
    String time;

    public Student_Data(String audio, String id, String message, String time) {
        this.audio = audio;
        this.id = id;
        this.message = message;
        this.time = time;
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
}


