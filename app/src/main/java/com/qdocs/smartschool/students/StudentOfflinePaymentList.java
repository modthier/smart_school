package com.qdocs.smartschool.students;

import static android.widget.Toast.makeText;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.adapters.OfflinePaymentAdapter;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public class StudentOfflinePaymentList extends BaseActivity {
    RecyclerView recyclerView;
    LinearLayout nodata_layout,data_layout;
    SwipeRefreshLayout pullToRefresh;
    ArrayList<String> amountlist = new ArrayList<String>();
    ArrayList<String> attachmentlist = new ArrayList<String>();
    ArrayList<String> transportfeesmonthlist = new ArrayList<String>();
    ArrayList<String> codelist = new ArrayList<String>();
    ArrayList<String> statusdateList = new ArrayList<String>();
    ArrayList<String> paymentdateList = new ArrayList<String>();
    ArrayList<String> idList = new ArrayList<String>();
    ArrayList<String> submitdateList = new ArrayList<String>();
    ArrayList<String> invoice_idlist = new ArrayList<String>();
    ArrayList<String> referencelist = new ArrayList<String>();
    ArrayList<String> paymentmodelist = new ArrayList<String>();
    ArrayList<String> paymentfromlist = new ArrayList<String>();
    ArrayList<String> fee_group_nameList = new ArrayList<String>();
    ArrayList<String> monthlist = new ArrayList<String>();
    ArrayList<String> routeList = new ArrayList<String>();
    ArrayList<String> is_activelist = new ArrayList<String>();
    ArrayList<String> replylist = new ArrayList<String>();
    ArrayList<String> arrays=new ArrayList<String>();
    OfflinePaymentAdapter offlinePaymentAdapter;
    public String defaultDatetimeFormat, currency;
    CardView card_view_outer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_offlinepayment, null, false);
        mDrawerLayout.addView(contentView, 0);
        defaultDatetimeFormat = Utility.getSharedPreferences(getApplicationContext(), "datetimeFormat");
        currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        titleTV.setText(getApplicationContext().getString(R.string.offlinebankpayment));
        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        recyclerView=findViewById(R.id.student_listview);
        nodata_layout = (LinearLayout) findViewById(R.id.nodata_layout);
        data_layout = (LinearLayout) findViewById(R.id.data_layout);
        loaddata();

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                loaddata();
            }
        });

        offlinePaymentAdapter = new OfflinePaymentAdapter(StudentOfflinePaymentList.this,idList,paymentdateList,submitdateList,statusdateList,amountlist,invoice_idlist,referencelist,paymentmodelist,paymentfromlist,fee_group_nameList,monthlist,routeList,is_activelist,replylist,attachmentlist,codelist,transportfeesmonthlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(offlinePaymentAdapter);

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

    private void getDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getOfflineBankPayments;
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
                        idList.clear();
                        submitdateList.clear();
                        paymentdateList.clear();
                        amountlist.clear();
                        invoice_idlist.clear();
                        statusdateList.clear();
                        referencelist.clear();
                        paymentmodelist.clear();
                        paymentfromlist.clear();
                        fee_group_nameList.clear();
                        monthlist.clear();
                        routeList.clear();
                        replylist.clear();
                        is_activelist.clear();
                        attachmentlist.clear();
                        codelist.clear();
                        transportfeesmonthlist.clear();

                        String  currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
                        String  currency_price =  Utility.getSharedPreferences(getApplicationContext(), Constants.currency_price);
                        if(dataArray.length() != 0) {
                            data_layout.setVisibility(View.VISIBLE);
                            nodata_layout.setVisibility(View.GONE);
                            for(int i = 0; i < dataArray.length(); i++) {
                                idList.add(dataArray.getJSONObject(i).getString("id"));
                                submitdateList.add(Utility.parseDate("yyyy-MM-dd HH:mm:ss", defaultDatetimeFormat,dataArray.getJSONObject(i).getString("submit_date")));
                                paymentdateList.add(Utility.parseDate("yyyy-MM-dd", defaultDateFormat,dataArray.getJSONObject(i).getString("payment_date")));
                                statusdateList.add(Utility.parseDate("yyyy-MM-dd HH:mm:ss", defaultDatetimeFormat,dataArray.getJSONObject(i).getString("approve_date")));
                                amountlist.add(currency + Utility.changeAmount(dataArray.getJSONObject(i).getString("amount"),currency,currency_price));
                                invoice_idlist.add(dataArray.getJSONObject(i).getString("invoice_id"));
                                referencelist.add(dataArray.getJSONObject(i).getString("reference"));
                                paymentmodelist.add(dataArray.getJSONObject(i).getString("bank_from"));
                                monthlist.add(dataArray.getJSONObject(i).getString("month"));
                                is_activelist.add(dataArray.getJSONObject(i).getString("is_active"));
                                replylist.add(dataArray.getJSONObject(i).getString("reply"));
                                attachmentlist.add(dataArray.getJSONObject(i).getString("attachment"));
                                codelist.add(dataArray.getJSONObject(i).getString("code"));
                                transportfeesmonthlist.add(dataArray.getJSONObject(i).getString("month"));
                                paymentfromlist.add(dataArray.getJSONObject(i).getString("bank_account_transferred"));
                                fee_group_nameList .add(dataArray.getJSONObject(i).getString("fee_group_name")+" ("+dataArray.getJSONObject(i).getString("type")+")");
                                routeList .add(dataArray.getJSONObject(i).getString("pickup_point")+" ("+dataArray.getJSONObject(i).getString("route_title")+")");
                            }
                            offlinePaymentAdapter.notifyDataSetChanged();
                        } else {
                            pullToRefresh.setVisibility(View.GONE);
                            data_layout.setVisibility(View.GONE);
                            nodata_layout.setVisibility(View.VISIBLE);
                         // Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.noData), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StudentOfflinePaymentList.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentOfflinePaymentList.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }
}
