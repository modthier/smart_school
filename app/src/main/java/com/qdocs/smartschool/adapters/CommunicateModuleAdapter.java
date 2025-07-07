package com.qdocs.smartschool.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.model.Album1;
import com.qdocs.smartschool.students.StudentNoticeBoard;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class CommunicateModuleAdapter extends RecyclerView.Adapter<CommunicateModuleAdapter.MyViewHolder> {
    private FragmentActivity context;
    private List<Album1> albumList;
    Album1 album1 ;
    View view;
    public CommunicateModuleAdapter(FragmentActivity context, ArrayList<Album1> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_module_selection, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView modulename;
        LinearLayout layout;
        ImageView moduleiamge;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            modulename = (TextView) itemView.findViewById(R.id.modulename);
            moduleiamge = (ImageView) itemView.findViewById(R.id.moduleiamge);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Utility.setLocale(context, Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
         album1 = albumList.get(position);
         holder.modulename.setText(album1.getName());
        if(position==0){
            if(album1.getName().equals("notice_board")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.noticeBoard));
            }
        }
         if(album1.getValue().equals("0")){
             holder.itemView.setVisibility(View.GONE);
             holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            // notifyItemRemoved(position);
         }else{
             holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
             holder.itemView.setVisibility(View.VISIBLE);
         }
       Picasso.with(context).load(album1.getThumbnail()).fit().centerInside().placeholder(null).into(holder.moduleiamge);
         holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==0){
                        if(album1.getName().equals("notice_board")) {
                            Intent examlistintent = new Intent(context, StudentNoticeBoard.class);
                            context.startActivity(examlistintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }
                    }
                }
            });
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


}
