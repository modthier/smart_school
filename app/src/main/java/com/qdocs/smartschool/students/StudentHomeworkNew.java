package com.qdocs.smartschool.students;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.qdocs.smartschool.BaseActivity ;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.adapters.StudentHomeworkAdapter;
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

public class StudentHomeworkNew extends BaseActivity {
    ColorStateList def;
    CardView card_view_outer;
    TextView pending,completed,select,evaluated;
    RecyclerView homeworkListView;
    ArrayList<String> homeworkIdList = new ArrayList<String>();
    ArrayList<String> homeworkTitleList = new ArrayList<String>();
    ArrayList<String> homeworkSubjectNameList = new ArrayList<String>();
    ArrayList<String> homeworkHomeworkDateList = new ArrayList<String>();
    ArrayList<String> homeworkSubmissionDateList = new ArrayList<String>();
    ArrayList<String> homeworkstatusList = new ArrayList<String>();
    ArrayList<String> homeworkEvaluationDateList = new ArrayList<String>();
    ArrayList<String> homeworkEvaluationByList = new ArrayList<String>();
    ArrayList<String> homeworkEvaluationMarksList = new ArrayList<String>();
    ArrayList<String> homeworkCreatedByList = new ArrayList<String>();
    ArrayList<String> homeworkDocumentList = new ArrayList<String>();
    ArrayList<String> homeworkClassList = new ArrayList<String>();
    ArrayList<String> homework_evaluation_idList = new ArrayList<String>();
    ArrayList<String> homeworknameList = new ArrayList<String>();
    ArrayList<String> marksList = new ArrayList<String>();
    ArrayList<String> noteList = new ArrayList<String>();
    ArrayList<String> created_byList = new ArrayList<String>();
    StudentHomeworkAdapter adapter;
    LinearLayout nodata_layout,data_layout;
    Spinner subjectlist_spinner;
    String subjectid;
    ArrayList<String>subjectlist=new ArrayList<String>();
    ArrayList<String>subjectidlist=new ArrayList<String>();
    SwipeRefreshLayout pullToRefresh;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    String status="pending";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_student_homework_new, null, false);
        mDrawerLayout.addView(contentView, 0);

        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getScannerDataFromApi(obj.toString());
        }else{
            makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
        titleTV.setText(getApplicationContext().getString(R.string.homework));

        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        subjectlist_spinner = findViewById(R.id.subjectlist_spinner);
        pending = findViewById(R.id.pending);
        completed = findViewById(R.id.completed);
        evaluated = findViewById(R.id.evaluated);
        select = findViewById(R.id.select);

        def=completed.getTextColors();
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status="pending";
                select.animate().x(0).setDuration(100);
                select.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_select));
                pending.setTextColor(Color.WHITE);
                completed.setTextColor(def);
                evaluated.setTextColor(def);
                if(Utility.isConnectingToInternet(StudentHomeworkNew.this)){
                    params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                    params.put("homework_status",status);
                    params.put("subject_group_subject_id","");
                    JSONObject obj=new JSONObject(params);
                    Log.e("params ", obj.toString());
                    getDataFromApi(obj.toString());

                }else{
                    makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status="submitted";
                int size=completed.getWidth();
                select.animate().x(size).setDuration(100);
                select.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.yellow_curve_border));
                completed.setTextColor(getResources().getColor(R.color.yellow));
                pending.setTextColor(def);
                evaluated.setTextColor(def);
                if(Utility.isConnectingToInternet(StudentHomeworkNew.this)){
                    params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                    params.put("homework_status",status);
                    params.put("subject_group_subject_id",subjectid);
                    JSONObject obj=new JSONObject(params);
                    Log.e("params ", obj.toString());
                    getDataFromApi(obj.toString());

                }else{
                    makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        evaluated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status="evaluated";
                int esize=evaluated.getWidth()*2;
                select.animate().x(esize).setDuration(100);
                select.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.green_curve_border));
                evaluated.setTextColor(Color.WHITE);
                pending.setTextColor(def);
                completed.setTextColor(def);
                if(Utility.isConnectingToInternet(StudentHomeworkNew.this)){
                    params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                    params.put("homework_status",status);
                    params.put("subject_group_subject_id",subjectid);
                    JSONObject obj=new JSONObject(params);
                    Log.e("params ", obj.toString());
                    getDataFromApi(obj.toString());

                }else{
                    makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        homeworkListView = (RecyclerView) findViewById(R.id.studentHomework_listview);
        nodata_layout = (LinearLayout) findViewById(R.id.nodata_layout);
        data_layout = (LinearLayout) findViewById(R.id.data_layout);
        adapter = new StudentHomeworkAdapter(StudentHomeworkNew.this, homeworkIdList, homeworkTitleList, homeworkSubjectNameList,
                homeworkHomeworkDateList, homeworkSubmissionDateList, homeworkEvaluationDateList, homeworkEvaluationByList,
                homeworkCreatedByList, homeworkDocumentList, homeworkClassList, homework_evaluation_idList,homeworkstatusList,homeworknameList,marksList,homeworkEvaluationMarksList,noteList,created_byList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        homeworkListView.setLayoutManager(mLayoutManager);
        homeworkListView.setItemAnimator(new DefaultItemAnimator());
        homeworkListView.setAdapter(adapter);

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                if(Utility.isConnectingToInternet(StudentHomeworkNew.this)){
                    params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                    params.put("homework_status",status);
                    params.put("subject_group_subject_id",subjectid);
                    JSONObject obj=new JSONObject(params);
                    Log.e("params ", obj.toString());
                    getDataFromApi(obj.toString());

                }else{
                    makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        subjectlist.add(getApplicationContext().getString(R.string.all));
        subjectidlist.add("");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(StudentHomeworkNew.this, android.R.layout.simple_spinner_item, subjectlist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectlist_spinner.setAdapter(dataAdapter);
        subjectlist_spinner.setSelection(0);
        subjectlist_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 subjectid = subjectidlist.get(i);

                    if (Utility.isConnectingToInternet(StudentHomeworkNew.this)) {
                        params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                        params.put("homework_status", status);
                        params.put("subject_group_subject_id", subjectid);
                        JSONObject obj = new JSONObject(params);
                        Log.e("params ", obj.toString());
                        getDataFromApi(obj.toString());
                    }else {
                        makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                    }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        loaddata();
    }

    public  void  loaddata(){
        if(Utility.isConnectingToInternet(StudentHomeworkNew.this)){
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
            params.put("homework_status","pending");
            params.put("subject_group_subject_id","");
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());

        }else{
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getHomeworkUrl;
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
                        JSONArray dataArray = obj.getJSONArray("homeworklist");

                        homeworkIdList.clear();
                        homeworkTitleList.clear();
                        homeworkSubjectNameList.clear();
                        homeworkHomeworkDateList.clear();
                        homeworkSubmissionDateList.clear();
                        homeworkCreatedByList.clear();
                        homeworkEvaluationByList.clear();
                        homeworkDocumentList.clear();
                        homeworkClassList.clear();
                        homeworkEvaluationDateList.clear();
                        homeworkstatusList.clear();
                        homework_evaluation_idList.clear();
                        homeworknameList.clear();
                        marksList.clear();
                        homeworkEvaluationMarksList.clear();
                        noteList.clear();
                        created_byList.clear();

                        if(dataArray.length() != 0) {
                            nodata_layout.setVisibility(View.GONE);
                            data_layout.setVisibility(View.VISIBLE);
                            for(int i = 0; i < dataArray.length(); i++) {
                                homeworkIdList.add(dataArray.getJSONObject(i).getString("id"));
                                homeworkTitleList.add(dataArray.getJSONObject(i).getString("description"));
                                homeworkSubjectNameList.add(dataArray.getJSONObject(i).getString("subject_name")+" ("+dataArray.getJSONObject(i).getString("subject_code")+")");
                                homeworkCreatedByList.add(dataArray.getJSONObject(i).getString("created_by_name")+" "+dataArray.getJSONObject(i).getString("created_by_surname")+" ("+dataArray.getJSONObject(i).getString("created_by_employee_id")+")");
                                homeworkHomeworkDateList.add(dataArray.getJSONObject(i).getString("homework_date"));
                                homeworkSubmissionDateList.add(dataArray.getJSONObject(i).getString("submit_date"));
                                homeworkstatusList.add(dataArray.getJSONObject(i).getString("status"));
                                homeworknameList.add(dataArray.getJSONObject(i).getString("name"));
                                marksList.add(dataArray.getJSONObject(i).getString("marks"));
                                noteList.add(dataArray.getJSONObject(i).getString("note"));
                                created_byList.add(dataArray.getJSONObject(i).getString("created_by"));
                                homeworkDocumentList.add(dataArray.getJSONObject(i).getString("document"));
                                homeworkClassList.add(dataArray.getJSONObject(i).getString("class") + " " + dataArray.getJSONObject(i).getString("section") );
                                homeworkEvaluationByList.add( dataArray.getJSONObject(i).getString("evaluated_by"));
                                homeworkEvaluationMarksList.add( dataArray.getJSONObject(i).getString("evaluation_marks"));
                                if(dataArray.getJSONObject(i).getString("evaluation_date").equals("0000-00-00")){
                                    homeworkEvaluationDateList.add("");
                                }else{
                                    homeworkEvaluationDateList.add( Utility.parseDate("yyyy-MM-dd", defaultDateFormat,dataArray.getJSONObject(i).getString("evaluation_date")));
                                }
                                homework_evaluation_idList.add(dataArray.getJSONObject(i).getString("homework_evaluation_id"));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            nodata_layout.setVisibility(View.VISIBLE);
                            data_layout.setVisibility(View.GONE);
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
                Toast.makeText(StudentHomeworkNew.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentHomeworkNew.this); //Creating a Request Queue
        requestQueue.add(stringRequest);//Adding request to the queue
    }

    private void getScannerDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getstudentsubjectUrl;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONArray dataArray = obj.getJSONArray("subjectlist");

                        if(dataArray.length() != 0) {
                            for(int i = 0; i < dataArray.length(); i++) {
                                subjectlist.add(dataArray.getJSONObject(i).getString("name")+" ("+dataArray.getJSONObject(i).getString("code")+")");
                                subjectidlist.add(dataArray.getJSONObject(i).getString("subject_group_subjects_id"));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(StudentHomeworkNew.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try  {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(StudentHomeworkNew.this);
        requestQueue.add(stringRequest);
    }
}
