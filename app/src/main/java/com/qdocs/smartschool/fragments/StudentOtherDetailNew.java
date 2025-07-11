package com.qdocs.smartschool.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.qdocs.smartschool.students.StudentProfileDetailsNew;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.makeText;

@SuppressLint("ValidFragment")
public class StudentOtherDetailNew extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    ArrayList<String> appointment_datelist = new ArrayList<String>();
    ArrayList<String> opd_noList = new ArrayList<String>();
    ArrayList<String> consultantList = new ArrayList<String>();
    ArrayList<String> refferencelist = new ArrayList<String>();
    ArrayList<String> symptomslist = new ArrayList<String>();
    private String visitid;
    SwipeRefreshLayout pullToRefresh;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    TextView previousSchool,nationalIdNo,localIdNo,bankAcNo,bankName,ifscCode,rte,pickup_point,studentHouse,vehicleRoute,vehicleNo,driverName,driverContact,hostel,hostelRoomNo,hostelRoomType;
    LinearLayout previousSchool_layout,nationalIdNo_layout,localIdNo_layout,bankAcNo_layout,bankName_layout,ifscCode_layout,
            rte_layout,pickup_point_layout,studentHouse_layout,vehicleRoute_layout,vehicleNo_layout,driverName_layout,driverContact_layout,hostel_layout,hostelRoomNo_layout,hostelRoomType_layout;
    public String defaultDateFormat, currency;
    @SuppressLint("ValidFragment")
    public StudentOtherDetailNew() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {

        if(Utility.isConnectingToInternet(getActivity())){
            params.put("student_id", Utility.getSharedPreferences(getActivity(), "studentId"));
            params.put("user_type", Utility.getSharedPreferences(getActivity(), Constants.loginType));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        }else{
            makeText(getActivity(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_other_list, container, false);
        defaultDateFormat = Utility.getSharedPreferences(getActivity(), "dateFormat");
        currency = Utility.getSharedPreferences(getActivity(), Constants.currency);

        previousSchool_layout=mainView.findViewById(R.id.previousSchool_layout);
        previousSchool=mainView.findViewById(R.id.previousSchool);

        nationalIdNo_layout=mainView.findViewById(R.id.nationalIdNo_layout);
        nationalIdNo=mainView.findViewById(R.id.nationalIdNo);

        localIdNo_layout=mainView.findViewById(R.id.localIdNo_layout);
        localIdNo=mainView.findViewById(R.id.localIdNo);

        bankAcNo_layout=mainView.findViewById(R.id.bankAcNo_layout);
        bankAcNo=mainView.findViewById(R.id.bankAcNo);

        bankName_layout=mainView.findViewById(R.id.bankName_layout);
        bankName=mainView.findViewById(R.id.bankName);

        ifscCode_layout=mainView.findViewById(R.id.ifscCode_layout);
        ifscCode=mainView.findViewById(R.id.ifscCode);

        rte_layout=mainView.findViewById(R.id.rte_layout);
        rte=mainView.findViewById(R.id.rte);

        studentHouse_layout=mainView.findViewById(R.id.studentHouse_layout);
        studentHouse=mainView.findViewById(R.id.studentHouse);

        pickup_point_layout=mainView.findViewById(R.id.pickup_point_layout);
        pickup_point=mainView.findViewById(R.id.pickup_point);

        vehicleRoute_layout=mainView.findViewById(R.id.vehicleRoute_layout);
        vehicleRoute=mainView.findViewById(R.id.vehicleRoute);

        vehicleNo_layout=mainView.findViewById(R.id.vehicleNo_layout);
        vehicleNo=mainView.findViewById(R.id.vehicleNo);

        driverName_layout=mainView.findViewById(R.id.driverName_layout);
        driverName=mainView.findViewById(R.id.driverName);

        driverContact_layout=mainView.findViewById(R.id.driverContact_layout);
        driverContact=mainView.findViewById(R.id.driverContact);

        hostel_layout=mainView.findViewById(R.id.hostel_layout);
        hostel=mainView.findViewById(R.id.hostel);

        hostelRoomNo_layout=mainView.findViewById(R.id.hostelRoomNo_layout);
        hostelRoomNo=mainView.findViewById(R.id.hostelRoomNo);

        hostelRoomType_layout=mainView.findViewById(R.id.hostelRoomType_layout);
        hostelRoomType=mainView.findViewById(R.id.hostelRoomType);

        pullToRefresh =mainView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(this);

        pullToRefresh.post(new Runnable() {
                               @Override
                               public void run() {
                                   pullToRefresh.setRefreshing(true);
                                   loadData();
                               }
                           }
        );
        return mainView;
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private void getDataFromApi (String bodyParams) {
        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getActivity().getApplicationContext(), "apiUrl")+Constants.getStudentProfileUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                pullToRefresh.setRefreshing(false);
                if (result != null) {
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONObject dataArray = obj.getJSONObject("student_result");
                        String defaultDateFormat = Utility.getSharedPreferences(getActivity().getApplicationContext(), "dateFormat");

                        previousSchool.setText(dataArray.getString("previous_school"));
                        nationalIdNo.setText(dataArray.getString("adhar_no"));
                        localIdNo.setText(dataArray.getString("samagra_id"));
                        bankAcNo.setText(dataArray.getString("bank_account_no"));
                        bankName.setText(dataArray.getString("bank_name"));
                        ifscCode.setText(dataArray.getString("ifsc_code"));
                        rte.setText(dataArray.getString("rte"));
                        studentHouse.setText(dataArray.getString("house_name"));
                        pickup_point.setText(dataArray.getString("pickup_point_name"));
                        vehicleRoute.setText(dataArray.getString("route_title"));
                        vehicleNo.setText(dataArray.getString("vehicle_no"));
                        driverName.setText(dataArray.getString("driver_name"));
                        driverContact.setText(dataArray.getString("driver_contact"));
                        hostel.setText(dataArray.getString("hostel_name"));
                        hostelRoomNo.setText(dataArray.getString("room_no"));
                        hostelRoomType.setText(dataArray.getString("room_type"));

                        if(dataArray.getString("hostel_name").equals("")){
                            hostel_layout.setVisibility(View.GONE);
                            hostelRoomNo_layout.setVisibility(View.GONE);
                            hostelRoomType_layout.setVisibility(View.GONE);
                        }else{
                            hostel_layout.setVisibility(View.VISIBLE);
                            hostelRoomNo_layout.setVisibility(View.VISIBLE);
                            hostelRoomType_layout.setVisibility(View.VISIBLE);
                        }

                        if(dataArray.getString("route_title").equals("")){
                            pickup_point_layout.setVisibility(View.GONE);
                            vehicleRoute_layout.setVisibility(View.GONE);
                            vehicleNo_layout.setVisibility(View.GONE);
                            driverName_layout.setVisibility(View.GONE);
                            driverContact_layout.setVisibility(View.GONE);
                        }else{
                            pickup_point_layout.setVisibility(View.VISIBLE);
                            vehicleRoute_layout.setVisibility(View.VISIBLE);
                            vehicleNo_layout.setVisibility(View.VISIBLE);
                            driverName_layout.setVisibility(View.VISIBLE);
                            driverContact_layout.setVisibility(View.VISIBLE);
                        }

                        JSONObject fieldsArray = obj.getJSONObject("student_fields");
                        System.out.println("fieldsArray=="+fieldsArray);
                        if(!fieldsArray.has("rte")){
                            rte_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("local_identification_no")){
                            localIdNo_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("previous_school")){
                            previousSchool_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("national_identification_no")){
                            nationalIdNo_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("bank_account_no")){
                            bankAcNo_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("bank_name")){
                            bankName_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("ifsc_code")){
                            ifscCode_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("house_name")){
                            studentHouse_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("route_list")){
                            pickup_point_layout.setVisibility(View.GONE);
                            vehicleRoute_layout.setVisibility(View.GONE);
                            vehicleNo_layout.setVisibility(View.GONE);
                            driverName_layout.setVisibility(View.GONE);
                            driverContact_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("hostel_id")){
                            hostel_layout.setVisibility(View.GONE);
                            hostelRoomNo_layout.setVisibility(View.GONE);
                            hostelRoomType_layout.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(getActivity().getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getActivity().getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getActivity().getApplicationContext(), "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }
}