package com.qdocs.smartschool.students;

import static android.widget.Toast.makeText;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.adapters.CbseExaminationAdapter;
import com.qdocs.smartschool.model.AssismentTypeModel;
import com.qdocs.smartschool.model.CbseExamModel;
import com.qdocs.smartschool.model.ExamAssismentModel;
import com.qdocs.smartschool.model.SubjectModel;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class CbseExaminationActivity extends BaseActivity {
    RecyclerView recyclerview;
    CardView card_view_outer;
    String marksvalue;
    CbseExaminationAdapter cbseExaminationAdapter;
    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<String> exam_total_markslist = new ArrayList<String>();
    ArrayList<String> exam_percentageList = new ArrayList<String>();
    ArrayList<String> exam_gradelist = new ArrayList<String>();
    ArrayList<String> exam_obtain_markslist = new ArrayList<String>();
    ArrayList<String> subjectlist = new ArrayList<String>();
    ArrayList<CbseExamModel> cbseexamlist = new ArrayList<>();
    LinearLayout cbesetimetable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_cbse_examination, null, false);
        mDrawerLayout.addView(contentView, 0);

        cbesetimetable = findViewById(R.id.cbesetimetable);
        cbesetimetable.setVisibility(View.VISIBLE);
        cbesetimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),CbseTimeTableActivity.class);
                startActivity(intent);
            }
        });
        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        defaultDateFormat = Utility.getSharedPreferences(getApplicationContext(), "dateFormat");
        currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        titleTV.setText(getApplicationContext().getString(R.string.cbseexamresult));

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        recyclerview=findViewById(R.id.recyclerview);
        cbseExaminationAdapter = new CbseExaminationAdapter(CbseExaminationActivity.this,
                cbseexamlist,null);
        recyclerview.setLayoutManager(new LinearLayoutManager(CbseExaminationActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(cbseExaminationAdapter);


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
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.cbseexamresultUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONArray dataArray = obj.getJSONArray("exams");
                        nameList.clear();
                        exam_total_markslist.clear();
                        exam_percentageList.clear();
                        exam_gradelist.clear();
                        exam_obtain_markslist.clear();
                        subjectlist.clear();

                        if(dataArray.length() != 0) {
                            for(int i = 0; i < dataArray.length(); i++) {
                                CbseExamModel cbseExamModel = new CbseExamModel();
                                cbseExamModel.setName(dataArray.getJSONObject(i).getString("name"));
                                cbseExamModel.setExam_total_marks(dataArray.getJSONObject(i).getString("exam_total_marks"));
                                cbseExamModel.setExam_percentage(dataArray.getJSONObject(i).getString("exam_percentage"));
                                cbseExamModel.setExam_grade(dataArray.getJSONObject(i).getString("exam_grade"));
                                cbseExamModel.setExam_rank(dataArray.getJSONObject(i).getString("exam_rank"));
                                cbseExamModel.setExam_obtain_marks(dataArray.getJSONObject(i).getString("exam_obtain_marks"));

                                JSONArray exam_assessmentsArray = dataArray.getJSONObject(i).getJSONArray("exam_assessments");
                                ArrayList<AssismentTypeModel> assismenttypeList = new ArrayList<>();
                                for(int k = 0; k<exam_assessmentsArray.length(); k++) {
                                    AssismentTypeModel assismentTypeModel=new AssismentTypeModel();
                                    assismentTypeModel.setName(exam_assessmentsArray.getJSONObject(k).getString("name"));
                                    assismentTypeModel.setCode(exam_assessmentsArray.getJSONObject(k).getString("code"));
                                    assismentTypeModel.setMaximum_marks(exam_assessmentsArray.getJSONObject(k).getString("maximum_marks"));

                                    assismenttypeList.add(assismentTypeModel);
                                }

                                float sum = 0;
                                JSONObject exam_dataObject = dataArray.getJSONObject(i).getJSONObject("exam_data");
                                JSONArray subjectsArray = exam_dataObject.getJSONArray("subjects");
                                ArrayList<SubjectModel> subjectList = new ArrayList<>();
                                for(int j = 0; j<subjectsArray.length(); j++) {
                                    SubjectModel model = new SubjectModel();
                                    model.setSubject_name(subjectsArray.getJSONObject(j).getString("subject_name"));
                                    model.setSubject_code(subjectsArray.getJSONObject(j).getString("subject_code"));
                                    JSONObject exam_assessmentsObject = subjectsArray.getJSONObject(j).getJSONObject("exam_assessments");
                                    Iterator<String> iter = exam_assessmentsObject.keys();
                                    ArrayList<ExamAssismentModel> assissmentList = new ArrayList<>();
                                    sum = 0;
                                    while(iter.hasNext()){
                                        String key = iter.next();
                                        ExamAssismentModel examAssismentModel=new ExamAssismentModel();
                                        JSONObject object1 = exam_assessmentsObject.getJSONObject(key);
                                        examAssismentModel.setCbse_exam_assessment_type_name(object1.getString("cbse_exam_assessment_type_name"));
                                        examAssismentModel.setMarks(object1.getString("marks"));
                                        examAssismentModel.setIs_absent(object1.getString("is_absent"));

                                        if(object1.getString("marks").equals("xx")){
                                            sum += Float.parseFloat("0");
                                            marksvalue=String.format("%.2f", sum);
                                        }else if(object1.getString("marks").equals("N/A")){
                                            sum += Float.parseFloat("0");
                                            marksvalue=String.format("%.2f", sum);
                                        }else{
                                            sum += Float.parseFloat(object1.getString("marks"));
                                            marksvalue=String.format("%.2f", sum);
                                        }

                                        //  examAssismentModel.setMarks(object1.getString("marks"));
                                        //total_payment.setText("Total: " +currency+  String.format("%.2f", sum));
                                        assissmentList.add(examAssismentModel);
                                    }

                                    model.setTotalmarks(marksvalue);
                                    model.setAssisment(assissmentList);
                                    subjectList.add(model);
                                }
                                cbseExamModel.setSubject(subjectList);
                                cbseExamModel.setAssismenttype(assismenttypeList);
                                cbseexamlist.add(cbseExamModel);
                            }
                            cbseExaminationAdapter.notifyDataSetChanged();
                        } else {}
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
                Toast.makeText(CbseExaminationActivity.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(CbseExaminationActivity.this); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }
}