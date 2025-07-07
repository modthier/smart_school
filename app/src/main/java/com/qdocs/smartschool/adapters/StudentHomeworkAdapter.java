package com.qdocs.smartschool.adapters;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import androidx.fragment.app.FragmentActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qdocs.smartschool.students.StudentUploadHomework;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.OpenPdf;
import com.qdocs.smartschool.R;
import java.util.ArrayList;


public class StudentHomeworkAdapter extends RecyclerView.Adapter<StudentHomeworkAdapter.MyViewHolder> {

    private FragmentActivity context;
    private ArrayList<String> homeworkIdList;
    private ArrayList<String> homeworkTitleList;
    private ArrayList<String> homeworkSubjectNameList;
    private ArrayList<String> homeworkHomeworkDateList;
    private ArrayList<String> homeworkSubmissionDateList;
    private ArrayList<String> homeworkEvaluationDateList;
    private ArrayList<String> homeworkEvaluationByList;
    private ArrayList<String> marksList;
    private ArrayList<String> homeworkDocumentList;
    private ArrayList<String> homeworkEvaluationMarksList;
    private ArrayList<String> homeworkClasssList;
    private ArrayList<String> homework_evaluation_idList;
    private ArrayList<String> homeworkstatusList;
    private ArrayList<String> homeworknameList;
    private ArrayList<String> homeworkCreatedByList;
    private ArrayList<String> noteList;
    private ArrayList<String> created_byList;
    long downloadID;

    public StudentHomeworkAdapter(FragmentActivity studentHomework, ArrayList<String> homeworkIdList,
                                  ArrayList<String> homeworkTitleList, ArrayList<String> homeworkSubjectNameList,
                                  ArrayList<String> homeworkHomeworkDateList, ArrayList<String> homeworkSubmissionDateList,
                                  ArrayList<String> homeworkEvaluationDateList, ArrayList<String> homeworkEvaluationByList,
                                  ArrayList<String> homeworkCreatedByList, ArrayList<String> homeworkDocumentList,
                                  ArrayList<String> homeworkClasssList, ArrayList<String> homework_evaluation_idList,
                                  ArrayList<String> homeworkstatusList, ArrayList<String> homeworknameList, ArrayList<String> marksList, ArrayList<String> homeworkEvaluationMarksList, ArrayList<String> noteList, ArrayList<String> created_byList) {

        this.context = studentHomework;
        this.homeworkIdList = homeworkIdList;
        this.homeworkTitleList = homeworkTitleList;
        this.homeworkSubjectNameList = homeworkSubjectNameList;
        this.homeworkHomeworkDateList = homeworkHomeworkDateList;
        this.homeworkSubmissionDateList = homeworkSubmissionDateList;
        this.homeworkEvaluationDateList = homeworkEvaluationDateList;
        this.homeworkEvaluationByList = homeworkEvaluationByList;
        this.marksList = marksList;
        this.homeworkCreatedByList = homeworkCreatedByList;
        this.homeworkEvaluationMarksList = homeworkEvaluationMarksList;
        this.homeworkDocumentList = homeworkDocumentList;
        this.homeworkClasssList = homeworkClasssList;
        this.homework_evaluation_idList = homework_evaluation_idList;
        this.homeworkstatusList = homeworkstatusList;
        this.homeworknameList = homeworknameList;
        this.created_byList = created_byList;
        this.noteList = noteList;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView subjectNameTV, homeworkDateTV, submissionDateTV, evaluationDateTV,subjectTV;
        TextView evaluatedByTV, createdByTV, classTV, evaluationDateHeadTV, evaluatedByHeadTV;
        LinearLayout downloadBtn,uploadBtn,submitted_status_layout,pending_status_layout,evaluated_status_layout;
        TextView description,marks,marksObt,note;
        LinearLayout headLay;

        public MyViewHolder(View view) {
            super(view);
            subjectNameTV = (TextView) view.findViewById(R.id.adapter_student_homework_subjectNameTV);
            classTV = (TextView) view.findViewById(R.id.adapter_student_homework_class);
            homeworkDateTV = (TextView) view.findViewById(R.id.adapter_student_homework_homeworkDateTV);
            submissionDateTV = (TextView) view.findViewById(R.id.adapter_student_homework_submissionDateTV);
            evaluationDateTV = (TextView) view.findViewById(R.id.adapter_student_homework_evluationDateTV);
            evaluatedByTV = (TextView) view.findViewById(R.id.adapter_student_homework_evaluatedByTV);
            description = (TextView) view.findViewById(R.id.adapter_student_homework_description);
            createdByTV = (TextView) view.findViewById(R.id.adapter_student_homework_createdByTV);
            downloadBtn = (LinearLayout) view.findViewById(R.id.adapter_student_homework_downloadBtn);
            uploadBtn = (LinearLayout) view.findViewById(R.id.adapter_student_homework_uploadBtn);
            submitted_status_layout = (LinearLayout) view.findViewById(R.id.submitted_status_layout);
            evaluated_status_layout = (LinearLayout) view.findViewById(R.id.evaluated_status_layout);
            pending_status_layout = (LinearLayout) view.findViewById(R.id.pending_status_layout);
            headLay = view.findViewById(R.id.adapter_student_homework_headLayout);
            evaluationDateHeadTV = view.findViewById(R.id.adapter_student_homework_evaluationDateHeadTV);
            marks = view.findViewById(R.id.marks);
            marksObt = view.findViewById(R.id.marksObt);
            note = view.findViewById(R.id.note);
            evaluatedByHeadTV = view.findViewById(R.id.adapter_student_homework_evaluatedByHeadTV);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_homework_new, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String defaultDateFormat = Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat");
        String superadmin_restriction = Utility.getSharedPreferences(context.getApplicationContext(), Constants.superadmin_restriction);
        //DECORATE
        holder.headLay.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));

