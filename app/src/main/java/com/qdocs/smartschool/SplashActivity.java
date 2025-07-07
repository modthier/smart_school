package com.qdocs.smartschool;

import static android.widget.Toast.makeText;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.smartschool.students.NewDashboard;
import com.qdocs.smartschool.students.StudentFees;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SplashActivity extends Activity {

    private static final int SPLASH_TIME_OUT = 1000;
    ImageView logoIV;
    Boolean isLoggegIn,isLock;
    Boolean isUrlTaken;

    public Map<String, String>  headers = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);
        logoIV = findViewById(R.id.splash_logo);

        Boolean isLocaleSet;

        try {
            isLocaleSet = Utility.getSharedPreferencesBoolean(getApplicationContext(), "isLocaleSet");
        } catch (NullPointerException e) {
            isLocaleSet = false;
        }

        if(isLocaleSet) {
            setLocale(Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        }
       splash();

    }

    private void splash() {

        new Handler().postDelayed(new Runnable() {
            public void run() {

                try {
                    isLoggegIn = Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.isLoggegIn);
                    isLock = Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.isLock);
                    isUrlTaken = Utility.getSharedPreferencesBoolean(getApplicationContext(), "isUrlTaken");
                } catch (NullPointerException NPE) {
                    isLoggegIn = false;
                    isUrlTaken = false;
                    isLock = false;
                }

                Log.e("loggeg", isLoggegIn.toString());
                Log.e("isLock", isLock.toString());
                Log.e("isUrlTaken", isUrlTaken.toString());

                if(Constants.askUrlFromUser) {
                    if(isUrlTaken) {
                        if(Utility.isConnectingToInternet(SplashActivity.this)){
                            ismaintenancemode(Utility.getSharedPreferences(getApplicationContext(), "apiUrl"));
                        }else{
                            makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent asd = new Intent(getApplicationContext(), TakeUrl.class);
                        startActivity(asd);
                        finish();
                    }
                } else {
                    if(Utility.isConnectingToInternet(SplashActivity.this)){
                        ismaintenancemode(Constants.domain+"/api/");
                    }else{
                        makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, SPLASH_TIME_OUT);
    }

    public void setLocale(String localeName) {
        Locale myLocale = new Locale(localeName);
        Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Log.e("Status", "Locale updated!");
    }

    private void ismaintenancemode(String siteurl) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = siteurl+ Constants.getMaintenanceModeStatusUrl;
        System.out.println("url=="+url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String maintenance_mode = object.getString("maintenance_mode");
                    System.out.println("maintenance_mode="+maintenance_mode.toString());
                    if(maintenance_mode.equals("0")){
                        Utility.setSharedPreferenceBoolean(getApplicationContext(), "maintenance_mode", false);
                        pd.dismiss();
                        if(isLoggegIn){
                            if(isLock){
                                Intent i = new Intent(getApplicationContext(), StudentFees.class);
                                startActivity(i);
                                finish();
                            }else{
                                Intent i = new Intent(getApplicationContext(), NewDashboard.class);
                                startActivity(i);
                                finish();
                            }
                        }else {
                            Intent i = new Intent(getApplicationContext(), Login.class);
                            startActivity(i);
                            finish();
                        }
                    } else{
                        Utility.setSharedPreferenceBoolean(getApplicationContext(), "maintenance_mode", true);
                        pd.dismiss();
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SplashActivity.this);
                        builder.setCancelable(false);
                        builder.setMessage(R.string.maintainMessage);
                        builder.setTitle("");
                        android.app.AlertDialog alert = builder.create();
                        alert.show();

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
                volleyError.printStackTrace();
                Toast.makeText(SplashActivity.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue


    }
}
