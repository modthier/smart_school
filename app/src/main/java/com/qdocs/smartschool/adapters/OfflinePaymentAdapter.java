package com.qdocs.smartschool.adapters;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.smartschool.OpenPdf;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class OfflinePaymentAdapter extends RecyclerView.Adapter<OfflinePaymentAdapter.MyViewHolder> {
    long downloadID;
    private FragmentActivity context;
    private ArrayList<String> paymentdateList;
    private ArrayList<String> submitdateList;
    private ArrayList<String> statusdateList;
    private ArrayList<String> idList;
    private ArrayList<String> amountlist;
    private ArrayList<String> invoice_idlist;
    private ArrayList<String> referencelist;
    private ArrayList<String> routeList;
    private ArrayList<String> paymentmodelist;
    private ArrayList<String> paymentfromlist;
    private ArrayList<String> fee_group_nameList;
    private ArrayList<String> monthlist;
    private ArrayList<String> is_activelist;
    private ArrayList<String> replylist;
    private ArrayList<String> attachmentlist;
    private ArrayList<String> codelist;
    private ArrayList<String> transportfeesmonthlist;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    String defaultDatetimeFormat;
    String permission;
    public OfflinePaymentAdapter(FragmentActivity studentonlineexam, ArrayList<String> idList, ArrayList<String> paymentdateList,
                                 ArrayList<String> submitdateList, ArrayList<String> statusdateList, ArrayList<String> amountlist, ArrayList<String> invoice_idlist,
                                 ArrayList<String> referencelist, ArrayList<String> paymentmodelist, ArrayList<String> paymentfromlist, ArrayList<String> fee_group_nameList, ArrayList<String> monthlist,
                                 ArrayList<String> routeList, ArrayList<String> is_activelist, ArrayList<String> replylist, ArrayList<String> attachmentlist, ArrayList<String> codelist, ArrayList<String> transportfeesmonthlist) {
        this.context = studentonlineexam;
        this.paymentdateList = paymentdateList;
        this.submitdateList = submitdateList;
        this.statusdateList = statusdateList;
        this.idList = idList;
        this.amountlist = amountlist;
        this.invoice_idlist = invoice_idlist;
        this.referencelist = referencelist;
        this.paymentmodelist = paymentmodelist;
        this.paymentfromlist = paymentfromlist;
        this.monthlist = monthlist;
        this.is_activelist = is_activelist;
        this.fee_group_nameList = fee_group_nameList;
        this.routeList = routeList;
        this.replylist = replylist;
        this.attachmentlist = attachmentlist;
        this.codelist = codelist;
        this.transportfeesmonthlist = transportfeesmonthlist;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView paymentdate, submitdate, amount, feescode,statusdate,feesgroup,paymentid,comment,routepickuppoint,paymentFrom,transportfeesmonth,reference,paymentMode,id,status;
        LinearLayout headLayout,feescode_layout,feesgroup_layout,transportfeesmonth_layout,route_layout;
        ImageView downloadBtn;

        public MyViewHolder(View view) {
            super(view);
            paymentdate = view.findViewById(R.id.paymentdate);
            id = view.findViewById(R.id.id);
            amount = view.findViewById(R.id.amount);
            headLayout = view.findViewById(R.id.adapter_student_headLayout);
            submitdate = view.findViewById(R.id.submitdate);
            statusdate = view.findViewById(R.id.statusdate);
            feesgroup = view.findViewById(R.id.feesgroup);
            feescode = view.findViewById(R.id.feescode);
            paymentFrom = view.findViewById(R.id.paymentFrom);
            reference = view.findViewById(R.id.reference);
            paymentMode = view.findViewById(R.id.paymentMode);
            status = view.findViewById(R.id.status);
            comment = view.findViewById(R.id.comment);
            downloadBtn = view.findViewById(R.id.downloadBtn);
            transportfeesmonth = view.findViewById(R.id.transportfeesmonth);
            feescode_layout = view.findViewById(R.id.feescode_layout);
            feesgroup_layout = view.findViewById(R.id.feesgroup_layout);
            transportfeesmonth_layout = view.findViewById(R.id.transportfeesmonth_layout);
            route_layout = view.findViewById(R.id.route_layout);
            paymentid = view.findViewById(R.id.paymentid);
            routepickuppoint = view.findViewById(R.id.routepickuppoint);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_offlinepayment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        defaultDatetimeFormat = Utility.getSharedPreferences(context.getApplicationContext(), "datetimeFormat");
        holder.headLayout.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        holder.paymentdate.setText(paymentdateList.get(position));
        holder.submitdate.setText(submitdateList.get(position));
        holder.statusdate.setText(statusdateList.get(position));
        holder.amount.setText(amountlist.get(position));
        holder.feesgroup.setText(fee_group_nameList.get(position));
        holder.feescode.setText(codelist.get(position));
        holder.reference.setText(referencelist.get(position));
        holder.paymentFrom.setText(paymentfromlist.get(position));
        holder.paymentMode.setText(paymentmodelist.get(position));
        holder.paymentid.setText(invoice_idlist.get(position));
        holder.id.setText("Request ID "+idList.get(position));
        holder.comment.setText(replylist.get(position));
        if(transportfeesmonthlist.get(position).equals("")){
            holder.feesgroup_layout.setVisibility(View.VISIBLE);
            holder.feescode_layout.setVisibility(View.VISIBLE);
            holder.transportfeesmonth_layout.setVisibility(View.GONE);
            holder.route_layout.setVisibility(View.GONE);
        }else{
            holder.feesgroup_layout.setVisibility(View.GONE);
            holder.feescode_layout.setVisibility(View.GONE);
            holder.transportfeesmonth_layout.setVisibility(View.VISIBLE);
            holder.route_layout.setVisibility(View.VISIBLE);
            holder.transportfeesmonth.setText(transportfeesmonthlist.get(position));
            holder.routepickuppoint.setText(routeList.get(position));
        }

        if(is_activelist.get(position).equals("1")){
            holder.status.setText(context.getApplicationContext().getString(R.string.approved));
            holder.status.setBackgroundResource(R.drawable.green_border);
        }else if(is_activelist.get(position).equals("0")){
            holder.status.setText(context.getApplicationContext().getString(R.string.pending));
            holder.status.setBackgroundResource(R.drawable.yellow_border);
        }else if(is_activelist.get(position).equals("2")){
            holder.status.setText(context.getApplicationContext().getString(R.string.rejected));
            holder.status.setBackgroundResource(R.drawable.red_border);
        }

        if(attachmentlist.get(position).equals("")){
            holder.downloadBtn.setVisibility(View.GONE);
        }else{
            holder.downloadBtn.setVisibility(View.VISIBLE);
            holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String urlStr = Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl);
                    urlStr += "uploads/offline_payments/"+attachmentlist.get(position);
                    downloadID = Utility.beginDownload(context, attachmentlist.get(position), urlStr);
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
        return idList.size();
    }

}


