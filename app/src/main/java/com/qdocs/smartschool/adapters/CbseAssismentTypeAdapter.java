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
import com.qdocs.smartschool.model.AssismentTypeModel;
import java.util.ArrayList;


public class CbseAssismentTypeAdapter extends  RecyclerView.Adapter<CbseAssismentTypeAdapter.MyViewHolder> {

    Context context;
    ArrayList<AssismentTypeModel> assismentList = new ArrayList<>();
    Fragment fragment;

    public CbseAssismentTypeAdapter(Context context, ArrayList<AssismentTypeModel> assismentList, Fragment fragment) {

        this.assismentList = assismentList;
        this.context = context;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public CbseAssismentTypeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assissment_type_layout, parent, false);
        return new CbseAssismentTypeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CbseAssismentTypeAdapter.MyViewHolder myViewHolder, int position) {
        AssismentTypeModel assismentTypeModel=assismentList.get(position);
        if(assismentTypeModel.getCode().equals("")){
            myViewHolder.type.setText(assismentTypeModel.getName());
        }else{
            myViewHolder.type.setText(assismentTypeModel.getName()+" ("+assismentTypeModel.getCode()+")");
        }
        myViewHolder.maxmarks.setText("(Max "+assismentTypeModel.getMaximum_marks()+")");
    }

    @Override
    public int getItemCount() {
        return assismentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView type,maxmarks;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            type=itemView.findViewById(R.id.type);
            maxmarks=itemView.findViewById(R.id.maxmarks);

        }
    }
}
