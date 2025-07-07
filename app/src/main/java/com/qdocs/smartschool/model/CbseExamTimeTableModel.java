package com.qdocs.smartschool.model;

import java.util.ArrayList;

public class CbseExamTimeTableModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CbseTimetableModel> getTime_table() {
        return time_table;
    }

    public void setTime_table(ArrayList<CbseTimetableModel> time_table) {
        this.time_table = time_table;
    }

    ArrayList<CbseTimetableModel> time_table = new ArrayList<>();


}
