package com.qdocs.smartschool.students;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.Login;
import com.qdocs.smartschool.SplashActivity;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.adapters.StudentTimelineAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import static android.widget.Toast.makeText;

public class StudentTimeline extends BaseActivity {
    StudentTimelineAdapter adapter;
    ArrayList<String> timeLineIdList = new ArrayList<>();
    ArrayList<String> timeLineDocumentList = new ArrayList<>();
    ArrayList<String> timeLineTitleList = new ArrayList<>();
    ArrayList<String> timeLineDescList = new ArrayList<>();
    ArrayList<String> timeLineDateList = new ArrayList<>();
    ArrayList<String> timeLineStatusList = new ArrayList<>();
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    CardView card_view_outer;
    FloatingActionButton add_leave;
    SwipeRefreshLayout pullToRefresh;
    String student_timeline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_timeline_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        if(Utility.isConnectingToInternet(StudentTimeline.this)){
            StudentTimelineStatus();
        }else{
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

        titleTV.setText(getApplicationContext().getString(R.string.timeLine));
        add_leave = findViewById(R.id.studentLeave_fab);
        add_leave.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour))));
        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        RecyclerView listView = findViewById(R.id.student_timeline_listview);
        adapter = new StudentTimelineAdapter(StudentTimeline.this, timeLineIdList, timeLineDocumentList,
                timeLineTitleList, timeLineDescList, timeLineDateList, timeLineStatusList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(adapter);
        listView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 0));

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                loaddata();
            }
        });

        add_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StudentTimeline.this,StudentAddTimeLine.class);
                startActivity(intent);
            }
        });

        loaddata();
    }

    public void loaddata() {
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("studentId", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
            JSONObject obj=new JSONObject(params);
            System.out.println("params timeline "+obj.toString());
            Log.e("params timeline ", obj.toString());
            getDataFromApi(obj.toString());
        } else {
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onRestart() {
        super.onRestart();
        loaddata();

    }

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getTimelineUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                pullToRefresh.setRefreshing(false);
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONArray dataArray = new JSONArray(result);
                        timeLineIdList.clear();
                        timeLineDocumentList.clear();
                        timeLineTitleList.clear();
                        timeLineDescList.clear();
                        timeLineDateList.clear();
                        timeLineStatusList.clear();
                        if (dataArray.length() != 0) {

                            for(int i = 0; i < dataArray.length(); i++) {
                                timeLineIdList.add(dataArray.getJSONObject(i).getString("id"));
                                timeLineDocumentList.add(dataArray.getJSONObject(i).getString("document"));
                                timeLineTitleList.add(dataArray.getJSONObject(i).getString("title"));
                                timeLineDescList.add(dataArray.getJSONObject(i).getString("description"));
                                timeLineDateList.add(dataArray.getJSONObject(i).getString("timeline_date"));
                                timeLineStatusList.add(dataArray.getJSONObject(i).getString("status"));
                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            pullToRefresh.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.noData), Toast.LENGTH_SHORT).show();
                        }
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
                        Toast.makeText(StudentTimeline.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentTimeline.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void StudentTimelineStatus() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getStudentTimelineStatusUrl;
        System.out.println("url=="+url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                pd.dismiss();
                try {
                    JSONObject object = new JSONObject(result);
                     student_timeline = object.getString("student_timeline");
                     Utility.setSharedPreference(getApplicationContext(),"student_timeline",student_timeline);
                    System.out.println("student_timeline="+student_timeline);
                    if(student_timeline.equals("enabled")){
                        add_leave.setVisibility(View.VISIBLE);
                    }else{
                        add_leave.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(StudentTimeline.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                Log.e("Headers", headers.toString());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(StudentTimeline.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue


    }

}
