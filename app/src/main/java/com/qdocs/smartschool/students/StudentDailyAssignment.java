package com.qdocs.smartschool.students;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.adapters.StudentDailyAssignmentAdapter;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import static android.widget.Toast.makeText;

public class StudentDailyAssignment extends BaseActivity {
    RecyclerView documentListView;
    StudentDailyAssignmentAdapter adapter;
    SwipeRefreshLayout pullToRefresh;
    ArrayList<String> idList = new ArrayList<>();
    ArrayList<String> subject_nameList = new ArrayList<>();
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> descriptionList = new ArrayList<>();
    ArrayList<String> remarkList = new ArrayList<>();
    ArrayList<String> submissiondateList = new ArrayList<>();
    ArrayList<String> evaluation_dateList = new ArrayList<>();
    ArrayList<String> attachmentList = new ArrayList<>();
    ArrayList<String> subjectidList = new ArrayList<>();
    FloatingActionButton add_assignment;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    CardView card_view_outer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.daily_assignment_activity, null, false);
        mDrawerLayout.addView(contentView, 0);
        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));

        titleTV.setText(getApplicationContext().getString(R.string.daily_assignment));
        add_assignment = findViewById(R.id.add_assignment);
        add_assignment.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour))));
        add_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), StudentAddAssignment.class);
                startActivity(intent);
            }
        });
        documentListView = findViewById(R.id.studentDocument_listview);

        adapter = new StudentDailyAssignmentAdapter(StudentDailyAssignment.this,idList,subject_nameList,titleList,descriptionList,
                remarkList,submissiondateList,evaluation_dateList, attachmentList,subjectidList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        documentListView.setLayoutManager(mLayoutManager);
        documentListView.setItemAnimator(new DefaultItemAnimator());
        documentListView.setAdapter(adapter);
        loaddata();

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                loaddata();
            }
        });
    }

    public  void  loaddata(){
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
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

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getdailyassignmentUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                pullToRefresh.setRefreshing(false);
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONArray dataArray = obj.getJSONArray("dailyassignment");
                        idList.clear();
                        subject_nameList.clear();
                        titleList.clear();
                        descriptionList.clear();
                        remarkList.clear();
                        submissiondateList.clear();
                        evaluation_dateList.clear();
                        attachmentList.clear();
                        subjectidList.clear();
                        if (dataArray.length() != 0) {
                            for(int i = 0; i < dataArray.length(); i++) {
                                idList.add(dataArray.getJSONObject(i).getString("id"));
                                subject_nameList.add(dataArray.getJSONObject(i).getString("subject_name")+" ("+dataArray.getJSONObject(i).getString("subject_code")+")");
                                titleList.add(dataArray.getJSONObject(i).getString("title"));
                                descriptionList.add(dataArray.getJSONObject(i).getString("description"));
                                remarkList.add(dataArray.getJSONObject(i).getString("remark"));
                                submissiondateList.add(dataArray.getJSONObject(i).getString("date"));
                                evaluation_dateList.add(dataArray.getJSONObject(i).getString("evaluation_date"));
                                attachmentList.add(dataArray.getJSONObject(i).getString("attachment"));
                                subjectidList.add(dataArray.getJSONObject(i).getString("subject_group_subject_id"));
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
                    pullToRefresh.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Log.e("Volley Error", volleyError.toString());
                        Toast.makeText(StudentDailyAssignment.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentDailyAssignment.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


}
