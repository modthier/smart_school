package com.qdocs.smartschool.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.students.StudentVideoTutorialPlay;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import java.util.ArrayList;

public class StudentDownloadVideoAdapter extends RecyclerView.Adapter<StudentDownloadVideoAdapter.MyViewHolder> {

    private FragmentActivity context;
    RecyclerView recyclerView;
    ArrayList<String> idList;
    ArrayList<String> titleList;
    ArrayList<String> descriptionList;
    ArrayList<String> video_linkList;
    ArrayList<String> thumb_pathList;
    ArrayList<String> created_byList;
    ArrayList<String> created_by;

    private int lastPosition = -1;
    public StudentDownloadVideoAdapter(FragmentActivity fragmentActivity, RecyclerView recyclerView, ArrayList<String> idList, ArrayList<String> titleList,
                                       ArrayList<String> descriptionList, ArrayList<String> video_linkList, ArrayList<String> thumb_pathList, ArrayList<String> created_byList, ArrayList<String> created_by) {
        this.context = fragmentActivity;
        this.recyclerView = recyclerView;
        this.idList = idList;
        this.titleList = titleList;
        this.descriptionList = descriptionList;
        this.video_linkList = video_linkList;
        this.thumb_pathList = thumb_pathList;
        this.created_byList = created_byList;
        this.created_by = created_by;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView videotitlename,description,created_by;
            ImageView videothumnail;


        public MyViewHolder(View view) {
            super(view);
            videotitlename = view.findViewById(R.id.videotitlename);
            videothumnail = view.findViewById(R.id.videothumnail);
            description = view.findViewById(R.id.description);
            created_by = view.findViewById(R.id.created_by);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_download_videolist, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String superadmin_restriction = Utility.getSharedPreferences(context.getApplicationContext(), Constants.superadmin_restriction);

        holder.videotitlename.setText(titleList.get(position));
        holder.description.setText(descriptionList.get(position));

        if(superadmin_restriction.equals("enabled")){
            holder.created_by.setText(created_byList.get(position));
        }else{
            if(created_by.get(position).equals("1")){
                holder.created_by.setText("");
            }else{
                holder.created_by.setText(created_byList.get(position));
            }
        }
        String thumnail = Utility.getSharedPreferences(context.getApplicationContext(), "imagesUrl")+thumb_pathList.get(position);
        Glide.with(context)
                .load(thumnail)
                .into(holder.videothumnail);
        holder.videothumnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video_linkList.get(position)));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                context.startActivity(intent);
                /*Intent intent = new Intent(context.getApplicationContext(), StudentVideoTutorialPlay.class);
                intent.putExtra("title", titleList.get(position));
                intent.putExtra("titlepath", thumb_pathList.get(position));
                intent.putExtra("videolink", video_linkList.get(position));
                context.startActivity(intent);*/
            }
        });

    }
    @Override
    public int getItemCount() {
        return idList.size();
    }


}
