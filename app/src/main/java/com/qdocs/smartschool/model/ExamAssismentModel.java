package com.qdocs.smartschool.model;

import java.util.ArrayList;

public class ExamAssismentModel {

    public String getCbse_exam_assessment_type_name() {
        return cbse_exam_assessment_type_name;
    }

    public void setCbse_exam_assessment_type_name(String cbse_exam_assessment_type_name) {
        this.cbse_exam_assessment_type_name = cbse_exam_assessment_type_name;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    private String cbse_exam_assessment_type_name;
    private String marks;




    public String getIs_absent() {
        return is_absent;
    }

    public void setIs_absent(String is_absent) {
        this.is_absent = is_absent;
    }

    private String is_absent;


}
