package com.qdocs.smartschool.model;

import java.util.ArrayList;

public class SubjectModel {
    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    String subject_name;
  String subject_code;

    public String getTotalmarks() {
        return totalmarks;
    }

    public void setTotalmarks(String totalmarks) {
        this.totalmarks = totalmarks;
    }

    String totalmarks;


    public ArrayList<ExamAssismentModel> getAssisment() {
        return assisment;
    }

    public void setAssisment(ArrayList<ExamAssismentModel> assisment) {
        this.assisment = assisment;
    }

    ArrayList<ExamAssismentModel> assisment = new ArrayList<>();

}
