package com.qdocs.smartschool.students;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
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
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.adapters.StudentOnlineCourseAdapter;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import static android.widget.Toast.makeText;

public class StudentOnlineCourse extends AppCompatActivity {
    StudentOnlineCourseAdapter adapter;
    RecyclerView recyclerview;
    private ProgressBar loadingPB;
    public NestedScrollView nestedSV;
    ArrayList<String> courseidList = new ArrayList<String>();
    ArrayList<String> coursetitleList = new ArrayList<String>();
    ArrayList<String> coursedescriptionList = new ArrayList<String>();
    ArrayList<String> course_thumbnailList = new ArrayList<String>();
    ArrayList<String> course_priceList = new ArrayList<String>();
    ArrayList<String> coursediscountList = new ArrayList<String>();
    ArrayList<String> classsectionList = new ArrayList<String>();
    ArrayList<String> sectionArray = new ArrayList<String>();
    ArrayList<String> free_courselist = new ArrayList<String>();
    ArrayList<String> course_progresslist = new ArrayList<String>();
    ArrayList<String> classlist = new ArrayList<String>();
    ArrayList<String> teacherlist = new ArrayList<String>();
    ArrayList<String> total_lessonlist = new ArrayList<String>();
    ArrayList<String> total_hour_countlist = new ArrayList<String>();
    ArrayList<String> updated_datelist = new ArrayList<String>();
    ArrayList<String> imagelist = new ArrayList<String>();
    ArrayList<String> totalcourseratinglist = new ArrayList<String>();
    ArrayList<String> courseratinglist = new ArrayList<String>();
    ArrayList<String> paidstatuslist = new ArrayList<String>();
    ArrayList<String> lessoncountList = new ArrayList<String>();
    SwipeRefreshLayout pullToRefresh;
    CardView card_view_outer;
    public ImageView backBtn;
    public Map<String, String>  headers = new HashMap<String, String>();
    public Map<String, String> params = new Hashtable<String, String>();
    public TextView titleTV;
    protected FrameLayout mDrawerLayout, actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        setContentView(R.layout.activity_student_online_course);
        backBtn = findViewById(R.id.actionBar_backBtn);
        actionBar = findViewById(R.id.actionBarSecondary);
        titleTV = findViewById(R.id.actionBar_title);
        actionBar.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        }
        titleTV.setText(getApplicationContext().getString(R.string.course_list));
        pullToRefresh = findViewById(R.id.pullToRefresh);
        recyclerview=findViewById(R.id.recyclerview);
        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        adapter = new StudentOnlineCourseAdapter(StudentOnlineCourse.this,recyclerview,courseidList,coursetitleList,coursedescriptionList,
                course_thumbnailList,coursediscountList,free_courselist,course_progresslist,classlist,
                teacherlist,total_lessonlist,total_hour_countlist,updated_datelist,imagelist,course_priceList,paidstatuslist,totalcourseratinglist,
                courseratinglist,classsectionList,lessoncountList,sectionArray);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(adapter);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                loaddata();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.no_animation,  R.anim.slide_rightleft);
            }
        });
        loaddata();
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
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.courselistUrl;
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
                        String pay_method = obj.getString("pay_method");
                        if(pay_method.equals("0")) {
                            Log.e("test", "testing");
                            Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.showCoursePaymentBtn, false);
                        } else {
                            Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.showCoursePaymentBtn, true);
                        }
                        JSONArray dataArray = obj.getJSONArray("course_list");
                        courseidList.clear();
                        coursetitleList.clear();
                        coursedescriptionList.clear();
                        course_thumbnailList.clear();
                        course_priceList.clear();
                        coursediscountList.clear();
                        free_courselist.clear();
                        course_progresslist.clear();
                        classlist.clear();
                        teacherlist.clear();
                        total_lessonlist.clear();
                        total_hour_countlist.clear();
                        updated_datelist.clear();
                        paidstatuslist.clear();
                        imagelist.clear();
                        totalcourseratinglist.clear();
                        courseratinglist.clear();
                        classsectionList.clear();
                        lessoncountList.clear();


                        if(dataArray.length() != 0) {
                            for(int i = 0; i < dataArray.length(); i++) {
                                courseidList.add(dataArray.getJSONObject(i).getString("id"));
                                coursetitleList.add(dataArray.getJSONObject(i).getString("title"));
                                coursedescriptionList.add(dataArray.getJSONObject(i).getString("description"));
                                course_thumbnailList.add(dataArray.getJSONObject(i).getString("course_thumbnail"));
                                String amount= Utility.changeAmount(dataArray.getJSONObject(i).getString("price"), Utility.getSharedPreferences(getApplicationContext(), Constants.currency),
                                       Utility.getSharedPreferences(getApplicationContext(), Constants.currency_price));
                                course_priceList.add(amount);
                                coursediscountList.add(dataArray.getJSONObject(i).getString("discount"));
                                sectionArray.add(dataArray.getJSONObject(i).getJSONArray("section").toString());
                                classsectionList.add(dataArray.getJSONObject(i).getString("class")+"-"+dataArray.getJSONObject(i).getString("section"));
                                free_courselist.add(dataArray.getJSONObject(i).getString("free_course"));
                                lessoncountList.add(dataArray.getJSONObject(i).getString("total_lesson"));
                                course_progresslist.add(dataArray.getJSONObject(i).getString("course_progress"));
                                classlist.add(dataArray.getJSONObject(i).getString("class"));
                                teacherlist.add(dataArray.getJSONObject(i).getString("name")+" "+dataArray.getJSONObject(i).getString("surname")+" ("+dataArray.getJSONObject(i).getString("staff_employee_id")+")");
                                total_lessonlist.add(dataArray.getJSONObject(i).getString("total_lesson"));
                                total_hour_countlist.add(dataArray.getJSONObject(i).getString("total_hour_count"));
                                updated_datelist.add(dataArray.getJSONObject(i).getString("updated_date"));
                                paidstatuslist.add(dataArray.getJSONObject(i).getString("paidstatus"));
                                imagelist.add(dataArray.getJSONObject(i).getString("image"));
                                totalcourseratinglist.add(dataArray.getJSONObject(i).getString("totalcourserating"));
                                courseratinglist.add(dataArray.getJSONObject(i).getString("courserating"));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            pullToRefresh.setVisibility(View.GONE);
                            makeText(StudentOnlineCourse.this, R.string.noData, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                   pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(StudentOnlineCourse.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentOnlineCourse.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }

    @Override
    public void onRestart() {
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        super.onRestart();
        loaddata();
        // do some stuff here
    }
    public void setLocale(Context context, String localeName){
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale myLocale = new Locale(localeName);
        Locale.setDefault(myLocale);
        configuration.setLocale(myLocale);

        if (Build.VERSION.SDK_INT >= 25) {
            context = context.getApplicationContext().createConfigurationContext(configuration);
            context = context.createConfigurationContext(configuration);
        }

        context.getResources().updateConfiguration(configuration,
                resources.getDisplayMetrics());
    }

    @Override
    protected void onResume() {
        updateResources(getApplicationContext(),Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        super.onResume();
    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }
}