        holder.subjectNameTV.setText(homeworkSubjectNameList.get(position));
        holder.homeworkDateTV.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat,homeworkHomeworkDateList.get(position)));
        holder.submissionDateTV.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat,homeworkSubmissionDateList.get(position)));
        holder.evaluationDateTV.setText(homeworkEvaluationDateList.get(position));
        holder.marks.setText(marksList.get(position));
        holder.marksObt.setText(homeworkEvaluationMarksList.get(position));
        String plain = Html.fromHtml(homeworkTitleList.get(position)).toString();
        holder.description.setText(plain);
        holder.note.setText(noteList.get(position));
        holder.evaluatedByTV.setText(homeworkEvaluationByList.get(position));
        context.registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        if(superadmin_restriction.equals("enabled")){
            holder.createdByTV.setText(homeworkCreatedByList.get(position));
        }else{
            if(created_byList.get(position).equals("1")){
                holder.createdByTV.setText("");
            }else{
                holder.createdByTV.setText(homeworkCreatedByList.get(position));
            }
        }
        if(homeworkstatusList.get(position).equals("pending")){
            holder.pending_status_layout.setVisibility(View.VISIBLE);
            holder.submitted_status_layout.setVisibility(View.GONE);
            holder.evaluated_status_layout.setVisibility(View.GONE);
            holder.uploadBtn.setVisibility(View.VISIBLE);
        }else if(homeworkstatusList.get(position).equals("submitted")){
            holder.pending_status_layout.setVisibility(View.GONE);
            holder.submitted_status_layout.setVisibility(View.VISIBLE);
            holder.evaluated_status_layout.setVisibility(View.GONE);
            holder.uploadBtn.setVisibility(View.VISIBLE);
        }else if(homeworkstatusList.get(position).equals("evaluated")){
            holder.pending_status_layout.setVisibility(View.GONE);
            holder.submitted_status_layout.setVisibility(View.GONE);
            holder.evaluated_status_layout.setVisibility(View.VISIBLE);
            holder.uploadBtn.setVisibility(View.GONE);
        }

        holder.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context.getApplicationContext(), StudentUploadHomework.class);
                intent.putExtra("Homework_ID",homeworkIdList.get(position));
                System.out.println("homework_id="+homeworkIdList.get(position));
                context.startActivity(intent);
            }
        });


        if(homeworkDocumentList.get(position).equals("")) {
            holder.downloadBtn.setVisibility(View.GONE);
        } else {
            holder.downloadBtn.setVisibility(View.VISIBLE);
        }

        holder.downloadBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String urlStr = Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl);
                urlStr += "uploads/homework/"+homeworkDocumentList.get(position);
                downloadID = Utility.beginDownload(context, homeworkDocumentList.get(position), urlStr);

                Intent intent=new Intent(context.getApplicationContext(), OpenPdf.class);
                intent.putExtra("imageUrl",urlStr);
                context.startActivity(intent);
            }
        });

    }

    public BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.notification_logo)
                                .setContentTitle(context.getApplicationContext().getString(R.string.app_name))
                                .setContentText("All Download completed");

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());
                context.unregisterReceiver(onDownloadComplete);
            }
        }
    };
    @Override
    public int getItemCount() {
        return homeworkIdList.size();
    }
}
