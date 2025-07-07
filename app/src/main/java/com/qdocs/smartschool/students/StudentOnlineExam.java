package com.qdocs.smartschool.students;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.adapters.StudentOnlineExamListAdapter;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import static android.widget.Toast.makeText;

public class StudentOnlineExam extends BaseActivity {

    public String defaultDatetimeFormat, currency;
    RecyclerView recyclerView;
    LinearLayout nodata_layout;
    StudentOnlineExamListAdapter adapter;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    SwipeRefreshLayout pullToRefresh;

    ArrayList<String> examList = new ArrayList<String>();
    ArrayList<String> exam_fromList = new ArrayList<String>();
    ArrayList<String> exam_toList = new ArrayList<String>();
    ArrayList<String> durationList = new ArrayList<String>();
    ArrayList<String> attemptList = new ArrayList<String>();
    ArrayList<String> attemptslist = new ArrayList<String>();
    ArrayList<String> onlineexam_idlist = new ArrayList<String>();
    ArrayList<String> onlineexam_student_idlist = new ArrayList<String>();
    ArrayList<String> publish_resultlist = new ArrayList<String>();
    ArrayList<String> is_submittedlist = new ArrayList<String>();
    ArrayList<String> is_marks_displaylist = new ArrayList<String>();
    ArrayList<String> is_neg_markinglist = new ArrayList<String>();
    ArrayList<String> passing_percentagelist = new ArrayList<String>();
    ArrayList<String> total_questionlist = new ArrayList<String>();
    ArrayList<String> total_descriptivelist = new ArrayList<String>();
    ArrayList<String> answer_word_countlist = new ArrayList<String>();
    ArrayList<String> descriptionlist = new ArrayList<String>();
    ArrayList<String> is_quizlist = new ArrayList<String>();
    ArrayList<String> attemptedlist = new ArrayList<String>();
    CardView card_view_outer;
    TextView pending,completed,select;
    ColorStateList def;
    String status="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_student_online_exam, null, false);
        mDrawerLayout.addView(contentView, 0);
        loaddata();
        defaultDatetimeFormat = Utility.getSharedPreferences(getApplicationContext(), "datetimeFormat");
        currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        pending = findViewById(R.id.pending);
        completed = findViewById(R.id.completed);

        titleTV.setText(getApplicationContext().getString(R.string.onlineexam));
        card_view_outer = (CardView) findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        nodata_layout = (LinearLayout) findViewById(R.id.nodata_layout);
        adapter = new StudentOnlineExamListAdapter(StudentOnlineExam.this,recyclerView,examList,exam_fromList,
                exam_toList,durationList,attemptList,attemptslist,onlineexam_idlist,publish_resultlist,is_submittedlist,
                onlineexam_student_idlist,is_quizlist,attemptedlist,is_marks_displaylist,is_neg_markinglist,passing_percentagelist,total_questionlist,total_descriptivelist,answer_word_countlist,descriptionlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        select = findViewById(R.id.select);

        def=completed.getTextColors();
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status="";
                select.animate().x(0).setDuration(100);
                select.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_select));
                pending.setTextColor(Color.WHITE);
                completed.setTextColor(def);
                if (Utility.isConnectingToInternet(getApplicationContext())) {
                    params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                    params.put("exam_type", status);
                    JSONObject obj=new JSONObject(params);
                    Log.e("params ", obj.toString());
                    getDataFromApi(obj.toString());
                } else {
                    makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status="closed";
                int size=completed.getWidth();
                select.animate().x(size).setDuration(100);
                select.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.yellow_curve_border));
                completed.setTextColor(getResources().getColor(R.color.yellow));
                pending.setTextColor(def);
                if (Utility.isConnectingToInternet(getApplicationContext())) {
                    params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                    params.put("exam_type", status);
                    JSONObject obj=new JSONObject(params);
                    Log.e("params ", obj.toString());
                    getDataFromApi(obj.toString());
                } else {
                    makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });



        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                loaddata();
            }
        });
        loaddata();
    }


    public  void  loaddata(){
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
            params.put("exam_type", status);
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        } else {
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();   // call super back pressed method
    }

    @Override
    public void onRestart() {
        super.onRestart();
        loaddata();
        // do some stuff here
    }

    private void getDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getOnlineExamUrl;
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
                        JSONArray dataArray = obj.getJSONArray("onlineexam");
                        examList.clear();
                        attemptList.clear();
                        examList.clear();
                        exam_fromList.clear();
                        durationList.clear();
                        exam_toList.clear();
                        attemptslist.clear();
                        onlineexam_idlist.clear();
                        onlineexam_student_idlist.clear();
                        publish_resultlist.clear();
                        is_submittedlist.clear();
                        is_quizlist.clear();
                        attemptedlist.clear();
                        descriptionlist.clear();
                        answer_word_countlist.clear();
                        if(dataArray.length() != 0) {
                            pullToRefresh.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            nodata_layout.setVisibility(View.GONE);
                            for(int i = 0; i < dataArray.length(); i++) {
                                examList.add(dataArray.getJSONObject(i).getString("exam"));
                                durationList.add(dataArray.getJSONObject(i).getString("duration"));
                                attemptList .add(dataArray.getJSONObject(i).getString("attempt"));
                                exam_fromList.add(dataArray.getJSONObject(i).getString("exam_from"));
                                exam_toList.add(dataArray.getJSONObject(i).getString("exam_to"));
                                attemptslist.add(dataArray.getJSONObject(i).getString("counter"));
                                is_quizlist.add(dataArray.getJSONObject(i).getString("is_quiz"));
                                attemptedlist.add(dataArray.getJSONObject(i).getString("is_attempted"));
                                onlineexam_idlist.add(dataArray.getJSONObject(i).getString("id"));
                                onlineexam_student_idlist.add(dataArray.getJSONObject(i).getString("onlineexam_student_id"));
                                publish_resultlist.add(dataArray.getJSONObject(i).getString("publish_result"));
                                is_submittedlist.add("");
                                is_marks_displaylist.add(dataArray.getJSONObject(i).getString("is_marks_display"));
                                is_neg_markinglist.add(dataArray.getJSONObject(i).getString("is_neg_marking"));
                                passing_percentagelist.add(dataArray.getJSONObject(i).getString("passing_percentage"));
                                total_questionlist.add(dataArray.getJSONObject(i).getString("total_question"));
                                total_descriptivelist.add(dataArray.getJSONObject(i).getString("total_descriptive"));
                                answer_word_countlist.add(dataArray.getJSONObject(i).getString("answer_word_count"));
                                descriptionlist.add(dataArray.getJSONObject(i).getString("description"));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            pullToRefresh.setVisibility(View.GONE);
                            nodata_layout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            //Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.noData), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StudentOnlineExam.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentOnlineExam.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }
}
