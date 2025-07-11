package com.qdocs.smartschool.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qdocs.smartschool.students.StudentReportCard_ExamListResult;
import com.qdocs.smartschool.R;
import java.util.ArrayList;

public class StudentExamResultAdapter extends RecyclerView.Adapter<StudentExamResultAdapter.MyViewHolder> {

    private StudentReportCard_ExamListResult context;
    private ArrayList<String> examType;
    private ArrayList<String> examSubjectList;
    private ArrayList<String> examPassingMarksList;
    private ArrayList<String> examObtainedMarksList;
    private ArrayList<String> examResultList;
    private ArrayList<String> examget_marksList;
    private ArrayList<String> exam_gradeList;

    public StudentExamResultAdapter(StudentReportCard_ExamListResult studentReportCard_examResult,
                                    ArrayList<String> examType,ArrayList<String> examSubjectList, ArrayList<String> examPassingMarksList,
                                    ArrayList<String> examObtainedMarksList,ArrayList<String> examResultList,ArrayList<String> examget_marksList,ArrayList<String> exam_gradeList) {
        this.context = studentReportCard_examResult;
        this.examSubjectList = examSubjectList;
        this.examType = examType;
        this.examPassingMarksList = examPassingMarksList;
        this.examObtainedMarksList = examObtainedMarksList;
        this.examResultList = examResultList;
        this.examget_marksList = examget_marksList;
        this.exam_gradeList = exam_gradeList;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView subjectNameTV, passngMarksTV, obtainedMarksTV, resultnoteTV,subject,gradepoint,credit,quality,note,resultTV;
        LinearLayout gpa_layout,basic_layout;

        public MyViewHolder(View view) {
            super(view);
            subjectNameTV = (TextView) view.findViewById(R.id.adapter_reportCard_examResult_subjectNameTV);
            passngMarksTV = (TextView) view.findViewById(R.id.adapter_reportCard_examResult_passingmarksTV);
            obtainedMarksTV = (TextView) view.findViewById(R.id.adapter_reportCard_examResult_marksObtainedTV);
            resultnoteTV = (TextView) view.findViewById(R.id.adapter_reportCard_examResult_resultTV);
            resultTV = (TextView) view.findViewById(R.id.resultTV);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report_card_exam_result, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.subjectNameTV.setText(examSubjectList.get(position));
            holder.passngMarksTV.setText(examPassingMarksList.get(position));
            holder.obtainedMarksTV.setText(examObtainedMarksList.get(position));
            holder.resultnoteTV.setText(examResultList.get(position));
             if(examType.get(0).equals("basic_system")) {
                 if(examget_marksList.get(position).equals("")){
                     holder.resultTV.setText(context.getApplicationContext().getString(R.string.fail));
                     holder.resultTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.red_border));
                 }else{
                     if (Float.valueOf(examget_marksList.get(position)) >= Float.valueOf(examPassingMarksList.get(position))) {
                         holder.resultTV.setText(context.getApplicationContext().getString(R.string.pass));
                         // holder.resultTV.setBackgroundColor(Color.parseColor("#66AA18"));
                         holder.resultTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.green_border));
                     } else {
                         holder.resultTV.setText(context.getApplicationContext().getString(R.string.fail));
                         holder.resultTV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.red_border));
                     }
                 }

             }else{
                 holder.resultTV.setText(exam_gradeList.get(position));
                 holder.resultTV.setTextColor(context.getResources().getColor(R.color.textHeading));
             }
    }
    @Override
    public int getItemCount() {
        return examSubjectList.size();
    }
}