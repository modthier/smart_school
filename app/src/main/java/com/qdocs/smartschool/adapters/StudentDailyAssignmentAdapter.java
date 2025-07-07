package com.qdocs.smartschool.adapters;

import static android.widget.Toast.makeText;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.smartschool.OpenPdf;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.students.StudentDailyAssignment;
import com.qdocs.smartschool.students.StudentEditAssignment;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StudentDailyAssignmentAdapter extends RecyclerView.Adapter<StudentDailyAssignmentAdapter.MyViewHolder> {

    private Context context;
    ArrayList<String> idList;
    ArrayList<String> subject_nameList;
    ArrayList<String> titleList;
    ArrayList<String> descriptionList;
    ArrayList<String> remarkList;
    ArrayList<String> submissiondateList;
    ArrayList<String> evaluation_dateList;
    ArrayList<String> attachmentList;
    ArrayList<String> subjectidList;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    long downloadID;


    public StudentDailyAssignmentAdapter(StudentDailyAssignment studentDailyAssignment, ArrayList<String> idList, ArrayList<String> subject_nameList, ArrayList<String> titleList
            , ArrayList<String> descriptionList , ArrayList<String> remarkList , ArrayList<String> submissiondateList , ArrayList<String> evaluation_dateList, ArrayList<String> attachmentList, ArrayList<String> subjectidList) {
        this.context = studentDailyAssignment;
        this.idList = idList;
        this.subject_nameList = subject_nameList;
        this.titleList = titleList;
        this.descriptionList = descriptionList;
        this.remarkList = remarkList;
        this.submissiondateList = submissiondateList;
        this.evaluation_dateList = evaluation_dateList;
        this.attachmentList = attachmentList;
        this.subjectidList = subjectidList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTV, subjectNameTV,remarkTV,submissiondateTV,evluationDateTV,descriptionTV;
        LinearLayout downloadBtn,header;
        ImageView editBtn,deleteBtn;

        public MyViewHolder(View view) {
            super(view);
            subjectNameTV = (TextView) view.findViewById(R.id.adapter_subjectNameTV);
            titleTV = (TextView) view.findViewById(R.id.adapter_titleTV);
            remarkTV = (TextView) view.findViewById(R.id.adapter_remarkTV);
            submissiondateTV = (TextView) view.findViewById(R.id.adapter_submissiondateTV);
            evluationDateTV = (TextView) view.findViewById(R.id.adapter_evluationDateTV);
            descriptionTV = (TextView) view.findViewById(R.id.adapter_description);
            downloadBtn = (LinearLayout) view.findViewById(R.id.adapter_downloadBtn);
            header = (LinearLayout) view.findViewById(R.id.adapter_headLayout);
            editBtn = (ImageView) view.findViewById(R.id.adapter_editBtn);
            deleteBtn = (ImageView) view.findViewById(R.id.adapter_deleteBtn);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_daily_assignment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String defaultDateFormat = Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat");
        //DECORATE
        holder.header.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));

        holder.subjectNameTV.setText(subject_nameList.get(position));
        holder.titleTV.setText(titleList.get(position));
        holder.remarkTV.setText(remarkList.get(position));
        holder.submissiondateTV.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat,submissiondateList.get(position)));
        holder.evluationDateTV.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat,evaluation_dateList.get(position)));
        holder.descriptionTV.setText(descriptionList.get(position));

        if(evaluation_dateList.get(position).equals("")){
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.editBtn.setVisibility(View.VISIBLE);
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
                                params.put("id", idList.get(position));
                                JSONObject obj=new JSONObject(params);
                                Log.e("params ", obj.toString());
                                deleteDataFromApi(obj.toString());//Api Call

                            } else {
                                makeText(context.getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                            }

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
            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, StudentEditAssignment.class);
                    intent.putExtra("title",titleList.get(position));
                    intent.putExtra("description",descriptionList.get(position));
                    intent.putExtra("id",idList.get(position));
                    intent.putExtra("subjectid",subjectidList.get(position));
                    context.startActivity(intent);
                }
            });
        }else{
            holder.deleteBtn.setVisibility(View.GONE);
            holder.editBtn.setVisibility(View.GONE);
        }


        context.registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        if(attachmentList.get(position).equals("")){
            holder.downloadBtn.setVisibility(View.GONE);
        }else{
            holder.downloadBtn.setVisibility(View.VISIBLE);
            holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String urlStr = Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl);
                    urlStr += "uploads/homework/daily_assignment/"+attachmentList.get(position);
                    downloadID = Utility.beginDownload(context, attachmentList.get(position), urlStr);
                     System.out.println("Download Url=="+urlStr);
                    Intent intent=new Intent(context.getApplicationContext(), OpenPdf.class);
                    intent.putExtra("imageUrl",urlStr);
                    context.startActivity(intent);
                }
            });

        }

    }

    private void deleteDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(context.getApplicationContext(), "apiUrl")+Constants.deletedailyassignmentUrl;
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
                        ((Activity)context).finish();
                        context.startActivity(((Activity) context).getIntent());
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
        return idList.size();
    }
}
