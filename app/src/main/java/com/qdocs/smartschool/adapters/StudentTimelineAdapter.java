package com.qdocs.smartschool.adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.smartschool.OpenPdf;
import com.qdocs.smartschool.students.StudentEditTimeLine;
import com.qdocs.smartschool.students.StudentTimeline;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static android.widget.Toast.makeText;

public class StudentTimelineAdapter extends RecyclerView.Adapter<StudentTimelineAdapter.MyViewHolder> {

    private StudentTimeline context;
    private List<String> timeLineIdList;
    private List<String> timeLineDocumentList;
    private List<String> timeLineTitleList;
    private List<String> timeLineDescList;
    private List<String> timeLineDateList;
    private List<String> timeLineStatusList;
    String student_timeline;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    long downloadID;String path;

    public StudentTimelineAdapter( StudentTimeline studentTimeline, ArrayList<String> timeLineIdList,
                                  ArrayList<String> timeLineDocumentList, ArrayList<String> timeLineTitleList,
                                  ArrayList<String> timeLineDescList, ArrayList<String> timeLineDateList,
                                  ArrayList<String> timeLineStatusList) {

        this.context = studentTimeline;
        this.timeLineIdList = timeLineIdList;
        this.timeLineDocumentList = timeLineDocumentList;
        this.timeLineTitleList = timeLineTitleList;
        this.timeLineDescList = timeLineDescList;
        this.timeLineDateList = timeLineDateList;
        this.timeLineStatusList = timeLineStatusList;


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView dateTV, monthTV, yearTV, titleTV, descTV;
        public ImageView downloadBtn,editBtn,deleteBtn;
        LinearLayout headLayout;
        View lineView,horizontallineView;

        public MyViewHolder(View view) {
            super(view);
            dateTV = view.findViewById(R.id.adapter_studentTimeline_dateTV);
//            monthTV = view.findViewById(R.id.adapter_studentTimeline_MonthTV);
//            yearTV = view.findViewById(R.id.adapter_studentTimeline_yearTV);
            titleTV = view.findViewById(R.id.adapter_studentTimeline_titleTV);
            descTV = view.findViewById(R.id.adapter_studentTimeline_subtitleTV);
            lineView = view.findViewById(R.id.adapter_studentTimeline_line);
            horizontallineView = view.findViewById(R.id.adapter_studentTimeline_horizontalline);
            downloadBtn = view.findViewById(R.id.adapter_studentTimeline_downloadBtn);
            editBtn = view.findViewById(R.id.adapter_studentTimeline_editBtn);
            deleteBtn = view.findViewById(R.id.adapter_studentTimeline_deleteBtn);
            headLayout = view.findViewById(R.id.adapter_student_homework_headLayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_timeline, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.dateTV.setText(Utility.parseDate("yyyy-MM-dd", Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat"), timeLineDateList.get(position)));

        holder.titleTV.setText(timeLineTitleList.get(position));
        holder.descTV.setText(timeLineDescList.get(position));

        context.registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        if(timeLineDocumentList.get(position).equals("")){
            holder.downloadBtn.setVisibility(View.GONE);
        }else{
            holder.downloadBtn.setVisibility(View.VISIBLE);
            holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String urlStr = Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl);
                    urlStr += "uploads/student_timeline/"+timeLineDocumentList.get(position);

                    downloadID = Utility.beginDownload(context, timeLineDocumentList.get(position), urlStr);
                    Intent intent=new Intent(context.getApplicationContext(), OpenPdf.class);
                    intent.putExtra("imageUrl",urlStr);
                    context.startActivity(intent);
                }
            });
        }

        System.out.println("student_timeline=="+ Utility.getSharedPreferences(context.getApplicationContext(), "student_timeline"));
        student_timeline=Utility.getSharedPreferences(context.getApplicationContext(), "student_timeline");
        if(student_timeline.equals("enabled")){
            holder.editBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(context.getApplicationContext(), StudentEditTimeLine.class);
                    intent.putExtra("id",timeLineIdList.get(position));
                    intent.putExtra("date",timeLineDateList.get(position));
                    intent.putExtra("titles",timeLineTitleList.get(position));
                    intent.putExtra("description",timeLineDescList.get(position));
                    context.startActivity(intent);
                }
            });
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setCancelable(false);
                    builder.setMessage(R.string.deleteMsg);
                    builder.setTitle("");
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            if (Utility.isConnectingToInternet(context.getApplicationContext())) {
                                params.put("id", timeLineIdList.get(position));
                                JSONObject obj=new JSONObject(params);
                                Log.e("params ", obj.toString());
                                deleteDataFromApi(obj.toString());//Api Call
                                ((Activity)context).finish();
                            } else {
                                makeText(context.getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                            }


                            Intent intent = new Intent(context, StudentTimeline.class);
                            context.startActivity(intent);
                        }
                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }else{
            holder.editBtn.setVisibility(View.GONE);
            holder.deleteBtn.setVisibility(View.GONE);
        }

            holder.lineView.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
            holder.horizontallineView.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
            holder.headLayout.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));


    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void deleteDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(context.getApplicationContext(), "apiUrl")+Constants.deletetimelineUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);
                        String message = object.getString("msg");
                        pd.dismiss();
                        Toast.makeText(context.getApplicationContext()," "+message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(context, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(context.getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(context.getApplicationContext(), "accessToken"));
                Log.e("Headers", headers.toString());
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
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
        return timeLineIdList.size();
    }
}