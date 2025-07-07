package com.qdocs.smartschool.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.smartschool.OpenPdf;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.students.StudentVisitorBook;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class StudentVisitorBookAdapter extends RecyclerView.Adapter<StudentVisitorBookAdapter.MyViewHolder> {

    private StudentVisitorBook context;
    private ArrayList<String> idList;
    private ArrayList<String> purposeList;
    private ArrayList<String> dateList;
    private ArrayList<String> visitornamelist;
    private ArrayList<String> contactlist;
    private ArrayList<String> id_prooflist;
    private ArrayList<String> no_of_peoplelist;
    private ArrayList<String> notelist;
    private ArrayList<String> in_timelist;
    private ArrayList<String> out_timelist;
    private ArrayList<String> imagelist;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    private boolean isapplyDateSelected = false;
    private boolean isfromDateSelected = false;
    private boolean istoDateSelected = false;
    String apply_date="";
    String from_date="";
    String to_date="";
    public String defaultDateFormat;
    long downloadID;

    public StudentVisitorBookAdapter(StudentVisitorBook studentVisitorBook, ArrayList<String> idList, ArrayList<String> purposeList, ArrayList<String> dateList,
                                     ArrayList<String> visitornamelist, ArrayList<String> contactlist, ArrayList<String> id_prooflist, ArrayList<String> no_of_peoplelist,
                                     ArrayList<String> notelist, ArrayList<String> in_timelist, ArrayList<String> out_timelist, ArrayList<String> imagelist) {

        this.context = studentVisitorBook;
        this.idList = idList;
        this.purposeList = purposeList;
        this.dateList = dateList;
        this.visitornamelist = visitornamelist;
        this.contactlist = contactlist;
        this.id_prooflist = id_prooflist;
        this.no_of_peoplelist = no_of_peoplelist;
        this.notelist = notelist;
        this.in_timelist = in_timelist;
        this.out_timelist = out_timelist;
        this.imagelist = imagelist;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView visitor_name,purposeTV,phoneTV,idcardTV,no_of_personTV,noteTV,dateTV,intimeTV,outtimeTV;
        ImageView downloadBtn;
        RelativeLayout headLayout;

        public MyViewHolder(View view) {
            super(view);
            visitor_name = view.findViewById(R.id.visitor_name);
            purposeTV = view.findViewById(R.id.adapter_purposeTV);
            phoneTV = view.findViewById(R.id.adapter_phoneTV);
            idcardTV = view.findViewById(R.id.adapter_idcardTV);
            no_of_personTV = view.findViewById(R.id.adapter_no_of_personTV);
            noteTV = view.findViewById(R.id.adapter_noteTV);
            dateTV = view.findViewById(R.id.adapter_dateTV);
            intimeTV = view.findViewById(R.id.adapter_intimeTV);
            outtimeTV = view.findViewById(R.id.adapter_outtimeTV);
            downloadBtn = view.findViewById(R.id.downloadBtn);
            headLayout = view.findViewById(R.id.headLayout);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_visitorbook, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.headLayout.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        holder.visitor_name.setText(visitornamelist.get(position));
        holder.purposeTV.setText(purposeList.get(position));
        holder.phoneTV.setText(contactlist.get(position));
        holder.idcardTV.setText(id_prooflist.get(position));
        holder.no_of_personTV.setText(no_of_peoplelist.get(position));
        holder.noteTV.setText(notelist.get(position));
        holder.dateTV.setText(dateList.get(position));
        holder.intimeTV.setText(in_timelist.get(position));
        holder.outtimeTV.setText(out_timelist.get(position));

        if(imagelist.get(position).equals("")){
            holder.downloadBtn.setVisibility(View.GONE);
        }else{
            holder.downloadBtn.setVisibility(View.VISIBLE);
        }

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String urlStr = Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl);
                urlStr += "uploads/front_office/visitors/"+imagelist.get(position);
                System.out.println("Attachment="+urlStr);
                downloadID = Utility.beginDownload(context, imagelist.get(position), urlStr);
                Intent intent=new Intent(context.getApplicationContext(), OpenPdf.class);
                intent.putExtra("imageUrl",urlStr);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return idList.size();
    }
}