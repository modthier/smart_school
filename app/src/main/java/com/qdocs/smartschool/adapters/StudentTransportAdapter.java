package com.qdocs.smartschool.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.students.StudentTransportRoutes;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentTransportAdapter extends RecyclerView.Adapter<StudentTransportAdapter.MyViewHolder> {

    private StudentTransportRoutes context;
    private List<String> pickup_pointList;
    private List<String> destination_distanceList;
    private List<String> pickup_timeList;
    long downloadID;
    String path,pickupname;

    public StudentTransportAdapter(StudentTransportRoutes studentTransportRoutes, ArrayList<String> pickup_pointList,
                                   ArrayList<String> destination_distanceList, ArrayList<String> pickup_timeList,String pickupname) {
        this.context = studentTransportRoutes;
        this.pickup_pointList = pickup_pointList;
        this.destination_distanceList = destination_distanceList;
        this.pickup_timeList = pickup_timeList;
        this.pickupname = pickupname;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView pickuppoint, distance, pickuptime;
        View lineView,horizontallineView;
        LinearLayout adapter_headLayout;
        CardView card_view_inner;

        public MyViewHolder(View view) {
            super(view);
            pickuppoint = view.findViewById(R.id.adapter_pickuppoint);
            distance = view.findViewById(R.id.adapter_distance);
            pickuptime = view.findViewById(R.id.adapter_pickuptime);
            lineView = view.findViewById(R.id.adapter_studentTimeline_line);
            horizontallineView = view.findViewById(R.id.adapter_studentTimeline_horizontalline);
            adapter_headLayout = view.findViewById(R.id.adapter_headLayout);
            card_view_inner = view.findViewById(R.id.card_view_inner);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_transport_route, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.pickuppoint.setText(pickup_pointList.get(position));
        holder.distance.setText(destination_distanceList.get(position));

        try {
            DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
            Date d = dateFormat.parse(pickup_timeList.get(position));
            SimpleDateFormat formatTime = new SimpleDateFormat("hh.mm aa");
            String time = formatTime.format(d);
            holder.pickuptime.setText(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(pickup_pointList.get(position));
        System.out.println("pickupname"+pickupname);
        if(pickup_pointList.get(position).equals(pickupname)){
            System.out.println("yes");
            holder.card_view_inner.setBackgroundColor(Color.parseColor("#B0DD38"));
        }else{
            System.out.println("no");
            holder.adapter_headLayout.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        }

        holder.lineView.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        holder.horizontallineView.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
    }

    @Override
    public int getItemCount() {
        return pickup_pointList.size();
    }
}