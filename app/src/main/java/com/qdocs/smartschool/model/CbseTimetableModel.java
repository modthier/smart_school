package com.qdocs.smartschool.model;

import java.util.ArrayList;

public class CbseTimetableModel {



    private String date;
    private String time_from;
    private String duration;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime_from() {
        return time_from;
    }

    public void setTime_from(String time_from) {
        this.time_from = time_from;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    private String room_no;
    private String subject_name;


    public ArrayList<SubjectModel> getTime_table() {
        return time_table;
    }

    public void setTime_table(ArrayList<SubjectModel> time_table) {
        this.time_table = time_table;
    }

    ArrayList<SubjectModel> time_table = new ArrayList<>();





}
