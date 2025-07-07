package com.qdocs.smartschool.adapters;

import static android.widget.Toast.makeText;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qdocs.smartschool.OpenPdf;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.students.CbseExaminationActivity;
import com.qdocs.smartschool.students.StudentVideoTutorialPlay;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StudentDownloadsAdapter extends BaseAdapter {
    long downloadID;
    private FragmentActivity context;
    private ArrayList<String> idList;
    private ArrayList<String> nameList;
    private ArrayList<String> sharedateList;
    private ArrayList<String> valid_uptoList;
    private ArrayList<String> sharebyList;
    private ArrayList<String> descriptionList;
    private ArrayList<String> uploaddateList;
    private ArrayList<String> checkdate;
    private ArrayList<String> created_bylist;
    String urlStr,videoUrl,title,thumbpath;
    RecyclerView recyclerView;
    StudentContentUploadAdapter adapter;
    public String defaultDateFormat,superadmin_restriction;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    public StudentDownloadsAdapter(FragmentActivity activity, ArrayList<String> idList, ArrayList<String> nameList,
                                   ArrayList<String> sharedateList, ArrayList<String> sharebyList, ArrayList<String> valid_uptoList, ArrayList<String> descriptionList, ArrayList<String> uploaddateList, ArrayList<String> checkdate, ArrayList<String> created_bylist) {

        this.context = activity;
        this.idList = idList;
        this.nameList = nameList;
        this.sharedateList = sharedateList;
        this.sharebyList = sharebyList;
        this.valid_uptoList = valid_uptoList;
        this.descriptionList = descriptionList;
        this.uploaddateList = uploaddateList;
        this.checkdate = checkdate;
        this.created_bylist = created_bylist;

    }
    @Override
    public int getCount() {
        return nameList.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }
    ArrayList<String> idlist = new ArrayList<String>();
    ArrayList<String> real_namelist = new ArrayList<String>();
    ArrayList<String> file_typelist = new ArrayList<String>();
    ArrayList<String> vid_urllist = new ArrayList<String>();
    ArrayList<String> img_namelist = new ArrayList<String>();
    ArrayList<String> dir_pathlist = new ArrayList<String>();
    ContentDocumentListAdapter contentDocumentListAdapter;
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        defaultDateFormat = Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat");
        superadmin_restriction = Utility.getSharedPreferences(context.getApplicationContext(), Constants.superadmin_restriction);

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.adapter_fragment_studentdownload, viewGroup, false);


        LinearLayout nameHeader = view.findViewById(R.id.headLayout);
        LinearLayout attachmentdetail = view.findViewById(R.id.attachmentdetail);


        TextView sharedate = (TextView) view.findViewById(R.id.sharedate);
        TextView valid_upto = (TextView) view.findViewById(R.id.valid_upto);
        TextView shareby = (TextView) view.findViewById(R.id.shareby);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView uploaddate = (TextView) view.findViewById(R.id.uploaddate);
        TextView titlename = (TextView) view.findViewById(R.id.title);

        sharedate.setTag(position);

        sharedate.setText(sharedateList.get(position));


        description.setText(descriptionList.get(position));
        uploaddate.setText(uploaddateList.get(position));
        titlename.setText(nameList.get(position));

        if(superadmin_restriction.equals("enabled")){
            shareby.setText(sharebyList.get(position));
        }else{
            if(created_bylist.get(position).equals("1")){
                shareby.setText("");
            }else{
                shareby.setText(sharebyList.get(position));
            }
        }

        try {
            if (new SimpleDateFormat("yyyy-MM-dd").parse(checkdate.get(position)).before(new Date())) {
                valid_upto.setText(valid_uptoList.get(position)+" Expires");

            }else{
                valid_upto.setText(valid_uptoList.get(position));

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        nameHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));

        attachmentdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog(idList.get(position));
            }
        });
        return view;
    }

    private void showAddDialog(String id) {

        View view = context.getLayoutInflater().inflate(R.layout.attachment_detail_dialog, null);
        view.setMinimumHeight(500);

        RelativeLayout headerLay = (RelativeLayout)view.findViewById(R.id.addTask_dialog_header);
        RecyclerView recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        ImageView closeBtn = (ImageView) view.findViewById(R.id.addTask_dialog_crossIcon);

        if(Utility.isConnectingToInternet(context)){
            params.put("id", id);
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            System.out.println("params== "+ obj.toString());
            getDataFromApi(obj.toString());

        }else{
            makeText(context, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

        contentDocumentListAdapter = new ContentDocumentListAdapter(context,idlist,
                real_namelist,file_typelist,vid_urllist,img_namelist,dir_pathlist);
        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(contentDocumentListAdapter);
        headerLay.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.primaryColour)));
        final BottomSheetDialog dialog = new BottomSheetDialog(context);

        dialog.setContentView(view);
        dialog.show();

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(context.getApplicationContext(), "apiUrl")+ Constants.getDownloadsLinksByIdUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        System.out.println("Result=="+result);
                        JSONArray jsonArray = new JSONArray(result);
                        idlist.clear();
                        real_namelist.clear();
                        file_typelist.clear();
                        vid_urllist.clear();
                        img_namelist.clear();
                        dir_pathlist.clear();

                        for(int j = 0; j < jsonArray.length(); j++) {
                            idlist.add(jsonArray.getJSONObject(j).getString("id"));
                            real_namelist.add(jsonArray.getJSONObject(j).getString("real_name"));
                            file_typelist.add(jsonArray.getJSONObject(j).getString("file_type"));
                            vid_urllist.add(jsonArray.getJSONObject(j).getString("vid_url"));
                            img_namelist.add(jsonArray.getJSONObject(j).getString("img_name"));
                            dir_pathlist.add(jsonArray.getJSONObject(j).getString("dir_path"));
                        }
                        contentDocumentListAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(context.getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
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
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(stringRequest);
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
}