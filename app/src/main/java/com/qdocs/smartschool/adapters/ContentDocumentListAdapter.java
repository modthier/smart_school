package com.qdocs.smartschool.adapters;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.smartschool.OpenPdf;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.students.StudentVideoTutorialPlay;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;

import java.util.ArrayList;

public class ContentDocumentListAdapter extends RecyclerView.Adapter<ContentDocumentListAdapter.MyViewHolder> {

    private FragmentActivity context;
    private ArrayList<String> idlist;
    private ArrayList<String> real_namelist;
    private ArrayList<String> file_typelist;
    private ArrayList<String> vid_urllist;
    private ArrayList<String> img_namelist;
    private ArrayList<String> dir_pathlist;

    private boolean isapplyDateSelected = false;
    private boolean isfromDateSelected = false;
    private boolean istoDateSelected = false;
    String urlStr="",videoUrl="",title="",thumbpath="";
    String to_date="";
    public String defaultDateFormat;
    long downloadID;

    public ContentDocumentListAdapter(FragmentActivity fragmentActivity, ArrayList<String> idlist, ArrayList<String> real_namelist, ArrayList<String> file_typelist,
                                      ArrayList<String> vid_urllist, ArrayList<String> img_namelist, ArrayList<String> dir_pathlist) {

        this.context = fragmentActivity;
        this.idlist = idlist;
        this.real_namelist = real_namelist;
        this.file_typelist = file_typelist;
        this.vid_urllist = vid_urllist;
        this.img_namelist = img_namelist;
        this.dir_pathlist = dir_pathlist;


    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView filename;
        LinearLayout downloadBtn;

        public MyViewHolder(View view) {
            super(view);
            filename = view.findViewById(R.id.filename);
            downloadBtn = view.findViewById(R.id.downloadBtn);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_content_upload_detail, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.filename.setText(real_namelist.get(position));


        if(file_typelist.get(position).equals("video")){

            holder.downloadBtn.setVisibility(View.GONE);
            holder.filename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoUrl=vid_urllist.get(position);
                    title=real_namelist.get(position);
                    thumbpath=Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl)+dir_pathlist.get(position)+img_namelist.get(position);
                    Intent intent = new Intent(context.getApplicationContext(), StudentVideoTutorialPlay.class);
                    intent.putExtra("title", title);
                    intent.putExtra("titlepath", thumbpath);
                    intent.putExtra("videolink",videoUrl);
                    context.startActivity(intent);
                }
            });
        }else{

            holder.downloadBtn.setVisibility(View.VISIBLE);
            holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    urlStr = Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl);
                    urlStr += dir_pathlist.get(position)+img_namelist.get(position);
                     System.out.println("file_typelist="+urlStr);
                    downloadID = Utility.beginDownload(context, urlStr, urlStr);
                    Intent intent=new Intent(context.getApplicationContext(), OpenPdf.class);
                    intent.putExtra("imageUrl",urlStr);
                    context.startActivity(intent);

                }
            });
        }

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

            }
        }
    };

    @Override
    public int getItemCount() {
        return idlist.size();
    }
}