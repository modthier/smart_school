package com.qdocs.smartschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.model.CbseTimetableModel;
import com.qdocs.smartschool.utils.Utility;
import java.util.ArrayList;

public class CbseExaminationTimetableAdapter extends RecyclerView.Adapter<CbseExaminationTimetableAdapter.MyViewHolder>  {

    Context context;
    private Fragment fragment;
    ArrayList<CbseTimetableModel> subjectList = new ArrayList<>();

    public CbseExaminationTimetableAdapter(Context context, ArrayList<CbseTimetableModel> subjectList, Fragment fragment) {

        this.subjectList = subjectList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public CbseExaminationTimetableAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cbse_subjecttimetable_list, parent, false);
        return new CbseExaminationTimetableAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CbseExaminationTimetableAdapter.MyViewHolder myViewHolder, int position) {

        String defaultDateFormat = Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat");
        final CbseTimetableModel model = subjectList.get(position);
        myViewHolder.subjectname.setText(model.getSubject_name());
        myViewHolder.date.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat,model.getDate()));
        myViewHolder.starttime.setText(model.getTime_from());
        myViewHolder.duration.setText(model.getDuration());
        myViewHolder.rooomno.setText(model.getRoom_no());
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subjectname,date,starttime,duration,rooomno;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectname= itemView.findViewById(R.id.subjectname);
            date= itemView.findViewById(R.id.date);
            starttime= itemView.findViewById(R.id.starttime);
            duration= itemView.findViewById(R.id.duration);
            rooomno= itemView.findViewById(R.id.rooomno);

        }
    }
}
