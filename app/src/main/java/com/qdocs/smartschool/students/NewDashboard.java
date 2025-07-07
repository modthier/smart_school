package com.qdocs.smartschool.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.qdocs.smartschool.AboutSchool;
import com.qdocs.smartschool.Login;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.SettingActivity;
import com.qdocs.smartschool.TakeUrl;
import com.qdocs.smartschool.adapters.AcademicModuleAdapter;
import com.qdocs.smartschool.adapters.CommunicateModuleAdapter;
import com.qdocs.smartschool.adapters.ElearningModuleAdapter;
import com.qdocs.smartschool.adapters.LoginChildListAdapter;
import com.qdocs.smartschool.adapters.OtherModuleAdapter;
import com.qdocs.smartschool.model.Album1;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.DatabaseHelper;
import com.qdocs.smartschool.utils.DrawerArrowDrawable;
import com.qdocs.smartschool.utils.Utility;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import static android.widget.Toast.makeText;

public class NewDashboard extends AppCompatActivity {
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    public DrawerArrowDrawable drawerArrowDrawable;
    ImageView drawerIndicator;
    public DrawerLayout drawer;
    String[] permissionsRequired = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA,Manifest.permission.POST_NOTIFICATIONS};
    protected FrameLayout mDrawerLayout, actionBar;
    CardView card_view_outer;
    private NavigationView navigationView;
    public boolean flipped;
    LoginChildListAdapter studentListAdapter;
    public float offset;
    ImageView actionBarLogo;
    TextView unread_count,version_name;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> aparams = new Hashtable<String, String>();
    FrameLayout notification_alert;
    private TextView classTV, nameTV, childDetailsTV;
    private ImageView profileImageIV;
    private LinearLayout switchChildBtn;
    private RelativeLayout drawerHead;
    public Map<String, String> logoutparams = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    String device_token;
    ArrayList<String> childIdList = new ArrayList<String>();
    ArrayList<String> childNameList = new ArrayList<String>();
    ArrayList<String> childClassList = new ArrayList<String>();
    ArrayList<String> childImageList = new ArrayList<String>();
    JSONArray modulesJson;
    TextView name,admissionno,classdata;
    ImageView profileImageview;
    ArrayList<String> moduleCodeList = new ArrayList<String>();
    ArrayList<String> moduleStatusList = new ArrayList<String>();
    LinearLayout profilelinear;
    CollapsingToolbarLayout collapsing_toolbar;
    RecyclerView elearning_recyclerView,academic_recyclerView,communicate_recyclerView,other_recyclerView;
    ElearningModuleAdapter elearningModuleAdapter;
    AcademicModuleAdapter academicModuleAdapter;
    CommunicateModuleAdapter communicateModuleAdapter;
    OtherModuleAdapter otherModuleAdapter;
    ArrayList<Album1> communicatealbumList,elearningalbumList,academicalbumList,otheralbumList;
    TextView textview1,textview2,textview3,textview4;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 5000;
    CardView elearning_card,academic_card,communicate_card,other_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dashboard);
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        drawerIndicator = findViewById(R.id.drawer_indicator);
        drawer = findViewById(R.id.drawer_layout);
        actionBar = findViewById(R.id.actionBar);
        unread_count = findViewById(R.id.unread_count);
        actionBarLogo = findViewById(R.id.actionBar_logo);
        notification_alert = findViewById(R.id.notification_alert);
        navigationView = findViewById(R.id.nav_view);
        profilelinear = findViewById(R.id.profilelinear);
        collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        name = findViewById(R.id.name);
        admissionno = findViewById(R.id.admissionno);
        textview1 = findViewById(R.id.textview1);
        textview1.setText(getApplicationContext().getString(R.string.elearning));
        textview2 = findViewById(R.id.textview2);
        textview2.setText(getApplicationContext().getString(R.string.academic));
        textview3 = findViewById(R.id.textview3);
        textview3.setText(getApplicationContext().getString(R.string.communicate));
        textview4 = findViewById(R.id.textview4);
        textview4.setText(getApplicationContext().getString(R.string.others));
        classdata = findViewById(R.id.classdata);
        other_card = findViewById(R.id.other_card);
        communicate_card = findViewById(R.id.communicate_card);
        academic_card = findViewById(R.id.academic_card);
        elearning_card = findViewById(R.id.elearning_card);
        profileImageview = findViewById(R.id.studentProfile_profileImageview);
        card_view_outer = findViewById(R.id.card_view_outer);
        Locale current = getResources().getConfiguration().locale;

        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("site_url", Utility.getSharedPreferences(getApplicationContext(), Constants.imagesUrl));
            JSONObject obj = new JSONObject(params);
            Log.e("params", obj.toString());
            System.out.println("params==" + obj.toString());
            getDatasFromApi(obj.toString());
        } else {
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
       // makeText(this, Utility.getSharedPreferences(getApplicationContext(),Constants.currentLocale), Toast.LENGTH_SHORT).show();
        elearningalbumList = new ArrayList<>();
        academicalbumList = new ArrayList<>();
        communicatealbumList = new ArrayList<>();
        otheralbumList = new ArrayList<>();

        prepareNavList();
        setUpDrawer();
        decorate();
        setUpPermission();

        device_token = FirebaseInstanceId.getInstance().getToken()+"";
        Log.e(" logout DEVICE TOKEN", device_token);
        DatabaseHelper db = new DatabaseHelper(NewDashboard.this);
        int profile_counts = db.getProfilesCount();
        db.close();
        if(String.valueOf(profile_counts).equals("0")){
            unread_count.setVisibility(View.GONE);
        }else{
            unread_count.setText(String.valueOf(profile_counts));
        }

        if(Utility.getSharedPreferences(getApplicationContext(), "role").equals("parent")) {
            if(Utility.getSharedPreferencesBoolean(getApplicationContext(), "hasMultipleChild")) {
                switchChildBtn.setVisibility(View.VISIBLE);
            } else {
                switchChildBtn.setVisibility(View.GONE);
            }
        } else {
            switchChildBtn.setVisibility(View.GONE);
        }

        switchChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChildList();
            }
        });

        elearning_recyclerView = findViewById(R.id.elearning_recyclerView);
        elearningModuleAdapter = new ElearningModuleAdapter(NewDashboard.this,elearningalbumList);
        GridLayoutManager elearningLayoutManager=new GridLayoutManager(this,4);
        elearning_recyclerView.setLayoutManager(elearningLayoutManager);
        elearning_recyclerView.setAdapter(elearningModuleAdapter);
        // elearning();

        academic_recyclerView = findViewById(R.id.academic_recyclerView);
        academicModuleAdapter = new AcademicModuleAdapter(NewDashboard.this,academicalbumList);
        GridLayoutManager academicLayoutManager=new GridLayoutManager(this,4);
        academic_recyclerView.setLayoutManager(academicLayoutManager);
        academic_recyclerView.setAdapter(academicModuleAdapter);
        //academic();

        communicate_recyclerView = findViewById(R.id.communicate_recyclerView);
        communicateModuleAdapter = new CommunicateModuleAdapter(NewDashboard.this,communicatealbumList);
        GridLayoutManager communicateLayoutManager=new GridLayoutManager(this,4);
        communicate_recyclerView.setLayoutManager(communicateLayoutManager);
        communicate_recyclerView.setAdapter(communicateModuleAdapter);
        //communicate();

        other_recyclerView = findViewById(R.id.other_recyclerView);
        otherModuleAdapter = new OtherModuleAdapter(NewDashboard.this,otheralbumList);
        GridLayoutManager otherLayoutManager=new GridLayoutManager(this,4);
        other_recyclerView.setLayoutManager(otherLayoutManager);
        other_recyclerView.setAdapter(otherModuleAdapter);
       // other();


        notification_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    DatabaseHelper db = new DatabaseHelper(NewDashboard.this);
                    db.updatestatus("0", "1");
                    Intent intent = new Intent(NewDashboard.this, NotificationList.class);
                    startActivity(intent);

            }
        });


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Title");

        Resources resources = getResources();
        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.drawerIndicatorColour));

        drawerIndicator.setImageDrawable(drawerArrowDrawable);

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;
                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }
                drawerArrowDrawable.setParameter(offset);
            }
        });

        drawerIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        if(Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("parent")){
            if (Utility.isConnectingToInternet(getApplicationContext())) {
                params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                params.put("date_from", getDateOfMonth(new Date(), "first"));
                params.put("date_to", getDateOfMonth(new Date(), "last"));
                params.put("role", Utility.getSharedPreferences(getApplicationContext(), Constants.loginType));
                params.put("user_id", Utility.getSharedPreferences(getApplicationContext(), Constants.userId));
                JSONObject object=new JSONObject(params);
                Log.e("params ", object.toString());
                getDataFromApi(object.toString());
            } else {
                makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            }

        }else{
            if (Utility.isConnectingToInternet(getApplicationContext())) {
                params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                params.put("date_from", getDateOfMonth(new Date(), "first"));
                params.put("date_to", getDateOfMonth(new Date(), "last"));
                params.put("role", Utility.getSharedPreferences(getApplicationContext(), Constants.loginType));
                JSONObject object=new JSONObject(params);
                Log.e("params ", object.toString());
                getDataFromApi(object.toString());
            } else {
                makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            }

            if (Utility.isConnectingToInternet(getApplicationContext())) {
                params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
                JSONObject object=new JSONObject(params);
                Log.e("params ", object.toString());
                getCurrencyDataFromApi(object.toString());
            } else {
                makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            }

        }
    }

    public static String getDateOfMonth(Date date, String index){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(index.equals("first")) {
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        } else {
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormatter.format(cal.getTime());
    }
    private void getElearningFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getELearningUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Modules Result", result);
                        JSONObject object = new JSONObject(result);
                        System.out.println("Modules Result"+result);

                        modulesJson = object.getJSONArray("module_list");
                        System.out.println("Modules length"+modulesJson.length());

                        int[] covers = new int[]{
                                R.drawable.ic_dashboard_homework,
                                R.drawable.ic_assignment,
                                R.drawable.ic_lessonplan,
                                R.drawable.ic_onlineexam,
                                R.drawable.ic_downloadcenter,
                                R.drawable.ic_onlinecourse,
                                R.drawable.ic_videocam,
                                R.drawable.ic_videocam,
                        };

                        Utility.setSharedPreference(getApplicationContext(), Constants.modulesArray, modulesJson.toString());
                        if (modulesJson.length() != 0) {
                            for(int i = 0; i < modulesJson.length(); i++) {
                                if(modulesJson.getJSONObject(i).getString("status").equals("1")){

                                    Album1 album1=new Album1();
                                    album1.setName(modulesJson.getJSONObject(i).getString("short_code"));
                                    album1.setValue(modulesJson.getJSONObject(i).getString("status"));
                                    album1.setThumbnail(covers[i]);
                                    elearningalbumList.add(album1);

                                }else{

                                }
                            }
                            elearningModuleAdapter.notifyDataSetChanged();
                            //setMenu(navigationView.getMenu());
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NewDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }

    private void getAcademicsFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getAcademicsUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Modules Result", result);
                        JSONObject object = new JSONObject(result);
                        System.out.println("Modules Result"+result);

                        modulesJson = object.getJSONArray("module_list");
                        System.out.println("Modules length"+modulesJson.length());

                        int[] covers = new int[]{
                                R.drawable.ic_calender_cross,
                                R.drawable.ic_lessonplan,
                                R.drawable.ic_nav_attendance,
                                R.drawable.ic_nav_reportcard,
                                R.drawable.ic_nav_timeline,
                                R.drawable.ic_documents_certificate,
                                R.drawable.ic_dashboard_homework,
                                R.drawable.ic_nav_reportcard,
                        };
                        Utility.setSharedPreference(getApplicationContext(), Constants.modulesArray, modulesJson.toString());
                        if (modulesJson.length() != 0) {
                            for(int i = 0; i < modulesJson.length(); i++) {
                                if(modulesJson.getJSONObject(i).getString("status").equals("1")){
                                    Album1 album1=new Album1();
                                    album1.setName(modulesJson.getJSONObject(i).getString("short_code"));
                                    album1.setValue(modulesJson.getJSONObject(i).getString("status"));
                                    album1.setThumbnail(covers[i]);
                                    academicalbumList.add(album1);
                                }else{}
                            }
                            academicModuleAdapter.notifyDataSetChanged();
                            //setMenu(navigationView.getMenu());
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NewDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }

    private void getCommunicateFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getCommunicateUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Modules Result", result);
                        JSONObject object = new JSONObject(result);
                        System.out.println("Modules Result"+result);

                        modulesJson = object.getJSONArray("module_list");
                        System.out.println("Modules length"+modulesJson.length());

                        int[] covers = new int[]{
                                R.drawable.ic_notice,
                                R.drawable.ic_notification
                        };

                        Utility.setSharedPreference(getApplicationContext(), Constants.modulesArray, modulesJson.toString());
                        if (modulesJson.length() != 0) {

                            for(int i = 0; i < modulesJson.length(); i++) {
                                if(modulesJson.getJSONObject(i).getString("status").equals("1")){

                                    Album1 album1=new Album1();
                                    album1.setName(modulesJson.getJSONObject(i).getString("short_code"));
                                    album1.setValue(modulesJson.getJSONObject(i).getString("status"));
                                    album1.setThumbnail(covers[i]);
                                    communicatealbumList.add(album1);

                                }else{

                                }
                            }
                            communicateModuleAdapter.notifyDataSetChanged();
                            //setMenu(navigationView.getMenu());
                        } else {

                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NewDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }

    private void getOthersFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getOthersUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Modules Result", result);
                        JSONObject object = new JSONObject(result);
                        System.out.println("Modules Result"+result);

                        modulesJson = object.getJSONArray("module_list");
                        System.out.println("Modules length"+modulesJson.length());

                        int[] covers = new int[]{
                                R.drawable.ic_nav_fees,
                                R.drawable.ic_leave,
                                R.drawable.ic_visitors,
                                R.drawable.ic_nav_transport,
                                R.drawable.ic_nav_hostel,
                                R.drawable.ic_dashboard_pandingtask,
                                R.drawable.ic_library,
                                R.drawable.ic_teacher
                        };

                        Utility.setSharedPreference(getApplicationContext(), Constants.modulesArray, modulesJson.toString());
                        if (modulesJson.length() != 0) {
                            for(int i = 0; i < modulesJson.length(); i++) {
                                if(modulesJson.getJSONObject(i).getString("status").equals("1")){
                                    Album1 album1=new Album1();
                                    album1.setName(modulesJson.getJSONObject(i).getString("short_code"));
                                    album1.setValue(modulesJson.getJSONObject(i).getString("status"));
                                    album1.setThumbnail(covers[i]);
                                    otheralbumList.add(album1);
                                }else{

                                }
                            }
                            otherModuleAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NewDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }

    private void setUpDrawer() {
        //HEADER
        View headerLayout = navigationView.getHeaderView(0);

        Menu menu = navigationView.getMenu();
        RelativeLayout tracks = (RelativeLayout) menu.findItem(R.id.nav_log_version).getActionView();
        TextView version_name = (TextView) tracks.findViewById(R.id.version_name);
        version_name.setText(getApplicationContext().getString(R.string.version)+" on "+Utility.getSharedPreferences(getApplicationContext(), Constants.app_ver));

        classTV = headerLayout.findViewById(R.id.drawer_userClass);
        nameTV = headerLayout.findViewById(R.id.drawer_userName);
        profileImageIV = headerLayout.findViewById(R.id.drawer_logo);
        drawerHead = headerLayout.findViewById(R.id.drawer_head);
        switchChildBtn = headerLayout.findViewById(R.id.drawer_switchChildBtn);
        childDetailsTV = headerLayout.findViewById(R.id.drawer_studentDetailsTV);
        //HEADER
        Resources resources = getResources();
        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.drawerIndicatorColour));

        drawerIndicator.setImageDrawable(drawerArrowDrawable);

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;
                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }
                drawerArrowDrawable.setParameter(offset);
            }
        });
        drawerIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void showChildList() {

        View view = getLayoutInflater().inflate(R.layout.fragment_login_bottom_sheet, null);
        view.setMinimumHeight(500);

        TextView headerTV = view.findViewById(R.id.login_bottomSheet_header);
        ImageView crossBtn = view.findViewById(R.id.login_bottomSheet_crossBtn);
        RecyclerView childListView = view.findViewById(R.id.login_bottomSheet_listview);

        headerTV.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        headerTV.setText(getString(R.string.childList));

        Log.e("ImageList", childImageList.toString());
        Log.e("Class List", childClassList.toString());
        Log.e("ID List", childIdList.toString());

        studentListAdapter = new LoginChildListAdapter(NewDashboard.this, childIdList, childNameList, childClassList, childImageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        childListView.setLayoutManager(mLayoutManager);
        childListView.setItemAnimator(new DefaultItemAnimator());
        childListView.setAdapter(studentListAdapter);

        final BottomSheetDialog dialog = new BottomSheetDialog(NewDashboard.this);

        dialog.setContentView(view);
        dialog.show();

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("parent_id", Utility.getSharedPreferences(getApplicationContext(), "userId"));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getStudentsListFromApi(obj.toString());
        } else {
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
        Log.e("Child Name", childNameList.toString());
    }

    private void setUpPermission() {
        if(ActivityCompat.checkSelfPermission(NewDashboard.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(NewDashboard.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(NewDashboard.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(NewDashboard.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(NewDashboard.this, permissionsRequired[4]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[4])){
                ActivityCompat.requestPermissions(NewDashboard.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            } else {
                ActivityCompat.requestPermissions(NewDashboard.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }
            Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.permissionStatus, true);
        }
    }


    private void decorate() {

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        String appLogo = Utility.getSharedPreferences(this, Constants.appLogo)+"?"+new Random().nextInt(11);

        Picasso.with(getApplicationContext()).load(Utility.getSharedPreferences(this, "userImage")).placeholder(R.drawable.placeholder_user).into(profileImageIV);
        Picasso.with(getApplicationContext()).load(Utility.getSharedPreferences(this, "userImage")).placeholder(R.drawable.placeholder_user).into(profileImageview);
        Picasso.with(getApplicationContext()).load(appLogo).fit().centerInside().placeholder(null).into(actionBarLogo);

        nameTV.setText(Utility.getSharedPreferences(this, Constants.userName));
        admissionno.setText("Admission No. "+Utility.getSharedPreferences(this, Constants.admission_no));
        classdata.setText(Utility.getSharedPreferences(this, Constants.classSection));
        name.setText(Utility.getSharedPreferences(this, Constants.userName));
        classTV.setText(Utility.getSharedPreferences(this, Constants.classSection));
        childDetailsTV.setText("Child - " + Utility.getSharedPreferences(getApplicationContext(), "studentName")
                + "\n" + Utility.getSharedPreferences(this, Constants.classSection));

        if(Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("parent")) {
            classTV.setVisibility(View.GONE);
            childDetailsTV.setVisibility(View.VISIBLE);
        } else {
            classTV.setVisibility(View.VISIBLE);
            childDetailsTV.setVisibility(View.GONE);
        }
        System.out.println("Colour=="+Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour));
        actionBar.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        profilelinear.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        collapsing_toolbar.setContentScrimColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));

        drawerHead.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        }
    }

    //RUNTIME PERMISSIONS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults
        );
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){

            if(ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(NewDashboard.this,permissionsRequired[4])){

                AlertDialog.Builder builder = new AlertDialog.Builder(NewDashboard.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs to access to your storage, camera and call permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(NewDashboard.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {

                if(Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(NewDashboard.this);
                    builder.setTitle("Allow Notifications");
                    builder.setMessage("For smooth functioning of app, please provide Auto-Start permission and allow notification access.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {

                }

            }
        }
    }

    private void getStudentsListFromApi (String bodyParams) {

        childIdList.clear(); childNameList.clear(); childClassList.clear(); childImageList.clear();

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.parent_getStudentList;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        JSONArray dataArray = object.getJSONArray("childs");
                        if (dataArray.length() != 0) {

                            for(int i = 0; i < dataArray.length(); i++) {
                                childIdList.add(dataArray.getJSONObject(i).getString("id"));
                                childNameList.add(dataArray.getJSONObject(i).getString("firstname") + " " +  dataArray.getJSONObject(i).getString("lastname") );
                                childClassList.add(dataArray.getJSONObject(i).getString("class") + "-" +  dataArray.getJSONObject(i).getString("section"));
                                childImageList.add(dataArray.getJSONObject(i).getString("image"));
                            }
                            studentListAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NewDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getDatasFromApi(String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

            String url = "https://sstrace.qdocs.in/postlic/verifyappjsonv2";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String result) {
                    System.out.println("result=="+result);
                    if (result != null) {
                        pd.dismiss();
                        try {

                            JSONObject object = new JSONObject(result);

                            if(object.getString("status").equals("1")) {
                                Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.isLoggegIn, false);

                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NewDashboard.this);
                                builder.setCancelable(false);
                                //builder.setMessage(R.string.verificationMessage);
                                builder.setMessage(object.getString("msg"));
                                builder.setTitle("");
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

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

                                android.app.AlertDialog alert = builder.create();
                                alert.show();
                            }else {

                                if (Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("student")) {
                                    aparams.put("id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
                                    aparams.put("user_type", Utility.getSharedPreferences(getApplicationContext(), Constants.loginType));
                                    JSONObject obj = new JSONObject(aparams);
                                    Log.e("params ", obj.toString());
                                    System.out.println("Status Details==" + obj.toString());
                                    checkStudentStatus(obj.toString());
                                } else {
                                    aparams.put("id", Utility.getSharedPreferences(getApplicationContext(), Constants.parentsId));
                                    aparams.put("user_type", Utility.getSharedPreferences(getApplicationContext(), Constants.loginType));
                                    JSONObject obj = new JSONObject(aparams);
                                    Log.e("params ", obj.toString());
                                    System.out.println("Status Details==" + obj.toString());
                                    checkStudentStatus(obj.toString());
                                }
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
                    Toast.makeText(NewDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    headers.put("Client-Service", Constants.clientService);
                    headers.put("Auth-Key", Constants.authKey);
                    headers.put("Content-Type", Constants.contentType);
                    headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                    headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
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
            RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);
            //Adding request to the queue
            requestQueue.add(stringRequest);


    }


    private void prepareNavList() {
        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.put("user", Utility.getSharedPreferences(getApplicationContext(), Constants.loginType));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getElearningFromApi(obj.toString());
            getCommunicateFromApi(obj.toString());
            getAcademicsFromApi(obj.toString());
            getOthersFromApi(obj.toString());
        } else {
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (menuItem.getItemId()) {

                    case R.id.nav_home:

                            Intent dashboard = new Intent(NewDashboard.this, NewDashboard.class);
                            startActivity(dashboard);
                            overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                            drawer.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.nav_profile:

                            Intent profile = new Intent(NewDashboard.this, StudentProfileDetailsNew.class);
                            startActivity(profile);
                            overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                            drawer.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.nav_about:

                            Intent about = new Intent(NewDashboard.this, AboutSchool.class);
                            startActivity(about);
                            overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                            drawer.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.nav_setting:

                            Intent setting = new Intent(NewDashboard.this, SettingActivity.class);
                            startActivity(setting);
                            overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                            drawer.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.nav_logout:
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NewDashboard.this);
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
                        break;
                }
                return false;
            }
        });

    }
    private void loginOutApi (String bodyParams) {
        DatabaseHelper dataBaseHelpers = new DatabaseHelper(NewDashboard.this);
        dataBaseHelpers.deleteAll() ;

        final ProgressDialog pd = new ProgressDialog(NewDashboard.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(NewDashboard.this, "apiUrl")+ Constants.logoutUrl;
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
                            Intent logout = new Intent(NewDashboard.this, Login.class);
                            logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            logout.putExtra("EXIT", true);
                            startActivity(logout);
                            finish();
                        } else {
                            Intent intent=new Intent(NewDashboard.this, TakeUrl.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(NewDashboard.this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                // Toast.makeText(StudentDashboard.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
                Intent intent=new Intent(NewDashboard.this,TakeUrl.class);
                startActivity(intent);
                finish();

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(NewDashboard.this, "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(NewDashboard.this, "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getDataFromApi (String bodyParams) {

        Log.e("RESULT PARAMS", bodyParams);
        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl") + Constants.getDashboardUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);
                        //TODO success
                        String success = "1"; //object.getString("success");
                        if (success.equals("1")) {

                            Utility.setSharedPreference(getApplicationContext(), Constants.classId, object.getString("class_id"));
                            Utility.setSharedPreference(getApplicationContext(), Constants.sectionId, object.getString("section_id"));

                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());//Creating a Request Queue
        requestQueue.add(stringRequest);//Adding request to the queue
    }

    private void getCurrencyDataFromApi (String bodyParams) {
        Log.e("RESULT PARAMS", bodyParams);
        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl") + Constants.getStudentCurrencyUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        //TODO success
                        JSONObject data = object.getJSONObject("result");
                        System.out.println("Currency data=="+data.toString());
                        Utility.setSharedPreference(getApplicationContext(), Constants.currency_price, data.getString("base_price"));
                        Utility.setSharedPreference(getApplicationContext(), Constants.currency_short_name, data.getString("name"));
                        Utility.setSharedPreference(getApplicationContext(), Constants.currency, data.getString("symbol"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());//Creating a Request Queue
        requestQueue.add(stringRequest);//Adding request to the queue
    }


    private void checkStudentStatus(String bodyParams) {


        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.checkStudentStatusUrl;
        System.out.println("url=="+url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject object = new JSONObject(result);
                    String response = object.getString("response");
                    System.out.println("response="+response.toString());
                    Utility.setSharedPreference(getApplicationContext(),"response",response);
                    if(Utility.getSharedPreferences(getApplicationContext(),"response").equals("no")){
                        Utility.setSharedPreferenceBoolean(getApplicationContext(), "isLoggegIn", false);
                        Intent logout = new Intent(getApplicationContext(), Login.class);
                        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        logout.putExtra("EXIT", true);
                        startActivity(logout);
                        finish();
                        handler.removeCallbacks(runnable);
                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Volley Error", volleyError.toString());

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

        RequestQueue requestQueue = Volley.newRequestQueue(NewDashboard.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
               /* if (Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("student")) {
                    aparams.put("id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
                    aparams.put("user_type", Utility.getSharedPreferences(getApplicationContext(), Constants.loginType));
                    JSONObject obj = new JSONObject(aparams);
                    Log.e("params ", obj.toString());
                    System.out.println("Status Details==" + obj.toString());
                    checkStudentStatus(obj.toString());
                } else {
                    aparams.put("id", Utility.getSharedPreferences(getApplicationContext(), Constants.parentsId));
                    aparams.put("user_type", Utility.getSharedPreferences(getApplicationContext(), Constants.loginType));
                    JSONObject obj = new JSONObject(aparams);
                    Log.e("params ", obj.toString());
                    System.out.println("Status Details==" + obj.toString());
                    checkStudentStatus(obj.toString());
                }*/
            }
        }, delay);
        super.onResume();
    }

}
