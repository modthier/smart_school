package com.qdocs.smartschool.students;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.adapters.StudentHostelAdapter;
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

public class StudentHostel extends BaseActivity {

    RecyclerView hostelListView;
    ArrayList<String> hostelIdList = new ArrayList<String>();
    ArrayList<String> hostelNameList = new ArrayList<String>();
    ArrayList<String> room_typeList = new ArrayList<String>();
    ArrayList<String> room_noList = new ArrayList<String>();
    ArrayList<String> no_of_bedList = new ArrayList<String>();
    ArrayList<String> cost_per_bedList = new ArrayList<String>();
    ArrayList<String> assignList = new ArrayList<String>();
    StudentHostelAdapter adapter;
    public Map<String, String>  headers = new HashMap<String, String>();
    public Map<String, String> params = new Hashtable<String, String>();
   CardView card_view_outer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_hostel_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        titleTV.setText(getApplicationContext().getString(R.string.hostel));

        hostelListView = (RecyclerView) findViewById(R.id.studentHostel_listview);
        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        adapter = new StudentHostelAdapter(StudentHostel.this, hostelIdList, hostelNameList,room_typeList,room_noList,no_of_bedList,cost_per_bedList,assignList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        hostelListView.setLayoutManager(mLayoutManager);
        hostelListView.setItemAnimator(new DefaultItemAnimator());
        hostelListView.setAdapter(adapter);


        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        }else{
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataFromApi(String bodyParams){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getHostelListUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);
                        JSONArray dataArray = object.getJSONArray("hostelarray");
                        String  currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
                        String  currency_price =  Utility.getSharedPreferences(getApplicationContext(), Constants.currency_price);
                        hostelIdList.clear();
                        hostelNameList.clear();
                        room_typeList.clear();
                        room_noList.clear();
                        no_of_bedList.clear();
                        cost_per_bedList.clear();
                        assignList.clear();
                        if(dataArray.length() != 0) {
                            for (int i = 0; i < dataArray.length(); i++) {
                                hostelIdList.add(dataArray.getJSONObject(i).getString("id"));
                                hostelNameList.add(dataArray.getJSONObject(i).getString("hostel_name"));
                                room_typeList.add(dataArray.getJSONObject(i).getString("room_type"));
                                room_noList.add(dataArray.getJSONObject(i).getString("room_no"));
                                no_of_bedList.add(dataArray.getJSONObject(i).getString("no_of_bed"));
                                cost_per_bedList.add(Utility.changeAmount(dataArray.getJSONObject(i).getString("cost_per_bed"),currency,currency_price));
                                assignList.add(dataArray.getJSONObject(i).getString("assign"));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Log.e("Volley Error", volleyError.toString());
                        Toast.makeText(StudentHostel.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentHostel.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
}
