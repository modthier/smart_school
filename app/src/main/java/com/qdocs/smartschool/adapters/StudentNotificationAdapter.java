package com.qdocs.smartschool.adapters;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qdocs.smartschool.OpenPdf;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import java.util.ArrayList;

public class StudentNotificationAdapter extends BaseAdapter {

    private FragmentActivity context;
    private ArrayList<String> noticeTitleId;
    private ArrayList<String> noticeTitleList;
    private ArrayList<String> noticeDateList;
    private ArrayList<String> noticeDescList;
    private ArrayList<String> noticeAttachmentList;
    private ArrayList<String> noticepublishDateList;
    private ArrayList<String> noticeCreatedByList;
    long downloadID;
    public StudentNotificationAdapter(FragmentActivity studentNoticeBoard, ArrayList<String> noticeTitleId,
                                      ArrayList<String> noticeTitleList, ArrayList<String> noticeDateList,
                                      ArrayList<String> noticeDescList, ArrayList<String> noticeAttachmentList, ArrayList<String> noticepublishDateList, ArrayList<String> noticeCreatedByList) {

        this.context = studentNoticeBoard;
        this.noticeTitleId = noticeTitleId;
        this.noticeTitleList = noticeTitleList;
        this.noticeDateList = noticeDateList;
        this.noticeDescList = noticeDescList;
        this.noticeAttachmentList = noticeAttachmentList;
        this.noticepublishDateList = noticepublishDateList;
        this.noticeCreatedByList = noticeCreatedByList;
    }

    @Override
    public int getCount() {
        return noticeTitleId.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View rowView = view;
        ViewHolder viewHolder = null;
        String superadmin_restriction = Utility.getSharedPreferences(context.getApplicationContext(), Constants.superadmin_restriction);
        if (rowView == null) {

            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.adapter_student_notification, viewGroup, false);
            viewHolder = new ViewHolder();


            viewHolder.notifTitle = (TextView) rowView.findViewById(R.id.studentNotificationAdapter_titleTV);
            viewHolder.notifDate = (TextView) rowView.findViewById(R.id.studentNotificationAdapter_dateTV);
            viewHolder.publishDate = (TextView) rowView.findViewById(R.id.studentNotificationAdapter_publishdateTV);
            viewHolder.createdbyTV = (TextView) rowView.findViewById(R.id.studentNotificationAdapter_createdbyTV);
            viewHolder.headLayout = (LinearLayout) rowView.findViewById(R.id.studentNotificationAdapter_headLayout);
            viewHolder.notificationDetailTV = rowView.findViewById(R.id.studentNotification_bottomSheet_descTV);
            viewHolder.createdBy_layout = rowView.findViewById(R.id.createdBy_layout);
            viewHolder.attachment_layout = rowView.findViewById(R.id.attachment_layout);

            viewHolder.notifTitle.setTag(position);
            viewHolder.notifDate.setTag(position);

        }else{
            viewHolder  = (ViewHolder) rowView.getTag();
        }
        if(superadmin_restriction.equals("enabled")){
            viewHolder.createdBy_layout.setVisibility(View.VISIBLE);
        }else{
            viewHolder.createdBy_layout.setVisibility(View.GONE);
        }

        viewHolder.notifTitle.setText(noticeTitleList.get(position));
        viewHolder.notifDate.setText(noticeDateList.get(position));
        viewHolder.publishDate.setText(noticepublishDateList.get(position));
        viewHolder.createdbyTV.setText(noticeCreatedByList.get(position));
        viewHolder.notificationDetailTV.setText(Html.fromHtml(noticeDescList.get(position)));
       // viewHolder.notificationDetailTV.loadData(noticeDescList.get(position),"text/html; charset=utf-8", null);
        if(noticeAttachmentList.get(position).equals("")){
            viewHolder.attachment_layout.setVisibility(View.GONE);
        }else{
            viewHolder.attachment_layout.setVisibility(View.VISIBLE);

                    viewHolder.attachment_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String urlStr = Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl);
                            urlStr += noticeAttachmentList.get(position);
                            System.out.println("urlStr="+urlStr);
                            downloadID = Utility.beginDownload(context, noticeAttachmentList.get(position), urlStr);
                            Intent intent=new Intent(context.getApplicationContext(), OpenPdf.class);
                            intent.putExtra("imageUrl",urlStr);
                            context.startActivity(intent);
                        }
                    });

        }

        //DECORATE
        viewHolder.headLayout.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));

        rowView.setTag(viewHolder);
        return rowView;
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

    static class ViewHolder {
        public TextView notifTitle, notifDate,publishDate,createdbyTV;
        public LinearLayout headLayout,attachment_layout,createdBy_layout;
        public TextView notificationDetailTV;
    }
}
