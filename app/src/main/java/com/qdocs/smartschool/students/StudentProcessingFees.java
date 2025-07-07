package com.qdocs.smartschool.students;

import static android.widget.Toast.makeText;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.Login;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.TakeUrl;
import com.qdocs.smartschool.adapters.StudentProcessingFeesAdapter;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.DatabaseHelper;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StudentProcessingFees extends BaseActivity {

    RecyclerView feesList;
    StudentProcessingFeesAdapter adapter;
    TextView gtAmtTV, gtDiscountTV, gtFineTV, gtPaidTV, gtBalanceTV,gtamtfineTV;
    CardView grandTotalLay;
    ArrayList <String> feesIdList = new ArrayList<String>();
    ArrayList <String> feesCodeList = new ArrayList<String>();
    ArrayList <String> feesnameList = new ArrayList<String>();
    ArrayList <String> dueDateList = new ArrayList<String>();
    ArrayList <String> amtList = new ArrayList<String>();

    ArrayList <String> statusList = new ArrayList<String>();
    ArrayList <String> feesDepositIdList = new ArrayList<String>();
    ArrayList <String> feesSessionIdList = new ArrayList<String>();
    ArrayList <String> feesDetails = new ArrayList<String>();
    ArrayList <String> feesTypeId = new ArrayList<String>();
    ArrayList <String> feesCat = new ArrayList<String>();

    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    TextView headerTV;
    SwipeRefreshLayout pullToRefresh;
    CardView card_view_outer;
    String device_token;
    public Map<String, String> logoutparams = new Hashtable<String, String>();
    String is_offline_fee_payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.students_processfees_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        titleTV.setText(getApplicationContext().getString(R.string.processingfees));
        device_token = FirebaseInstanceId.getInstance().getToken()+"";
        Log.e(" logout DEVICE TOKEN", device_token);
       // makeText(this, Utility.getSharedPreferences(getApplicationContext(),Constants.currency), Toast.LENGTH_SHORT).show();
        feesList = (RecyclerView) findViewById(R.id.studentFees_listview);
        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        gtAmtTV = findViewById(R.id.fees_amtTV);
        gtamtfineTV = findViewById(R.id.fees_amtfineTV);
        gtDiscountTV = findViewById(R.id.fees_discountTV);
        gtFineTV = findViewById(R.id.fees_fineTV);
        gtPaidTV = findViewById(R.id.fees_paidTV);

        grandTotalLay = findViewById(R.id.feesAdapter_containerCV);

        if(Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.isLock)) {
            logout.setVisibility(View.VISIBLE);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StudentProcessingFees.this);
                    builder.setCancelable(false);
                    builder.setMessage(R.string.logoutMsg);
                    builder.setTitle("");
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            if (Utility.isConnectingToInternet(getApplicationContext())) {
                                logoutparams.put("deviceToken", device_token);
                                JSONObject obj=new JSONObject(logoutparams);
                                Log.e("params ", obj.toString());
                                System.out.println("Logout Details=="+obj.toString());
                                loginOutApi(obj.toString());
                            } else {
                                makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }else{
            logout.setVisibility(View.GONE);
        }
        headerTV = findViewById(R.id.fees_headTV);

        adapter = new StudentProcessingFeesAdapter(StudentProcessingFees.this, feesIdList, feesnameList,feesCodeList, dueDateList,
                 feesDepositIdList,feesSessionIdList, statusList, feesDetails, feesTypeId, feesCat);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        feesList.setLayoutManager(mLayoutManager);
        feesList.setItemAnimator(new DefaultItemAnimator());
        feesList.setAdapter(adapter);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                loaddata();
            }
        });

        loaddata();
        headerTV.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
    }
    @Override
    public void onRestart() {
        super.onRestart();
        loaddata();
        // do some stuff here
    }

    public  void  loaddata(){
        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            System.out.println("params== "+ obj.toString());
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

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getProcessingfeesUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                pullToRefresh.setRefreshing(false);
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        System.out.println("Result=="+ result);
                        JSONObject object = new JSONObject(result);

                        feesIdList.clear();
                        feesCodeList.clear();
                        dueDateList.clear();
                        amtList.clear();
                        feesDepositIdList.clear();
                        feesSessionIdList.clear();
                        feesTypeId.clear();
                        feesCat.clear();
                        statusList.clear();
                        feesDetails.clear();

                            String  currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
                            String  currency_price =  Utility.getSharedPreferences(getApplicationContext(), Constants.currency_price);
                            JSONObject grandTotalDetails = object.getJSONObject("grand_fee");

                            /*String amount= Utility.changeAmount(grandTotalDetails.getString("amount"), Utility.getSharedPreferences(getApplicationContext(), Constants.currency));
                            System.out.println("Amount=="+amount);*/
                            gtAmtTV.setText(currency + Utility.changeAmount(grandTotalDetails.getString("total_paid"),currency,currency_price));
                           //  gtAmtTV.setText(currency + Utility.changeAmount(grandTotalDetails.getString("fee_amount"),currency,currency_price));
                           // Double amount=Double.parseDouble(Utility.changeAmount(grandTotalDetails.getString("fee_fine"),currency,currency_price));
                            gtamtfineTV.setText("");
                            gtDiscountTV.setText(currency + Utility.changeAmount(grandTotalDetails.getString("fee_discount"),currency,currency_price));
                            gtFineTV.setText(currency + Utility.changeAmount(grandTotalDetails.getString("fee_fine"),currency,currency_price));
                            gtPaidTV.setText(currency + Utility.changeAmount(grandTotalDetails.getString("fee_paid"),currency,currency_price));


                                    JSONArray feesArray = object.getJSONArray("student_fee");

                                        for (int j = 0; j < feesArray.length(); j++) {
                                            feesIdList.add(feesArray.getJSONObject(j).getString("id"));
                                            feesnameList.add(feesArray.getJSONObject(j).getString("name") + "-" + feesArray.getJSONObject(j).getString("type"));
                                            feesCodeList.add(feesArray.getJSONObject(j).getString("code"));
                                            feesDepositIdList.add("");
                                            dueDateList.add(Utility.parseDate("yyyy-MM-dd", Utility.getSharedPreferences(getApplicationContext(), "dateFormat"),feesArray.getJSONObject(j).getString("due_date")));
                                            feesSessionIdList.add(feesArray.getJSONObject(j).getString("unique_id"));
                                            feesTypeId.add(feesArray.getJSONObject(j).getString("fee_groups_feetype_id"));
                                            feesCat.add("fees");
                                            statusList.add(getApplicationContext().getString(R.string.processing)+"...");
                                            System.out.println("statusList=" + statusList.size());
                                            JSONObject feesDetailsJson;
                                            try {
                                                feesDetailsJson = new JSONObject(feesArray.getJSONObject(j).getString("amount_detail"));
                                            } catch (JSONException e) {
                                                feesDetailsJson = new JSONObject();
                                            }
                                            feesDetails.add(feesDetailsJson.toString());
                                        }

                                    JSONArray transport_feesArray = object.getJSONArray("transport_fees");
                                        for (int i = 0; i < transport_feesArray.length(); i++) {
                                            feesIdList.add(transport_feesArray.getJSONObject(i).getString("id"));
                                            feesnameList.add(transport_feesArray.getJSONObject(i).getString("transport_fee"));
                                            feesCodeList.add(transport_feesArray.getJSONObject(i).getString("month"));
                                            feesDepositIdList.add("");
                                            dueDateList.add(Utility.parseDate("yyyy-MM-dd", Utility.getSharedPreferences(getApplicationContext(), "dateFormat"),transport_feesArray.getJSONObject(i).getString("due_date")));
                                            feesSessionIdList.add(transport_feesArray.getJSONObject(i).getString("unique_id"));
                                            feesTypeId.add("");
                                            feesCat.add("transport");
                                            statusList.add(getApplicationContext().getString(R.string.processing)+"...");
                                            JSONObject feesDetailsJson;
                                            try {
                                                feesDetailsJson = new JSONObject(transport_feesArray.getJSONObject(i).getString("amount_detail"));
                                            } catch (JSONException e) {
                                                feesDetailsJson = new JSONObject();
                                            }
                                            feesDetails.add(feesDetailsJson.toString());
                                        }
                                    adapter.notifyDataSetChanged();


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
                Toast.makeText(StudentProcessingFees.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentProcessingFees.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }
    private void loginOutApi (String bodyParams) {
        DatabaseHelper dataBaseHelpers = new DatabaseHelper(StudentProcessingFees.this);
        dataBaseHelpers.deleteAll() ;

        final ProgressDialog pd = new ProgressDialog(StudentProcessingFees.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(StudentProcessingFees.this, "apiUrl")+ Constants.logoutUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        String success = object.getString("status");
                        if (success.equals("1")) {
                            Utility.setSharedPreferenceBoolean(getApplicationContext(), "isLoggegIn", false);
                            Intent logout = new Intent(StudentProcessingFees.this, Login.class);
                            logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            logout.putExtra("EXIT", true);
                            startActivity(logout);
                            finish();
                        } else {
                            Intent intent=new Intent(StudentProcessingFees.this, TakeUrl.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(StudentProcessingFees.this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                // Toast.makeText(StudentDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
                Intent intent=new Intent(StudentProcessingFees.this,TakeUrl.class);
                startActivity(intent);
                finish();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(StudentProcessingFees.this, "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(StudentProcessingFees.this, "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentProcessingFees.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
