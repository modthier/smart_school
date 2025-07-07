package com.qdocs.smartschool;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.DatabaseHelper;
import com.qdocs.smartschool.utils.LocaleHelper;
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

public class SettingActivity extends BaseActivity {
    String id="",langshortcode="",lang_id="",language="",shortcode="",name="",symbol="",currencyid="",curr_id="";
    Spinner lang_spinner,currency_spinner;
    ArrayList<String> idList = new ArrayList<String>();
    ArrayList<String> currencyidList = new ArrayList<String>();
    ArrayList<String> langList = new ArrayList<String>();
    ArrayList<String> currencyList = new ArrayList<String>();
    ArrayList<String> short_codeList = new ArrayList<String>();
    ArrayList<String> currency_symbolList = new ArrayList<String>();
    ArrayList<String> symbolList = new ArrayList<String>();
    public Map<String, String> headers = new HashMap<String, String>();
    public Map<String, String> params = new Hashtable<String, String>();
    Context context;
    public Map<String, String> logoutparams = new Hashtable<String, String>();
    TextView langTV,savelanguage,selected_lang,selected_currency,savecurrency,currencyTV;
    CardView card_view_outer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_setting, null, false);
        mDrawerLayout.addView(contentView, 0);
        titleTV.setText(getApplicationContext().getString(R.string.setting));
        lang_spinner =  findViewById(R.id.lang_spinner);
        currency_spinner =  findViewById(R.id.currency_spinner);
        savelanguage =  findViewById(R.id.savelanguage);
        savecurrency =  findViewById(R.id.savecurrency);
        selected_lang =  findViewById(R.id.selected_lang);
        selected_currency =  findViewById(R.id.selected_currency);

        langTV =  findViewById(R.id.langTV);
        currencyTV =  findViewById(R.id.currencyTV);
        getSettingsFromApi( Utility.getSharedPreferences(getApplicationContext(), Constants.imagesUrl));
        langList.add(getApplicationContext().getString(R.string.select));
        idList.add("0");
        short_codeList.add("a");

        currencyList.add(getApplicationContext().getString(R.string.select));
        currencyidList.add("0");
        currency_symbolList.add("a");
        langTV.setText(getApplicationContext().getString(R.string.language));
        currencyTV.setText(getApplicationContext().getString(R.string.currency));
        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
        JSONObject obje=new JSONObject(params);
        Log.e("params ", obje.toString());
        System.out.println("Language Details=="+obje.toString());
        getlangFromApi(obje.toString());

        params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
        JSONObject object=new JSONObject(params);
        Log.e("params ", object.toString());
        System.out.println("Language Details=="+object.toString());
        getCurrencyFromApi(object.toString());
        selected_currency.setText( Utility.getSharedPreferences(getApplicationContext(), Constants.currency_short_name)+" ("+Utility.getSharedPreferences(getApplicationContext(), Constants.currency)+")");
            selected_lang.setOnClickListener(new View.OnClickListener() {
                     @Override
                    public void onClick(View view) {
                     selected_lang.setVisibility(View.GONE);
                     lang_spinner.setVisibility(View.VISIBLE);
                     }
            });

        selected_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_currency.setVisibility(View.GONE);
                currency_spinner.setVisibility(View.VISIBLE);
            }
        });

        ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_spinner_item, langList);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lang_spinner.setAdapter(langAdapter);
        lang_spinner.setSelection(0);
        lang_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id = idList.get(i);
                shortcode = short_codeList.get(i);
                if(!id.equals("0")){
                    Utility.setLocale(getApplicationContext(), shortcode);
                    context = LocaleHelper.setLocale(SettingActivity.this,short_codeList.get(i));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_spinner_item, currencyList);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currency_spinner.setAdapter(currencyAdapter);
        currency_spinner.setSelection(0);
        currency_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                curr_id = currencyidList.get(i);
                symbol = currency_symbolList.get(i);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        savelanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.equals("")){

                } else if(!id.equals("0")){
                    params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                    params.put("language_id",id);
                    JSONObject obj=new JSONObject(params);
                    Log.e("params ", obj.toString());
                    System.out.println("lang params=="+ obj.toString());
                    savelanguage(obj.toString());
                }
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SettingActivity.this);
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
        savecurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curr_id.equals("")){

                }else if(!curr_id.equals("0")){
                    params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                    params.put("currency_id",curr_id);
                    JSONObject obj=new JSONObject(params);
                    Log.e("params ", obj.toString());
                    System.out.println("params=="+ obj.toString());
                    saveCurrency(obj.toString());
                }
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SettingActivity.this);
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
    }

    private void getlangFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getstudentcurrentlanguageUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        System.out.println("language details="+result);
                        JSONObject object = new JSONObject(result);
                        JSONArray jsonArray = object.getJSONArray("result");
                        if(jsonArray.length() != 0){
                            for(int i = 0; i < jsonArray.length(); i++) {
                                langshortcode =jsonArray.getJSONObject(0).getString("short_code");
                                language =jsonArray.getJSONObject(0).getString("language");
                                selected_lang.setVisibility(View.VISIBLE);
                                selected_lang.setText(language);

                                Utility.setSharedPreference(getApplicationContext(), Constants.langCode,langshortcode);
                                Utility.setLocale(getApplicationContext(), langshortcode);
                                Utility.setSharedPreference(getApplicationContext(), Constants.currentLocale, langshortcode);
                            }
                        }else{
                            selected_lang.setVisibility(View.GONE);
                            lang_spinner.setVisibility(View.VISIBLE);
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
                Toast.makeText(SettingActivity.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(SettingActivity.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getCurrencyFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.get_currency_listUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        System.out.println("Currency details="+result);
                        JSONObject object = new JSONObject(result);
                        JSONArray jsonArray = object.getJSONArray("result");
                        if(jsonArray.length() != 0){
                            for(int i = 0; i < jsonArray.length(); i++) {
                                currencyidList.add(jsonArray.getJSONObject(i).getString("id"));
                                currencyList.add(jsonArray.getJSONObject(i).getString("short_name")+" ("+jsonArray.getJSONObject(i).getString("symbol")+")");
                                currency_symbolList .add(jsonArray.getJSONObject(i).getString("symbol"));
                                currencyid=jsonArray.getJSONObject(i).getString("id");
                                System.out.println("Currency=="+jsonArray.getJSONObject(i).getString("name"));

                            }
                        }else{
                            selected_currency.setVisibility(View.GONE);
                            currency_spinner.setVisibility(View.VISIBLE);
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
                Toast.makeText(SettingActivity.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(SettingActivity.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void loginOutApi (String bodyParams) {
        DatabaseHelper dataBaseHelpers = new DatabaseHelper(SettingActivity.this);
        dataBaseHelpers.deleteAll() ;

        final ProgressDialog pd = new ProgressDialog(SettingActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(SettingActivity.this, "apiUrl")+ Constants.logoutUrl;
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
                            finish();
                            Intent logout = new Intent(SettingActivity.this, Login.class);
                            logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            logout.putExtra("EXIT", true);
                            startActivity(logout);

                        } else {
                            Intent intent=new Intent(SettingActivity.this, TakeUrl.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(SettingActivity.this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Intent intent=new Intent(SettingActivity.this,TakeUrl.class);
                startActivity(intent);
                finish();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(SettingActivity.this, "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(SettingActivity.this, "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(SettingActivity.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void savelanguage(String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.updatestudentlanguage;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        String success = object.getString("status");
                        String message = object.getString("msg");
                        if(success.equals("1")){
                        }else{
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SettingActivity.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(SettingActivity.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    private void saveCurrency(String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.updatestudentcurrency;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        String success = object.getString("status");
                        String message = object.getString("msg");
                        if(success.equals("1")){
                           // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SettingActivity.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(SettingActivity.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getSettingsFromApi (String domain) {

        if(!domain.endsWith("/")) {
            domain += "/";
        }
        final String url = domain+"app";
        Log.e("Verification Url", url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {

                        JSONObject object = new JSONObject(result);
                        String app_ver= object.getString("app_ver");
                        JSONArray langArray = object.getJSONArray("languages");

                        System.out.println("app_ver DASHBOARD=="+app_ver);

                        if(langArray.length() != 0){
                            for(int i = 0; i < langArray.length(); i++) {
                                idList.add(langArray.getJSONObject(i).getString("id"));
                                langList.add(langArray.getJSONObject(i).getString("language"));
                                short_codeList .add(langArray.getJSONObject(i).getString("short_code"));
                                lang_id=langArray.getJSONObject(i).getString("id");
                                System.out.println("LANGUAGE=="+langArray.getJSONObject(i).getString("language"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else { }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(SettingActivity.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(SettingActivity.this, "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(SettingActivity.this, "accessToken"));
                Log.e("Headers", headers.toString());
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SettingActivity.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }
}
