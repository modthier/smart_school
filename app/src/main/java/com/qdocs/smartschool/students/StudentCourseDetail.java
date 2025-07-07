package com.qdocs.smartschool.students;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.adapters.CourseCurriculumAdapterNew;
import com.qdocs.smartschool.adapters.CourseReviewAdapterNew;
import com.qdocs.smartschool.model.LessonModel;
import com.qdocs.smartschool.model.SectionModel;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import static android.widget.Toast.makeText;

public class StudentCourseDetail  extends BaseActivity {
    ImageView courseimageBanner,teacher_ImageIV;
    ImageButton courseplaybutton;
    boolean fullscreen = false;
    String videourl;
    ImageView fullscreenButton;
    private Dialog mFullScreenDialog;
    RelativeLayout image_layout,youtube_layout;
    WebView webView;
    LinearLayout price_layout,lesson_layout,quiz_layout,hour_layout,rating_layout;
    public Map<String, String> headers = new HashMap<String, String>();
    public Map<String, String> params = new Hashtable<String, String>();
    TextView courseDescription,totalrating,courseLearnheading;
    TextView courseNameTV,teacherNameTV,teacherupdateDateTV,courselesson,coursequiz,coursediscount,courseclass,courseprice,buy_now_btn,coursehours;
    TextView courseLearnTV;
    LinearLayout rate;
    RecyclerView courseCurriculumTV,courseDetails_review;
    public Map<String, String> createParams = new Hashtable<String, String>();
    CardView review_layout,curriculum_layout;
    String CourseId="",teacherlist="",CourseTitle="",Courseprice="",Coursediscount="",Coursepaidlist="",Courseprogress="",classsection="";
    CourseCurriculumAdapterNew adapter;
    CourseReviewAdapterNew courseReviewAdapterNew;
    String CourseRating,totalCourseRating;
    ArrayList<SectionModel> sectionList = new ArrayList<>();
    CardView card_view_outer;
    RatingBar rating;
    String erating,ereview,id;
    ArrayList<String> reviewidList = new ArrayList<String>();
    ArrayList<String> reviewList = new ArrayList<String>();
    ArrayList<String> ratingList = new ArrayList<String>();
    ArrayList<String> dateList = new ArrayList<String>();
    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<String> imageList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_student_course_detail, null, false);
        mDrawerLayout.addView(contentView, 0);
        titleTV.setText(getApplicationContext().getString(R.string.courseDetail));
        CourseId = getIntent().getExtras().getString("CourseId");
        teacherlist = getIntent().getExtras().getString("teacherlist");
        CourseRating = getIntent().getExtras().getString("CourseRating");
        totalCourseRating = getIntent().getExtras().getString("totalCourseRating");
        Coursepaidlist = getIntent().getExtras().getString("paidlist");
        Courseprice = getIntent().getExtras().getString("price");
        Coursediscount = getIntent().getExtras().getString("discount");
        Courseprogress = getIntent().getExtras().getString("course_progress");
        classsection = getIntent().getExtras().getString("classsection");
        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        price_layout=findViewById(R.id.price_layout);
        quiz_layout=findViewById(R.id.quiz_layout);
        lesson_layout=findViewById(R.id.lesson_layout);
        curriculum_layout=findViewById(R.id.curriculum_layout);
        hour_layout=findViewById(R.id.hour_layout);
        rating_layout=findViewById(R.id.rating_layout);
        review_layout=findViewById(R.id.review_layout);
        rate=findViewById(R.id.rate);
        courseLearnheading=findViewById(R.id.courseLearnheading);
        courseLearnheading.setText(getApplicationContext().getString(R.string.courseLearn));
        rating =findViewById(R.id.rating);
        loaddata();

        courseDescription=findViewById(R.id.courseDetailsdescription);
        courseNameTV=findViewById(R.id.courseDetails_courseNameTV);
        teacherNameTV=findViewById(R.id.teacherNameTV);
        teacherupdateDateTV=findViewById(R.id.updateDateTV);
        teacher_ImageIV=findViewById(R.id.teacher_ImageIV);
        courseimageBanner=findViewById(R.id.courseDetail_imageBanner);
        courseplaybutton=findViewById(R.id.coursedetail_playbutton);
        courselesson=findViewById(R.id.courseDetail_lesson);
        coursehours=findViewById(R.id.courseDetail_hours);
        coursequiz=findViewById(R.id.courseDetail_quiz);
        buy_now_btn=findViewById(R.id.buy_now_btn);
        courseclass=findViewById(R.id.courseDetail_class);
        image_layout = findViewById(R.id.image_layout);
        teacherNameTV .setText(teacherlist);
        webView = findViewById(R.id.webView);
        youtube_layout = findViewById(R.id.youtube_layout);

        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        totalrating =findViewById(R.id.totalrating);
        if(CourseRating.equals("0")){
        }else{
            totalrating.setText(" ("+totalCourseRating+" "+getApplicationContext().getString(R.string.ratings)+")");
            rating.setRating((int)Float.parseFloat(CourseRating));
        }

        webView.getSettings().setJavaScriptEnabled(true); // enable javascript
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new ChromeClient());
        final Activity activity = this;

        courseprice=findViewById(R.id.courseDetail_price);
        coursediscount=findViewById(R.id.courseDetail_discount);
        courseprice.setText(Courseprice);

        System.out.println("Courseprice=="+Courseprice);
        if(Courseprice.equals("Free")){
            coursediscount.setVisibility(View.GONE);
            courseprice.setText(Courseprice);
            price_layout.setVisibility(View.VISIBLE);
            review_layout.setVisibility(View.VISIBLE);
            if(Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("parent")){
                buy_now_btn.setVisibility(View.GONE);
                rate.setVisibility(View.GONE);
            }else{
                rate.setVisibility(View.VISIBLE);
                buy_now_btn.setVisibility(View.VISIBLE);
                buy_now_btn.setText(getApplicationContext().getString(R.string.startlesson));
                buy_now_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        coursediscount.setVisibility(View.GONE);
                        Intent intent=new Intent(getApplicationContext(), StudentStartLessonActivity.class);
                        intent.putExtra("CourseId",CourseId);
                        intent.putExtra("course_name",CourseTitle);
                        System.out.println("course_name="+CourseTitle);
                        startActivity(intent);
                    }
                });
            }

        }else{
            if(Coursediscount.equals("")){
                coursediscount.setVisibility(View.GONE);
                courseprice.setText(Courseprice);
                if(Courseprogress.equals("0")){
                    if(Coursepaidlist.equals("1")){
                        review_layout.setVisibility(View.VISIBLE);
                        price_layout.setVisibility(View.GONE);
                        if(Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("parent")){
                            buy_now_btn.setVisibility(View.GONE);
                            rate.setVisibility(View.GONE);
                        }else {
                            rate.setVisibility(View.VISIBLE);
                            buy_now_btn.setVisibility(View.VISIBLE);
                            buy_now_btn.setText(getApplicationContext().getString(R.string.startlesson));
                            buy_now_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    coursediscount.setVisibility(View.GONE);
                                    Intent intent = new Intent(getApplicationContext(), StudentStartLessonActivity.class);
                                    intent.putExtra("CourseId", CourseId);
                                    intent.putExtra("course_name", CourseTitle);
                                    System.out.println("course_name=" + CourseTitle);
                                    startActivity(intent);
                                }
                            });
                        }
                    }else{
                        rate.setVisibility(View.GONE);
                        review_layout.setVisibility(View.VISIBLE);
                        buy_now_btn.setText(getApplicationContext().getString(R.string.buynow)+" "+Courseprice);
                        buy_now_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(getApplicationContext(), CoursePayment.class);
                                intent.putExtra("CourseId",CourseId);
                                startActivity(intent);
                            }
                        });
                    }
                }else{
                    review_layout.setVisibility(View.VISIBLE);
                    if(Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("parent")){
                        buy_now_btn.setVisibility(View.GONE);
                        rate.setVisibility(View.GONE);
                    }else {
                        rate.setVisibility(View.VISIBLE);
                        buy_now_btn.setVisibility(View.VISIBLE);
                        buy_now_btn.setText(getApplicationContext().getString(R.string.startlesson));
                        price_layout.setVisibility(View.GONE);
                        buy_now_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                coursediscount.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(), StudentStartLessonActivity.class);
                                intent.putExtra("CourseId", CourseId);
                                intent.putExtra("course_name", CourseTitle);
                                System.out.println("course_name=" + CourseTitle);
                                startActivity(intent);
                            }
                        });
                    }
                }

            }else if(Coursediscount.equals("0.00")){
                coursediscount.setVisibility(View.GONE);
                courseprice.setText(Courseprice);
                if(Courseprogress.equals("0")){
                    if(Coursepaidlist.equals("1")){
                        review_layout.setVisibility(View.VISIBLE);
                        if(Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("parent")){
                            buy_now_btn.setVisibility(View.GONE);
                            rate.setVisibility(View.GONE);
                        }else {
                            rate.setVisibility(View.VISIBLE);
                            buy_now_btn.setVisibility(View.VISIBLE);
                            buy_now_btn.setText(getApplicationContext().getString(R.string.startlesson));
                            price_layout.setVisibility(View.GONE);
                            buy_now_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    coursediscount.setVisibility(View.GONE);
                                    Intent intent = new Intent(getApplicationContext(), StudentStartLessonActivity.class);
                                    intent.putExtra("CourseId", CourseId);
                                    intent.putExtra("course_name", CourseTitle);
                                    System.out.println("course_name=" + CourseTitle);
                                    startActivity(intent);
                                }
                            });
                        }
                    }else{
                        rate.setVisibility(View.GONE);
                        review_layout.setVisibility(View.VISIBLE);
                        buy_now_btn.setText(getApplicationContext().getString(R.string.buynow)+" "+Courseprice);
                        buy_now_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(getApplicationContext(), CoursePayment.class);
                                intent.putExtra("CourseId",CourseId);
                                startActivity(intent);
                            }
                        });
                    }
                }else{
                    review_layout.setVisibility(View.VISIBLE);
                    if(Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("parent")){
                        rate.setVisibility(View.GONE);
                        buy_now_btn.setVisibility(View.GONE);
                    }else {
                        rate.setVisibility(View.VISIBLE);
                        buy_now_btn.setVisibility(View.VISIBLE);
                        buy_now_btn.setText(getApplicationContext().getString(R.string.startlesson));
                        price_layout.setVisibility(View.GONE);
                        buy_now_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                coursediscount.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(), StudentStartLessonActivity.class);
                                intent.putExtra("CourseId", CourseId);
                                intent.putExtra("course_name", CourseTitle);
                                System.out.println("course_name=" + CourseTitle);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }else{
                courseprice.setText(Courseprice);
                courseprice.setPaintFlags(courseprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                coursediscount.setVisibility(View.VISIBLE);
                coursediscount.setText(Coursediscount);
                if(Courseprogress.equals("0")){
                    if(Coursepaidlist.equals("1")){
                        review_layout.setVisibility(View.VISIBLE);
                        if(Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("parent")){
                            buy_now_btn.setVisibility(View.GONE);
                            rate.setVisibility(View.GONE);
                        }else {
                            rate.setVisibility(View.VISIBLE);
                            buy_now_btn.setVisibility(View.VISIBLE);
                            buy_now_btn.setText(getApplicationContext().getString(R.string.startlesson));
                            price_layout.setVisibility(View.GONE);
                            buy_now_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    coursediscount.setVisibility(View.GONE);
                                    Intent intent = new Intent(getApplicationContext(), StudentStartLessonActivity.class);
                                    intent.putExtra("CourseId", CourseId);
                                    intent.putExtra("course_name", CourseTitle);
                                    System.out.println("course_name=" + CourseTitle);
                                    startActivity(intent);
                                }
                            });
                        }
                    }else{
                        rate.setVisibility(View.GONE);
                        review_layout.setVisibility(View.VISIBLE);
                        buy_now_btn.setText(getApplicationContext().getString(R.string.buynow)+" "+Coursediscount);
                        buy_now_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(getApplicationContext(), CoursePayment.class);
                                intent.putExtra("CourseId",CourseId);
                                startActivity(intent);
                            }
                        });
                    }
                }else{
                    review_layout.setVisibility(View.VISIBLE);
                    if(Utility.getSharedPreferences(getApplicationContext(), Constants.loginType).equals("parent")){
                        rate.setVisibility(View.GONE);
                        buy_now_btn.setVisibility(View.GONE);
                    }else {
                        rate.setVisibility(View.VISIBLE);
                        buy_now_btn.setVisibility(View.VISIBLE);
                        buy_now_btn.setText(getApplicationContext().getString(R.string.startlesson));
                        price_layout.setVisibility(View.GONE);
                        buy_now_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                coursediscount.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(), StudentStartLessonActivity.class);
                                intent.putExtra("CourseId", CourseId);
                                intent.putExtra("course_name", CourseTitle);
                                System.out.println("course_name=" + CourseTitle);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        }

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(StudentCourseDetail.this);
                dialog.setContentView(R.layout.add_course_rating);

                RelativeLayout headerLay = (RelativeLayout) dialog.findViewById(R.id.addTask_dialog_header);
                ImageView closeBtn = (ImageView) dialog.findViewById(R.id.addTask_dialog_crossIcon);

                final RatingBar ratingbar = (RatingBar) dialog.findViewById(R.id.rating);
                final TextInputEditText review = dialog.findViewById(R.id.review);
                Button submitBtn = dialog.findViewById(R.id.addTask_dialog_submitBtn);
                submitBtn.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
                review.setText(ereview);
                if(!erating.equals("")){
                    ratingbar.setRating(Float.parseFloat(erating));
                }
                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String rating=String.valueOf(ratingbar.getRating());
                       if (review.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.enterreview), Toast.LENGTH_LONG).show();
                        }else if(rating.equals("0.0")){
                           Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.ratingreq), Toast.LENGTH_LONG).show();
                       } else {
                            if (Utility.isConnectingToInternet(getApplicationContext())) {
                                createParams.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
                                createParams.put("review", review.getText().toString());
                                createParams.put("rating", rating);
                                createParams.put("course_id", CourseId);
                                createParams.put("id", id);
                                JSONObject obj=new JSONObject(createParams);
                                System.out.println("rating params===="+obj.toString());
                                createApi(obj.toString());
                            } else {
                                makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                //DECORATE
                headerLay.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
                //DECORATE
                dialog.show();
            }
        });

        courseLearnTV=findViewById(R.id.courseDetails_courseLearnTV);

        courseCurriculumTV=findViewById(R.id.courseDetails_courseCurriculumTV);
        adapter = new CourseCurriculumAdapterNew(StudentCourseDetail.this, sectionList, null);
        courseCurriculumTV.setLayoutManager(new LinearLayoutManager(StudentCourseDetail.this, LinearLayoutManager.VERTICAL, false));
        courseCurriculumTV.setItemAnimator(new DefaultItemAnimator());
        courseCurriculumTV.setAdapter(adapter);

        courseDetails_review=findViewById(R.id.courseDetails_review);
        courseReviewAdapterNew = new CourseReviewAdapterNew(StudentCourseDetail.this, reviewidList,reviewList,ratingList,dateList,nameList,imageList);
        courseDetails_review.setLayoutManager(new LinearLayoutManager(StudentCourseDetail.this, LinearLayoutManager.VERTICAL, false));
        courseDetails_review.setItemAnimator(new DefaultItemAnimator());
        courseDetails_review.setAdapter(courseReviewAdapterNew);

    }

    public  void  loaddata(){
        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("course_id",CourseId);
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), Constants.studentId));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            System.out.println("params== "+ obj.toString());
            getDataFromApi(obj.toString());
        }else{
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("course_id", CourseId);
            JSONObject objt=new JSONObject(params);
            Log.e("params ", objt.toString());
            getCourseCurrFromApi(objt.toString());
        }else{
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("course_id", CourseId);
            JSONObject objt=new JSONObject(params);
            Log.e("params ", objt.toString());
            System.out.println("params== "+objt.toString());
            getReviewApi(objt.toString());
        }else{
            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void createApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.addCourseRatingandReviewUrl;
        Log.e("URL",url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        String status = object.getString("status");

                        Toast.makeText(getApplicationContext(), object.getString("msg"), Toast.LENGTH_LONG).show();

                        if(status.equals("1")) {
                            finish();
                            startActivity(getIntent());
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
                Toast.makeText(StudentCourseDetail.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentCourseDetail.this);//Creating a Request Queue
        requestQueue.add(stringRequest);//Adding request to the queue
    }
    private void getReviewApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getCourseReviewsUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pd.dismiss();
                    try{
                        Log.e("Result", result);
                        System.out.println("Result=="+ result);
                        JSONObject obj = new JSONObject(result);
                        JSONArray resultarray = obj.getJSONArray("result_array");
                        reviewidList.clear();
                        reviewList.clear();
                        ratingList.clear();
                        dateList.clear();
                        nameList.clear();
                        imageList.clear();
                        if(resultarray.length() != 0) {
                            review_layout.setVisibility(View.VISIBLE);
                            for(int i = 0; i < resultarray.length(); i++) {
                                reviewidList.add(resultarray.getJSONObject(i).getString("id"));
                                reviewList.add(resultarray.getJSONObject(i).getString("review"));
                                ratingList.add(resultarray.getJSONObject(i).getString("rating"));
                                dateList.add(Utility.parseDate("yyyy-MM-dd", defaultDateFormat,resultarray.getJSONObject(i).getString("date")));
                               if(resultarray.getJSONObject(i).getString("student_id").equals("0")){
                                   nameList.add(resultarray.getJSONObject(i).getString("rating_provider_name"));
                               }else{
                                   nameList.add(resultarray.getJSONObject(i).getString("rating_provider_name")+" "+resultarray.getJSONObject(i).getString("lastname"));
                               }
                                imageList.add(resultarray.getJSONObject(i).getString("image"));
                            }
                        }else{
                            review_layout.setVisibility(View.GONE);
                        }

                        courseReviewAdapterNew.notifyDataSetChanged();

                    }catch (JSONException e) {
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
                Toast.makeText(StudentCourseDetail.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(StudentCourseDetail.this, "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(StudentCourseDetail.this, "accessToken"));
                Log.e("Headers new added", headers.toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentCourseDetail.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getCourseCurrFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.coursecurriculumUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                ArrayList<String> listParent = new ArrayList<String>();
                if (result != null) {
                    pd.dismiss();
                    try{
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONArray dataArray = obj.getJSONArray("sectionList");
                        if(dataArray.length() != 0) {
                         curriculum_layout.setVisibility(View.VISIBLE);
                            for (int j = 0; j < dataArray.length(); j++) {
                                SectionModel sectionrModel = new SectionModel();
                                sectionrModel.setSection_title(dataArray.getJSONObject(j).getString("section_title"));

                                JSONArray lessonArray = dataArray.getJSONObject(j).getJSONArray("lesson_quiz");

                                ArrayList<LessonModel> lessonArrayList = new ArrayList<>();
                                for (int i = 0; i < lessonArray.length(); i++) {
                                    LessonModel model = new LessonModel();
                                    model.setLessonTitle(lessonArray.getJSONObject(i).getString("lesson_title"));
                                    model.setQuizTitle(lessonArray.getJSONObject(i).getString("quiz_title"));
                                    model.setType(lessonArray.getJSONObject(i).getString("type"));
                                    model.setDuration(lessonArray.getJSONObject(i).getString("duration"));
                                    model.setQuiz_id(lessonArray.getJSONObject(i).getString("quiz_id"));
                                    lessonArrayList.add(model);
                                }

                                sectionrModel.setLessons(lessonArrayList);
                                sectionList.add(sectionrModel);
                            }
                        }else{
                            curriculum_layout.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();

                    }catch (JSONException e) {
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
                Toast.makeText(StudentCourseDetail.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(StudentCourseDetail.this, "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(StudentCourseDetail.this, "accessToken"));
                Log.e("Headers new added", headers.toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentCourseDetail.this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.coursedetailUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        System.out.println("Result=="+ result);
                        JSONObject obj = new JSONObject(result);
                        final JSONObject dataArray = obj.getJSONObject("course_detail");
                        final JSONArray rating_review = obj.getJSONArray("course_rating_review");
                        if(rating_review.length() != 0) {
                            erating= rating_review.getJSONObject(0).get("rating").toString();
                            ereview = rating_review.getJSONObject(0).get("review").toString();
                            id = rating_review.getJSONObject(0).get("id").toString();
                            System.out.println("erating=="+erating);
                            System.out.println("ereview=="+ereview);
                            System.out.println("id=="+id);
                        }else{
                            erating="";
                            ereview="";
                            id="";
                        }
                        courseNameTV.setText(dataArray.getString("title"));
                        courseDescription.setText(dataArray.getString("description"));
                        teacherupdateDateTV.setText(Utility.parseDate("yyyy-MM-dd hh:mm:ss", defaultDateFormat,dataArray.getString("updated_date")));
                     //   makeText(StudentCourseDetail.this, dataArray.getString("updated_date"), Toast.LENGTH_SHORT).show();
                        if(dataArray.getString("lesson_count").equals("0")){
                            lesson_layout.setVisibility(View.GONE);
                        }else{
                            lesson_layout.setVisibility(View.VISIBLE);
                            courselesson.setText(getApplicationContext().getString(R.string.lessons)+" "+dataArray.getString("lesson_count"));
                        }
                        if(dataArray.getString("quiz_count").equals("0")){
                            quiz_layout.setVisibility(View.GONE);
                        }else{
                            quiz_layout.setVisibility(View.VISIBLE);
                            coursequiz.setText(getApplicationContext().getString(R.string.quiz)+" "+dataArray.getString("quiz_count"));
                        }
                        if(dataArray.getString("total_hour").equals("00:00:00")){
                            hour_layout.setVisibility(View.GONE);
                        }else{
                            hour_layout.setVisibility(View.VISIBLE);
                            coursehours.setText(dataArray.getString("total_hour")+" Hrs");
                        }

                        courseclass.setText(dataArray.getString("class")+" "+classsection);

                        String input = dataArray.getString("outcomes");
                        String[] words = input.substring(1, input.length() - 2).replaceAll("\"", "").split(", ");
                        System.out.println("courselearn words=="+words);
                        JSONArray jsonArray = new JSONArray(input);
                        String[] strArr = new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            strArr[i] = jsonArray.getString(i);
                        }
                        System.out.println("courselearn=="+Arrays.toString(strArr));

                        courseLearnTV.setText(Arrays.toString(strArr).replaceAll("\\[|\\]", ""));
                        List<String> stringList = new ArrayList<String>(Arrays.asList(strArr));
                        System.out.println("courselearn stringList=="+stringList);

                        String teacher_img = Utility.getSharedPreferences(getApplicationContext(), "imagesUrl")+"uploads/staff_images/"+dataArray.getString("image");
                        Picasso.with(getApplicationContext()).load(teacher_img).placeholder(R.drawable.placeholder_user).memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE).into(teacher_ImageIV);

                        final String course_url=dataArray.getString("course_url");
                        final String video_id=dataArray.getString("video_id");
                        final String course_provider=dataArray.getString("course_provider");
                        if(course_url.equals("")){
                            image_layout.setVisibility(View.VISIBLE);
                            youtube_layout.setVisibility(View.GONE);
                            String thumnail = Utility.getSharedPreferences(getApplicationContext(), "imagesUrl")+"uploads/course/course_thumbnail/"+dataArray.getString("course_thumbnail");
                            Glide.with(getApplicationContext())
                                    .load(thumnail)
                                    .into(courseimageBanner);
                            courseplaybutton.setVisibility(View.GONE);
                        }else {
                            image_layout.setVisibility(View.GONE);
                            youtube_layout.setVisibility(View.VISIBLE);
                            courseplaybutton.setVisibility(View.VISIBLE);
                            if (course_provider.equals("html5")) {
                                        final ProgressDialog pd = ProgressDialog.show(StudentCourseDetail.this, "", "Loading..", true);
                                        if (Utility.isConnectingToInternet(getApplicationContext())) {
                                            videourl = course_url;
                                        } else {
                                            makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                                        }
                                        webView.setWebViewClient(new WebViewClient() {
                                            @SuppressWarnings("deprecation")
                                            @Override
                                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                                            }
                                            @TargetApi(Build.VERSION_CODES.M)
                                            @Override
                                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                                pd.show();
                                            }
                                            @Override
                                            public void onPageFinished(WebView view, String url) {
                                                pd.dismiss();
                                            }
                                        });
                                        Log.e("Video URL", "URL " + videourl);
                                        webView.loadUrl(videourl);
                            } else if(course_provider.equals("youtube")) {

                            final ProgressDialog pd = ProgressDialog.show(StudentCourseDetail.this, "", "Loading..", true);

                            if (Utility.isConnectingToInternet(getApplicationContext())) {
                                videourl = "http://www.youtube.com/embed/" + video_id + "?autoplay=1&vq=small";
                                System.out.println("videourl=="+videourl);
                            } else {
                                makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                            }

                            webView.setWebViewClient(new WebViewClient() {
                                @SuppressWarnings("deprecation")
                                @Override
                                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                    Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
                                }

                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                                    onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                                }

                                @Override
                                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                    pd.show();
                                }

                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    pd.dismiss();
                                }
                            });
                            Log.e("Video URL", "URL " + videourl);
                            webView.loadUrl(videourl);
                            }
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
                Toast.makeText(StudentCourseDetail.this,R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentCourseDetail.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }


    public class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }


}
