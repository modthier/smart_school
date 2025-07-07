package com.qdocs.smartschool.students;

import static android.widget.Toast.makeText;
import android.app.ProgressDialog;
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
import com.qdocs.smartschool.adapters.CbseTimeTableAdapter;
import com.qdocs.smartschool.model.CbseExamTimeTableModel;
import com.qdocs.smartschool.model.CbseTimetableModel;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public class CbseTimeTableActivity extends BaseActivity {
    RecyclerView recyclerview;
    CardView card_view_outer;
    CbseTimeTableAdapter cbseTimeTableAdapter;
    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<String> exam_total_markslist = new ArrayList<String>();
    ArrayList<String> exam_percentageList = new ArrayList<String>();
    ArrayList<String> exam_gradelist = new ArrayList<String>();
    ArrayList<String> exam_obtain_markslist = new ArrayList<String>();
    ArrayList<String> subjectlist = new ArrayList<String>();
    ArrayList<CbseExamTimeTableModel> cbseexamlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_cbsetimetable, null, false);
        mDrawerLayout.addView(contentView, 0);

        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        defaultDateFormat = Utility.getSharedPreferences(getApplicationContext(), "dateFormat");
        currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        titleTV.setText(getApplicationContext().getString(R.string.examSchedule));

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        recyclerview=findViewById(R.id.recyclerview);
        cbseTimeTableAdapter = new CbseTimeTableAdapter(CbseTimeTableActivity.this,
                cbseexamlist,null);
        recyclerview.setLayoutManager(new LinearLayoutManager(CbseTimeTableActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(cbseTimeTableAdapter);


        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("student_session_id", Utility.getSharedPreferences(getApplicationContext(), Constants.student_session_id));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        }else{
            makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

    }

    private void getDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.cbseexamtimetableUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONArray dataArray = obj.getJSONArray("result");
                        nameList.clear();
                        exam_total_markslist.clear();
                        exam_percentageList.clear();
                        exam_gradelist.clear();
                        exam_obtain_markslist.clear();
                        subjectlist.clear();

                        if(dataArray.length() != 0) {
                            for(int i = 0; i < dataArray.length(); i++) {
                                CbseExamTimeTableModel cbseExamTimeTableModel = new CbseExamTimeTableModel();
                                cbseExamTimeTableModel.setName(dataArray.getJSONObject(i).getString("name"));

                                JSONArray time_tableArray = dataArray.getJSONObject(i).getJSONArray("time_table");
                                ArrayList<CbseTimetableModel> time_tableList = new ArrayList<>();
                                for(int k = 0; k<time_tableArray.length(); k++) {
                                    CbseTimetableModel cbseTimetableModel=new CbseTimetableModel();
                                    cbseTimetableModel.setDuration(time_tableArray.getJSONObject(k).getString("duration"));
                                    cbseTimetableModel.setDate(time_tableArray.getJSONObject(k).getString("date"));
                                    cbseTimetableModel.setRoom_no(time_tableArray.getJSONObject(k).getString("room_no"));
                                    cbseTimetableModel.setTime_from(time_tableArray.getJSONObject(k).getString("time_from"));
                                    if(time_tableArray.getJSONObject(k).getString("subject_code").equals("")){
                                        cbseTimetableModel.setSubject_name(time_tableArray.getJSONObject(k).getString("subject_name"));
                                    }else {
                                        cbseTimetableModel.setSubject_name(time_tableArray.getJSONObject(k).getString("subject_name") + " (" + time_tableArray.getJSONObject(k).getString("subject_code") + ")");
                                    }
                                    time_tableList.add(cbseTimetableModel);
                                }

                                cbseExamTimeTableModel.setTime_table(time_tableList);
                                cbseexamlist.add(cbseExamTimeTableModel);
                            }
                            cbseTimeTableAdapter.notifyDataSetChanged();
                        } else {


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
                Toast.makeText(CbseTimeTableActivity.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(CbseTimeTableActivity.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }
}