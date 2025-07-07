package com.qdocs.smartschool.students;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.Login;
import com.qdocs.smartschool.TakeUrl;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.DatabaseHelper;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.adapters.StudentFeesAdapter;
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

public class StudentFees extends BaseActivity {

    RecyclerView feesList;
    StudentFeesAdapter adapter;
    TextView gtAmtTV, gtDiscountTV, gtFineTV, gtPaidTV, gtBalanceTV,gtamtfineTV;
    CardView grandTotalLay;
    ArrayList <String> feesIdList = new ArrayList<String>();
    ArrayList <String> feesCodeList = new ArrayList<String>();
    ArrayList <String> feesnameList = new ArrayList<String>();
    ArrayList <String> dueDateList = new ArrayList<String>();
    ArrayList <String> amtList = new ArrayList<String>();
    ArrayList <String> amtfineList = new ArrayList<String>();
    ArrayList <String> paidAmtList = new ArrayList<String>();
    ArrayList <String> discAmtList = new ArrayList<String>();
    ArrayList <String> fineAmtList = new ArrayList<String>();
    ArrayList <String> balanceAmtList = new ArrayList<String>();
    ArrayList <String> statusList = new ArrayList<String>();
    ArrayList <String> feesDepositIdList = new ArrayList<String>();
    ArrayList <String> feesSessionIdList = new ArrayList<String>();
    ArrayList <String> feesDetails = new ArrayList<String>();
    ArrayList <String> feesTypeId = new ArrayList<String>();
    ArrayList <String> feesCat = new ArrayList<String>();
    ArrayList <String> discountNameList = new ArrayList<String>();
    ArrayList <String> discountAmtList = new ArrayList<String>();
    ArrayList <String> discountpayment_idList = new ArrayList<String>();
    ArrayList <String> discountStatusList = new ArrayList<String>();
    ArrayList <String> transportfeesnameList = new ArrayList<String>();
    ArrayList <String> transportdueDateList = new ArrayList<String>();
    ArrayList <String> transportamtList = new ArrayList<String>();
    ArrayList <String> transportamtfineList = new ArrayList<String>();
    ArrayList <String> transportpaidAmtList = new ArrayList<String>();
    ArrayList <String> transportdiscAmtList = new ArrayList<String>();
    ArrayList <String> transportfineAmtList = new ArrayList<String>();
    ArrayList <String> transportbalanceAmtList = new ArrayList<String>();
    ArrayList <String> transportfeesDepositIdList = new ArrayList<String>();
    ArrayList <String> transportstatusList = new ArrayList<String>();
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    TextView headerTV,offlinePayment,processingfees,fees;
    SwipeRefreshLayout pullToRefresh;
    CardView card_view_outer;
    String device_token;
    public Map<String, String> logoutparams = new Hashtable<String, String>();
    String is_offline_fee_payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.students_fees_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        titleTV.setText(getApplicationContext().getString(R.string.fees));
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
        gtBalanceTV = findViewById(R.id.fees_balance);
        grandTotalLay = findViewById(R.id.feesAdapter_containerCV);
        fees = findViewById(R.id.fees);
        processingfees = findViewById(R.id.processingfees);
        offlinePayment = findViewById(R.id.offlinePayment);
        offlinePayment.setVisibility(View.VISIBLE);
        if(Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.isLock)) {
            logout.setVisibility(View.VISIBLE);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StudentFees.this);
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

        if(Utility.isConnectingToInternet(StudentFees.this)){
            StudentOfflineStatus();
        }else{
            makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }


        headerTV = findViewById(R.id.fees_headTV);

        adapter = new StudentFeesAdapter(StudentFees.this, feesIdList, feesnameList,feesCodeList, dueDateList, amtList,
                paidAmtList, balanceAmtList, feesDepositIdList,feesSessionIdList, statusList, feesDetails, feesTypeId, feesCat,
                discountNameList, discountAmtList, discountStatusList,discountpayment_idList,discAmtList,fineAmtList,amtfineList,
        transportdueDateList, transportamtfineList, transportpaidAmtList, transportdiscAmtList, transportbalanceAmtList, transportfeesDepositIdList,transportamtList,transportfineAmtList,transportstatusList);
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
        offlinePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StudentFees.this,StudentOfflinePaymentList.class);
                startActivity(intent);
            }
        });

        processingfees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StudentFees.this,StudentProcessingFees.class);
                startActivity(intent);
            }
        });
        loaddata();
        headerTV.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
    }
    @Override
    public void onRestart() {
        super.onRestart();
        loaddata();
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        // do some stuff here
    }

    public  void  loaddata(){
        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
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

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getFeesUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                pullToRefresh.setRefreshing(false);
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        feesIdList.clear();
                        feesCodeList.clear();
                        dueDateList.clear();
                        amtList.clear();
                        paidAmtList.clear();
                        discAmtList.clear();
                        fineAmtList.clear();
                        balanceAmtList.clear();
                        feesDepositIdList.clear();
                        feesSessionIdList.clear();
                        feesTypeId.clear();
                        feesCat.clear();
                        statusList.clear();
                        feesDetails.clear();
                        amtfineList.clear();
                        transportdueDateList.clear();
                        transportamtList.clear();
                        transportamtfineList.clear();
                        transportpaidAmtList.clear();
                        transportdiscAmtList.clear();
                        transportfineAmtList.clear();
                        transportbalanceAmtList.clear();
                        transportfeesDepositIdList.clear();
                        transportstatusList.clear();

                        String success = "1";
                        if (success.equals("1")) {
                            if(object.getString("pay_method").equals("0")) {
                                Log.e("test", "testing");
                                Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.showPaymentBtn, false);
                            } else {
                                Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.showPaymentBtn, true);
                            }
                            String  currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
                            String  currency_price =  Utility.getSharedPreferences(getApplicationContext(), Constants.currency_price);
                            JSONObject grandTotalDetails = object.getJSONObject("grand_fee");

                            /*String amount= Utility.changeAmount(grandTotalDetails.getString("amount"), Utility.getSharedPreferences(getApplicationContext(), Constants.currency));
                            System.out.println("Amount=="+amount);*/
                            gtAmtTV.setText(currency + Utility.changeAmount(grandTotalDetails.getString("amount"),currency,currency_price));
                            gtamtfineTV.setText(currency + Utility.changeAmount(grandTotalDetails.getString("fee_fine"),currency,currency_price));
                            gtDiscountTV.setText(currency + Utility.changeAmount(grandTotalDetails.getString("amount_discount"),currency,currency_price));
                            gtFineTV.setText(currency + Utility.changeAmount(grandTotalDetails.getString("amount_fine"),currency,currency_price));
                            gtPaidTV.setText(currency + Utility.changeAmount(grandTotalDetails.getString("amount_paid"),currency,currency_price));
                            gtBalanceTV.setText(currency + Utility.changeAmount(grandTotalDetails.getString("amount_remaining"),currency,currency_price));

                           // Double amount=Double.parseDouble(Utility.changeAmount(grandTotalDetails.getString("fee_fine"),currency,currency_price));

                            JSONArray dataArray = object.getJSONArray("student_due_fee");

                            if(dataArray.length() != 0) {
                                grandTotalLay.setVisibility(View.VISIBLE);

                                for(int i = 0; i < dataArray.length(); i++) {

                                    JSONArray feesArray = dataArray.getJSONObject(i).getJSONArray("fees");

                                    for(int j = 0; j<feesArray.length(); j++) {
                                        feesIdList.add(feesArray.getJSONObject(j).getString("id"));
                                        feesnameList.add(feesArray.getJSONObject(j).getString("name") + "-" + feesArray.getJSONObject(j).getString("type"));
                                        feesCodeList.add(feesArray.getJSONObject(j).getString("code"));

                                        dueDateList.add(feesArray.getJSONObject(j).getString("due_date"));
                                        amtList.add( currency + Utility.changeAmount(feesArray.getJSONObject(j).getString("amount"),currency,currency_price));
                                        amtfineList.add("+"+Utility.changeAmount(feesArray.getJSONObject(j).getString("fees_fine_amount"),currency,currency_price));
                                        paidAmtList.add(currency + Utility.changeAmount(feesArray.getJSONObject(j).getString("total_amount_paid"),currency,currency_price));
                                        discAmtList.add(currency + Utility.changeAmount(feesArray.getJSONObject(j).getString("total_amount_discount"),currency,currency_price));
                                        fineAmtList.add(currency + Utility.changeAmount(feesArray.getJSONObject(j).getString("total_amount_fine"),currency,currency_price));
                                        balanceAmtList.add(currency + Utility.changeAmount(feesArray.getJSONObject(j).getString("total_amount_remaining"),currency,currency_price));
                                        feesDepositIdList.add(feesArray.getJSONObject(j).getString("student_fees_deposite_id"));
                                        feesSessionIdList.add(feesArray.getJSONObject(j).getString("student_session_id"));
                                        feesTypeId.add(feesArray.getJSONObject(j).getString("fee_groups_feetype_id"));
                                        feesCat.add("fees");

                                        discountNameList.add("");
                                        discountAmtList.add("");
                                        discountStatusList.add("");
                                        transportdueDateList.add("");
                                        transportamtList.add("");
                                        transportamtfineList.add("");
                                        transportpaidAmtList.add("");
                                        transportdiscAmtList.add("");
                                        transportfineAmtList.add("");
                                        transportbalanceAmtList.add("");
                                        transportfeesDepositIdList.add("");
                                        transportstatusList.add("");


                                        statusList.add(feesArray.getJSONObject(j).getString("status").substring(0, 1).toUpperCase() + feesArray.getJSONObject(j).getString("status").substring(1));
                                        System.out.println("statusList="+statusList.size());
                                        JSONObject feesDetailsJson;
                                        try {
                                            feesDetailsJson = new JSONObject(feesArray.getJSONObject(j).getString("amount_detail"));
                                        } catch (JSONException e) {
                                            feesDetailsJson = new JSONObject();
                                        }
                                        feesDetails.add(feesDetailsJson.toString());
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.noData, Toast.LENGTH_LONG).show();
                            }


                            JSONArray transportfeesArray = object.getJSONArray("transport_fees");

                                for (int i = 0; i < transportfeesArray.length(); i++) {
                                    feesIdList.add(transportfeesArray.getJSONObject(i).getString("id"));
                                    feesCodeList.add(transportfeesArray.getJSONObject(i).getString("month"));
                                    feesSessionIdList.add(transportfeesArray.getJSONObject(i).getString("student_session_id"));
                                    transportdueDateList.add(transportfeesArray.getJSONObject(i).getString("due_date"));
                                    transportamtList.add(currency + Utility.changeAmount(transportfeesArray.getJSONObject(i).getString("fees"), currency, currency_price));
                                    transportamtfineList.add("+" + Utility.changeAmount(transportfeesArray.getJSONObject(i).getString("fees_fine_amount"), currency, currency_price));
                                    transportpaidAmtList.add(currency + Utility.changeAmount(transportfeesArray.getJSONObject(i).getString("total_amount_paid"), currency, currency_price));
                                    transportdiscAmtList.add(currency + Utility.changeAmount(transportfeesArray.getJSONObject(i).getString("total_amount_discount"), currency, currency_price));
                                    transportfineAmtList.add(currency + Utility.changeAmount(transportfeesArray.getJSONObject(i).getString("total_amount_fine"), currency, currency_price));
                                    transportbalanceAmtList.add(currency + Utility.changeAmount(transportfeesArray.getJSONObject(i).getString("total_amount_remaining"), currency, currency_price));
                                    transportfeesDepositIdList.add(transportfeesArray.getJSONObject(i).getString("student_fees_deposite_id"));
                                    transportstatusList.add(transportfeesArray.getJSONObject(i).getString("status").substring(0, 1).toUpperCase() + transportfeesArray.getJSONObject(i).getString("status").substring(1));
                                    feesCat.add("transport");
                                    discountNameList.add("");
                                    discountAmtList.add("");
                                    discountStatusList.add("");
                                    JSONObject feesDetailsJson;
                                    try {
                                        feesDetailsJson = new JSONObject(transportfeesArray.getJSONObject(i).getString("amount_detail"));
                                    } catch (JSONException e) {
                                        feesDetailsJson = new JSONObject();
                                    }
                                    feesDetails.add(feesDetailsJson.toString());
                                    System.out.println("transportstatusList=" + transportstatusList.size());
                                }
                            JSONArray discountArray = object.getJSONArray("student_discount_fee");


                                for (int i = 0; i < discountArray.length(); i++) {
                                    feesIdList.add(discountArray.getJSONObject(i).getString("id") + "discount");
                                    discountNameList.add(discountArray.getJSONObject(i).getString("code"));
                                    if (discountArray.getJSONObject(i).getString("type").equals("fix")) {
                                        discountAmtList.add(Utility.getSharedPreferences(getApplicationContext(), Constants.currency) + Utility.changeAmount(discountArray.getJSONObject(i).getString("amount"), currency, currency_price));
                                    } else {
                                        discountAmtList.add(discountArray.getJSONObject(i).getString("percentage") + "%");
                                    }
                                    // discountAmtList.add(Utility.changeAmount(discountArray.getJSONObject(i).getString("amount"),currency,currency_price));
                                    //discountpayment_idList.add(discountArray.getJSONObject(i).getString("payment_id"));
                                    if(discountArray.getJSONObject(i).getString("status").equals("applied")){
                                        if (discountArray.getJSONObject(i).getString("payment_id").equals("")) {
                                            discountStatusList.add(getApplicationContext().getString(R.string.applied));
                                        } else {
                                            discountStatusList.add(getApplicationContext().getString(R.string.applied) + " - " + discountArray.getJSONObject(i).getString("payment_id"));
                                         }
                                    }

                                    //discountStatusList.add(discountArray.getJSONObject(i).getString("status")+ " - "+discountArray.getJSONObject(i).getString("payment_id"));
                                    feesCat.add("discount");
                                    System.out.println("discountStatusList=" + discountStatusList.size());
                                }

                            adapter.notifyDataSetChanged();

                        } else {

                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StudentFees.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentFees.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }
    private void loginOutApi (String bodyParams) {
        DatabaseHelper dataBaseHelpers = new DatabaseHelper(StudentFees.this);
        dataBaseHelpers.deleteAll() ;

        final ProgressDialog pd = new ProgressDialog(StudentFees.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(StudentFees.this, "apiUrl")+ Constants.logoutUrl;
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
                            Intent logout = new Intent(StudentFees.this, Login.class);
                            logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            logout.putExtra("EXIT", true);
                            startActivity(logout);
                            finish();
                        } else {
                            Intent intent=new Intent(StudentFees.this, TakeUrl.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(StudentFees.this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                // Toast.makeText(StudentDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
                Intent intent=new Intent(StudentFees.this,TakeUrl.class);
                startActivity(intent);
                finish();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(StudentFees.this, "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(StudentFees.this, "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentFees.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void StudentOfflineStatus() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getOfflineBankPaymentStatusUrl;
        System.out.println("url=="+url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                pd.dismiss();
                try {
                    JSONObject object = new JSONObject(result);
                    is_offline_fee_payment = object.getString("is_offline_fee_payment");
                    Utility.setSharedPreference(getApplicationContext(),"is_offline_fee_payment",is_offline_fee_payment);
                    System.out.println("student_timeline="+is_offline_fee_payment);
                    if(is_offline_fee_payment.equals("1")){
                        offlinePayment.setVisibility(View.VISIBLE);
                    }else{
                        offlinePayment.setVisibility(View.GONE);
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
                Toast.makeText(StudentFees.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentFees.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }
}
