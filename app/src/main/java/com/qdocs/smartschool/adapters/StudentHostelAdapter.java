package com.qdocs.smartschool.adapters;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.students.StudentHostel;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StudentHostelAdapter extends RecyclerView.Adapter<StudentHostelAdapter.MyViewHolder> {

    private StudentHostel context;
    private ArrayList<String> hostelIdList;
    private ArrayList<String> hostelNameList;
    private ArrayList<String> room_typeList;
    private ArrayList<String> room_noList;
    private ArrayList<String> no_of_bedList;
    private ArrayList<String> cost_per_bedList;
    private ArrayList<String> assignList;

    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();

    StudentHostelDetailAdapter adapter;

    public StudentHostelAdapter(StudentHostel studentHostel, ArrayList<String> hostelIdList, ArrayList<String> hostelNameList,
                                ArrayList<String> room_typeList, ArrayList<String> room_noList, ArrayList<String> no_of_bedList,
                                ArrayList<String> cost_per_bedList, ArrayList<String> assignList) {
        this.context = studentHostel;
        this.hostelIdList = hostelIdList;
        this.hostelNameList = hostelNameList;
        this.room_typeList = room_typeList;
        this.room_noList = room_noList;
        this.no_of_bedList = no_of_bedList;
        this.cost_per_bedList = cost_per_bedList;
        this.assignList = assignList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView hostelNameTV,roomtypeTV,roomnoTV,noofbedTV,costperbedTV,statusTV;
        public RelativeLayout containerView;
        LinearLayout headLayout;

        public MyViewHolder(View view) {
            super(view);
            hostelNameTV = (TextView) view.findViewById(R.id.adapter_hostelnameTv);
            roomtypeTV = view.findViewById(R.id.adapter_roomtypeTV);
            roomnoTV= view.findViewById(R.id.adapter_roomnoTV);
            noofbedTV = view.findViewById(R.id.adapter_noofbedTV);
            costperbedTV = view.findViewById(R.id.adapter_costperbedTV);
            statusTV = view.findViewById(R.id.Adapter_statusTV);
            headLayout = view.findViewById(R.id.headLayout);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_hostel, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final String currency = Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency);
        holder.hostelNameTV.setText(hostelNameList.get(position));
        holder.roomtypeTV.setText(room_typeList.get(position));
        holder.roomnoTV.setText(room_noList.get(position));
        holder.noofbedTV.setText(no_of_bedList.get(position));
        holder.costperbedTV.setText(currency +cost_per_bedList.get(position));
        if(assignList.get(position).equals("1")){
            holder.statusTV.setVisibility(View.VISIBLE);
        }else{
            holder.statusTV.setVisibility(View.GONE);
        }
        holder.headLayout.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));


    }

    @Override
    public int getItemCount() {
        return hostelIdList.size();
    }


}
