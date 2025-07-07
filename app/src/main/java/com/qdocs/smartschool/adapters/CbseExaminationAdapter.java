package com.qdocs.smartschool.adapters;

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
import com.qdocs.smartschool.model.AssismentTypeModel;
import com.qdocs.smartschool.model.CbseExamModel;
import com.qdocs.smartschool.model.SubjectModel;
import com.qdocs.smartschool.students.CbseExaminationActivity;
import java.util.ArrayList;


public class CbseExaminationAdapter extends RecyclerView.Adapter<CbseExaminationAdapter.MyViewHolder>  {
    CbseExaminationActivity context;
    private ArrayList<CbseExamModel> cbseexamlist = new ArrayList<>();
    private Fragment fragment;
    public CbseExaminationAdapter(CbseExaminationActivity context, ArrayList<CbseExamModel> cbseexamlist, Fragment fragment) {

        this.cbseexamlist = cbseexamlist;
        this.context = context;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public CbseExaminationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cbse_exam_adapter, parent, false);
        return new CbseExaminationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CbseExaminationAdapter.MyViewHolder myViewHolder, int position) {
        CbseExamModel model = cbseexamlist.get(position);
        myViewHolder.name.setText(model.getName());
        myViewHolder.totalmarks.setText(model.getExam_obtain_marks()+"/"+model.getExam_total_marks());
        myViewHolder.percentage.setText(model.getExam_percentage()+"%");
        myViewHolder.grade.setText(model.getExam_grade());
        myViewHolder.rank.setText(model.getExam_rank());

        ArrayList<SubjectModel> subjectList = model.getSubject();
        CbseExaminationSubjectAdapter adapter = new CbseExaminationSubjectAdapter(context,subjectList,fragment);
        myViewHolder.recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        myViewHolder.recyclerview.setItemAnimator(new DefaultItemAnimator());
        myViewHolder.recyclerview.setAdapter(adapter);

        ArrayList<AssismentTypeModel> assimentList = model.getAssismenttype();
        CbseAssismentTypeAdapter cbseAssismentTypeAdapter = new CbseAssismentTypeAdapter(context,assimentList,fragment);
        myViewHolder.assrecyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
        myViewHolder.assrecyclerview.setItemAnimator(new DefaultItemAnimator());
        myViewHolder.assrecyclerview.setAdapter(cbseAssismentTypeAdapter);
    }

    @Override
    public int getItemCount() {
        return cbseexamlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,totalmarks,percentage,grade,rank;
        RecyclerView recyclerview,assrecyclerview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            totalmarks=itemView.findViewById(R.id.totalmarks);
            percentage=itemView.findViewById(R.id.percentage);
            grade=itemView.findViewById(R.id.grade);
            rank=itemView.findViewById(R.id.rank);
            recyclerview=itemView.findViewById(R.id.recyclerview);
            assrecyclerview=itemView.findViewById(R.id.assrecyclerview);
        }
    }
}
