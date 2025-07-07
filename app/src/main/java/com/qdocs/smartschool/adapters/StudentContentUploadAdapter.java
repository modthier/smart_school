package com.qdocs.smartschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.smartschool.R;

import java.util.ArrayList;

public class StudentContentUploadAdapter extends RecyclerView.Adapter<StudentContentUploadAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> real_nameList;
    private ArrayList<String> filetypeList;
    private ArrayList<String> teacherroom_noList;
    private ArrayList<String> time_List;
    private ArrayList<String> idList;

    public StudentContentUploadAdapter(Context applicationContext, ArrayList<String> real_nameList, ArrayList<String> filetypeList ) {
        this.context = applicationContext;
        this.real_nameList = real_nameList;
        this.filetypeList = filetypeList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView filename, day,subject,Roomno;
        LinearLayout viewdetail;
        public MyViewHolder(View view) {
            super(view);
            filename = (TextView) view.findViewById(R.id.filename);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_content_upload_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.filename.setText(real_nameList.get(position));

    }

    @Override
    public int getItemCount() {
        return real_nameList.size();
    }
}

