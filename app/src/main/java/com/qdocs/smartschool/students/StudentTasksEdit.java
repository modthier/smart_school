package com.qdocs.smartschool.students;

import static android.widget.Toast.makeText;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StudentTasksEdit extends BaseActivity   {
    String startweek;
    private boolean isDateSelected = false;
    ArrayList<String> taskIdList = new ArrayList<>();
    ArrayList<String> taskTitleList = new ArrayList<>();
    ArrayList<String> taskStatusList = new ArrayList<>();
    ArrayList<String> taskDateList = new ArrayList<>();
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> createTaskParams = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    SwipeRefreshLayout pullToRefresh;
    CardView card_view_outer;
    TextInputEditText titleET,dateTV;
    String title,date,id;
    Button submitBtn;
    String dates;
    String fromdate = "";
    private boolean isfromDateSelected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.edit_task_dialog, null, false);
        mDrawerLayout.addView(contentView, 0);
        final String defaultDateFormat = Utility.getSharedPreferences(getApplicationContext(), "dateFormat");
        titleTV.setText(getApplicationContext().getString(R.string.edittask));
        startweek = Utility.getSharedPreferences(getApplicationContext(), "startWeek");
        titleET=findViewById(R.id.addTask_dialog_titleET);
        dateTV=findViewById(R.id.addTask_dialog_dateTV);
        submitBtn=findViewById(R.id.addTask_dialog_submitBtn);
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        date = bundle.getString("date");
        id = bundle.getString("id");
        titleET.setText(title);
        dateTV.setText(Utility.parseDate("yyyy-MM-dd hh:mm:ss", defaultDateFormat,date));

        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mDay   = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mYear  = mcurrentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(StudentTasksEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        //month = month + 1;
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(selectedyear, selectedmonth, selectedday);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        date=sdf.format(newDate.getTime());
                        SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
                        dateTV.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat,sdfdate.format(newDate.getTime())));
                        isfromDateSelected=true;
                    }
                }, mYear, mMonth, mDay);
                if(startweek.equals("Monday")){
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
                }else if(startweek.equals("Tuesday")){
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.TUESDAY);
                }else if(startweek.equals("Wednesday")){
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.WEDNESDAY);
                }else if(startweek.equals("Thursday")){
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.THURSDAY);
                }else if(startweek.equals("Friday")) {
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.FRIDAY);
                }else if(startweek.equals("Saturday")){
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.SATURDAY);
                }else if(startweek.equals("Sunday")){
                    datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.SUNDAY);
                }
                datePickerDialog.show();
            }
        });

        submitBtn.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dateTV.getText().equals("")) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.selectDateError), Toast.LENGTH_LONG).show();
                } else if (titleET.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.selectTitleError), Toast.LENGTH_LONG).show();
                } else {
                    if (Utility.isConnectingToInternet(getApplicationContext())) {
                        createTaskParams.put("user_id", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                        createTaskParams.put("event_title", titleET.getText().toString());
                        createTaskParams.put("task_id", id);
                        createTaskParams.put("date", date);
                        JSONObject obj=new JSONObject(createTaskParams);
                        Log.e("submit detail= ", obj.toString());
                        System.out.println("submit detail= "+obj.toString());
                        createTaskApi(obj.toString());
                    } else {
                        makeText(getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void createTaskApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.createTaskUrl;
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
                        Toast.makeText(getApplicationContext(), R.string.submit_success, Toast.LENGTH_LONG).show();
                        if(status.equals("1")) {
                            finish();
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
                Toast.makeText(StudentTasksEdit.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentTasksEdit.this);//Creating a Request Queue
        requestQueue.add(stringRequest);//Adding request to the queue
    }

}
