package com.qdocs.smartschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.smartschool.R;
import java.util.ArrayList;

public class SubjectCbseExaminationAdapter extends RecyclerView.Adapter<SubjectCbseExaminationAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> subjectNameList;

    public SubjectCbseExaminationAdapter(Context applicationContext, ArrayList<String> subjectNameList) {

        this.subjectNameList = subjectNameList;
        this.context = applicationContext;

    }

    @NonNull
    @Override
    public SubjectCbseExaminationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cbse_subject_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectCbseExaminationAdapter.MyViewHolder holder, int position) {
        holder.name.setText(subjectNameList.get(position));


    }

    @Override
    public int getItemCount() {
        return subjectNameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);


        }
    }
}
