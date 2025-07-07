package com.qdocs.smartschool.model;

import java.util.ArrayList;

public class CbseExamModel {

    private String name;
    private String exam_total_marks;
    private String exam_obtain_marks;
    private String exam_percentage;

    public String getExam_rank() {
        return exam_rank;
    }

    public void setExam_rank(String exam_rank) {
        this.exam_rank = exam_rank;
    }

    private String exam_rank;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExam_total_marks() {
        return exam_total_marks;
    }

    public void setExam_total_marks(String exam_total_marks) {
        this.exam_total_marks = exam_total_marks;
    }

    public String getExam_obtain_marks() {
        return exam_obtain_marks;
    }

    public void setExam_obtain_marks(String exam_obtain_marks) {
        this.exam_obtain_marks = exam_obtain_marks;
    }

    public String getExam_percentage() {
        return exam_percentage;
    }

    public void setExam_percentage(String exam_percentage) {
        this.exam_percentage = exam_percentage;
    }

    public String getExam_grade() {
        return exam_grade;
    }

    public void setExam_grade(String exam_grade) {
        this.exam_grade = exam_grade;
    }

    private String exam_grade;


    public ArrayList<SubjectModel> getSubject() {
        return subject;
    }

    public void setSubject(ArrayList<SubjectModel> subject) {
        this.subject = subject;
    }

    ArrayList<SubjectModel> subject = new ArrayList<>();

    public ArrayList<AssismentTypeModel> getAssismenttype() {
        return assismenttype;
    }

    public void setAssismenttype(ArrayList<AssismentTypeModel> assismenttype) {
        this.assismenttype = assismenttype;
    }

    ArrayList<AssismentTypeModel> assismenttype = new ArrayList<>();


}
