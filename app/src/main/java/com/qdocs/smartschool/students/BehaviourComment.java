package com.qdocs.smartschool.students;

import static android.widget.Toast.makeText;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.adapters.BehaviourCommentAdapter;
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

public class BehaviourComment extends BaseActivity {
    String id;
    RecyclerView commentslist;
    EditText commentET;
    TextView savecomment;
    public Map<String, String>  headers = new HashMap<String, String>();
    public Map<String, String> params = new Hashtable<String, String>();
    ArrayList<String> namelist = new ArrayList<String>();
    ArrayList <String> datelist = new ArrayList<String>();
    ArrayList <String> messagelist = new ArrayList<String>();
    ArrayList <String> imagelist = new ArrayList<String>();
    ArrayList <String> typelist = new ArrayList<String>();
    ArrayList <String> idlist = new ArrayList<String>();
    String defaultDateTimeFormat,permission;
    LinearLayout comment_layout;

    BehaviourCommentAdapter studentCommentListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_behaviour_comment, null, false);
        mDrawerLayout.addView(contentView, 0);
        titleTV.setText(getApplicationContext().getString(R.string.comment));
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        permission = bundle.getString("permission");

        loaddata(id);
        commentET=findViewById(R.id.commentET);
        savecomment=findViewById(R.id.savecomment);
        comment_layout=findViewById(R.id.comment_layout);
        commentslist=findViewById(R.id.commentslist);
        studentCommentListAdapter = new BehaviourCommentAdapter(BehaviourComment.this,namelist,datelist,messagelist,imagelist,typelist,idlist);
        RecyclerView.LayoutManager mondayLayoutManager = new LinearLayoutManager(getApplicationContext());
        commentslist.setLayoutManager(mondayLayoutManager);
        commentslist.setItemAnimator(new DefaultItemAnimator());
        commentslist.setAdapter(studentCommentListAdapter);

        if(Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("student")){
            if(permission.equals("1")){
                comment_layout.setVisibility(View.GONE);
            }else {
                comment_layout.setVisibility(View.VISIBLE);
            }
        }else{
            if(permission.equals("0")){
                comment_layout.setVisibility(View.GONE);
            }else {
                comment_layout.setVisibility(View.VISIBLE);
            }
        }
        savecomment.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        savecomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commentET.getText().toString().equals("")){
                    makeText(BehaviourComment.this, getApplicationContext().getString(R.string.commentreq), Toast.LENGTH_SHORT).show();
                }else {
                    params.put("student_incident_id", id);
                    params.put("type", Utility.getSharedPreferences(getApplicationContext(), Constants.loginType));
                    params.put("comment", commentET.getText().toString());
                    params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                    JSONObject object = new JSONObject(params);
                    Log.e("params ", object.toString());
                    saveComment(object.toString());
                }
            }
        });

    }

    public  void  loaddata(String id) {
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("student_incident_id", id);
            JSONObject object = new JSONObject(params);
            Log.e("params ", object.toString());
            System.out.println("comment== " + object.toString());
            getCommentFromApi(object.toString());
        } else {
            makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
    }
    private void saveComment (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.addincidentcommentsUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                //pullToRefresh.setRefreshing(false);
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        if(obj.getString("msg").equals("Success")){
                            makeText(BehaviourComment.this, getApplicationContext().getString(R.string.submit_success), Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        }

                        commentET.setText("");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(BehaviourComment.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(BehaviourComment.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }

    private void getCommentFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getincidentcommentsUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                //pullToRefresh.setRefreshing(false);
               defaultDateTimeFormat = Utility.getSharedPreferences(getApplicationContext(), "datetimeFormat");
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONArray dataArray = obj.getJSONArray("messagelist");
                        namelist.clear();
                        datelist.clear();
                        imagelist.clear();
                        messagelist.clear();
                        typelist.clear();
                        idlist.clear();
                        if(dataArray.length() != 0) {
                            for(int i = 0; i < dataArray.length(); i++) {
                                typelist.add(dataArray.getJSONObject(i).getString("type"));
                                idlist.add(dataArray.getJSONObject(i).getString("id"));
                                if(dataArray.getJSONObject(i).getString("type").equals("student")) {
                                    imagelist.add(Utility.getSharedPreferences(getApplicationContext(), Constants.imagesUrl)+dataArray.getJSONObject(i).getString("student_image"));
                                    namelist.add(dataArray.getJSONObject(i).getString("firstname") + " " + dataArray.getJSONObject(i).getString("middlename")+" "+dataArray.getJSONObject(i).getString("lastname")+" ("+ dataArray.getJSONObject(i).getString("admission_no")+") - Student");
                                }else if(dataArray.getJSONObject(i).getString("type").equals("parent")) {
                                    imagelist.add(Utility.getSharedPreferences(getApplicationContext(), Constants.imagesUrl)+dataArray.getJSONObject(i).getString("student_image"));
                                    namelist.add(dataArray.getJSONObject(i).getString("firstname") + " " + dataArray.getJSONObject(i).getString("middlename")+" "+dataArray.getJSONObject(i).getString("lastname")+" ("+ dataArray.getJSONObject(i).getString("admission_no")+") - Guardian");
                                }else{
                                    System.out.println("image url=="+Utility.getSharedPreferences(getApplicationContext(), Constants.imagesUrl)+"uploads/staff_images/"+dataArray.getJSONObject(i).getString("staff_image"));
                                    imagelist.add(Utility.getSharedPreferences(getApplicationContext(), Constants.imagesUrl)+"uploads/staff_images/"+dataArray.getJSONObject(i).getString("staff_image"));
                                    namelist.add(dataArray.getJSONObject(i).getString("staff_name") + " " + dataArray.getJSONObject(i).getString("staff_surname")+" ("+ dataArray.getJSONObject(i).getString("staff_employee_id")+") - "+dataArray.getJSONObject(i).getString("role_name"));
                                }
                                messagelist.add(dataArray.getJSONObject(i).getString("comment"));
                                datelist.add(Utility.parseDate("yyyy-MM-dd HH:mm:ss", defaultDateTimeFormat, dataArray.getJSONObject(i).getString("created_date")));
                            }
                            studentCommentListAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(BehaviourComment.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(BehaviourComment.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }

}