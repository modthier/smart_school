package com.qdocs.smartschool.students;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
import com.qdocs.smartschool.adapters.StudentApplyLeaveAdapter;
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

public class StudentAppyLeave extends BaseActivity {
    public String defaultDateFormat, currency;
    RecyclerView recyclerView;
    LinearLayout nodata_layout,data_layout;
    FloatingActionButton add_leave;
    StudentApplyLeaveAdapter adapter;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    SwipeRefreshLayout pullToRefresh;
    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<String> fromList = new ArrayList<String>();
    ArrayList<String> toList = new ArrayList<String>();
    ArrayList<String> statuslist = new ArrayList<String>();
    ArrayList<String> approve_datestatuslist = new ArrayList<String>();
    ArrayList<String> idlist = new ArrayList<String>();
    ArrayList<String> reasonlist = new ArrayList<String>();
    ArrayList<String> sfromlist = new ArrayList<String>();
    ArrayList<String> stolist = new ArrayList<String>();
    ArrayList<String> sapplylist = new ArrayList<String>();
    ArrayList<String> apply_datelist = new ArrayList<String>();
    ArrayList<String> docslist = new ArrayList<String>();
    Bitmap bitmap;
    CardView card_view_outer;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_student_apply_leave, null, false);
        mDrawerLayout.addView(contentView, 0);
        nodata_layout = findViewById(R.id.nodata_layout);
        data_layout = findViewById(R.id.data_layout);
        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        add_leave = findViewById(R.id.studentLeave_fab);
        add_leave.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour))));

        defaultDateFormat = Utility.getSharedPreferences(getApplicationContext(), "dateFormat");
        currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        titleTV.setText(getApplicationContext().getString(R.string.applyleave));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        nodata_layout = (LinearLayout) findViewById(R.id.nodata_layout);

        adapter = new StudentApplyLeaveAdapter(StudentAppyLeave.this, nameList, fromList,
                toList, statuslist, idlist,apply_datelist,docslist,reasonlist,sfromlist,stolist,sapplylist,approve_datestatuslist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        decorate();
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));


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
               Intent intent=new Intent(StudentAppyLeave.this,StudentAddLeave.class);
               startActivity(intent);
            }
        });
        loaddata();
    }

    public void loaddata() {
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
            JSONObject obj = new JSONObject(params);
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

    private void decorate() {
        actionBar.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        }
    }

    private void getDataFromApi(String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getApplyLeaveUrl;
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
                        JSONArray dataArray = obj.getJSONArray("result_array");
                        fromList.clear();
                        toList.clear();
                        statuslist.clear();
                        idlist.clear();
                        sfromlist.clear();
                        sapplylist.clear();
                        nameList.clear();
                        reasonlist.clear();
                        stolist.clear();
                        docslist.clear();
                        apply_datelist.clear();
                        approve_datestatuslist.clear();
                        if (dataArray.length() != 0) {
                            pullToRefresh.setVisibility(View.VISIBLE);
                            nodata_layout.setVisibility(View.GONE);
                            data_layout.setVisibility(View.VISIBLE);

                            for (int i = 0; i < dataArray.length(); i++) {
                                nameList.add(dataArray.getJSONObject(i).getString("firstname") +""+ dataArray.getJSONObject(i).getString("lastname"));
                                fromList.add(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dataArray.getJSONObject(i).getString("from_date")));
                                toList.add(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dataArray.getJSONObject(i).getString("to_date")));
                                statuslist.add(dataArray.getJSONObject(i).getString("status"));
                                approve_datestatuslist.add(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dataArray.getJSONObject(i).getString("approve_date")));
                                idlist.add(dataArray.getJSONObject(i).getString("id"));
                                reasonlist.add(dataArray.getJSONObject(i).getString("reason"));
                                apply_datelist.add(Utility.parseDate("yyyy-MM-dd",defaultDateFormat,dataArray.getJSONObject(i).getString("apply_date")));
                                docslist.add(dataArray.getJSONObject(i).getString("docs"));
                                sfromlist.add(dataArray.getJSONObject(i).getString("from_date"));
                                stolist.add(dataArray.getJSONObject(i).getString("to_date"));
                                sapplylist.add(dataArray.getJSONObject(i).getString("apply_date"));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            pullToRefresh.setVisibility(View.GONE);
                            nodata_layout.setVisibility(View.VISIBLE);
                            data_layout.setVisibility(View.GONE);
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
                Toast.makeText(StudentAppyLeave.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
                    VolleyLog.wtf("Unsupported Encoding while " +
                            "trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(StudentAppyLeave.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }

}
