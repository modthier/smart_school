package com.qdocs.smartschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.model.ExamAssismentModel;
import com.qdocs.smartschool.model.SubjectModel;
import java.util.ArrayList;


public class CbseExaminationSubjectAdapter extends RecyclerView.Adapter<CbseExaminationSubjectAdapter.MyViewHolder>  {

    Context context;
    private Fragment fragment;
    ArrayList<SubjectModel> subjectList = new ArrayList<>();

    public CbseExaminationSubjectAdapter(Context context, ArrayList<SubjectModel> subjectList, Fragment fragment) {

        this.subjectList = subjectList;
        this.context = context;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public CbseExaminationSubjectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cbse_subject_list, parent, false);
        return new CbseExaminationSubjectAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CbseExaminationSubjectAdapter.MyViewHolder myViewHolder, int position) {
        final SubjectModel model = subjectList.get(position);
        myViewHolder.subjectname.setText(model.getSubject_name()+" ("+model.getSubject_code()+")");
        myViewHolder.totalmarks.setText(model.getTotalmarks());

        ArrayList<ExamAssismentModel> assismentList = model.getAssisment();
        CbseExaminationAssismentAdapter adapter = new CbseExaminationAssismentAdapter(context,assismentList,fragment);
        myViewHolder.recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        myViewHolder.recyclerview.setItemAnimator(new DefaultItemAnimator());
        myViewHolder.recyclerview.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subjectname,totalmarks;
        RecyclerView recyclerview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectname= itemView.findViewById(R.id.subjectname);
            totalmarks= itemView.findViewById(R.id.totalmarks);
            recyclerview= itemView.findViewById(R.id.recyclerview);
        }
    }
}
