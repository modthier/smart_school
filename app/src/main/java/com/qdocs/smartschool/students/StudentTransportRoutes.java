package com.qdocs.smartschool.students;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.adapters.StudentTransportAdapter;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.R;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import static android.widget.Toast.makeText;

public class StudentTransportRoutes extends BaseActivity {

    RecyclerView transportList;
    StudentTransportAdapter adapter;
    ArrayList<String> pickup_timeList = new ArrayList<String>();
    ArrayList<String> destination_distanceList = new ArrayList<String>();
    ArrayList<String> pickup_pointList = new ArrayList<String>();
    TextView routetitle,vehicleNo,vehicleModel,driverName,driverContact,driverLicence,made;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
     CardView card_view_outer;
     ImageView vehiclePhoto;
    String pickupname="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_transport_routes_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        titleTV.setText(getApplicationContext().getString(R.string.transportRoute));
        card_view_outer = findViewById(R.id.card_view_outer);
        routetitle = findViewById(R.id.routetitle);
        vehicleNo = findViewById(R.id.vehicleNo);
        vehicleModel = findViewById(R.id.vehicleModel);
        driverName = findViewById(R.id.driverName);
        vehiclePhoto = findViewById(R.id.topimage);
        driverContact = findViewById(R.id.driverContact);
        driverLicence = findViewById(R.id.driverLicence);
        made = findViewById(R.id.made);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));

        if(Utility.isConnectingToInternet(getApplicationContext())){
            params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        }else{
            makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

        transportList = (RecyclerView) findViewById(R.id.studentTransport_listView);
        RecyclerView.LayoutManager mondayLayoutManager = new LinearLayoutManager(getApplicationContext());
        transportList.setLayoutManager(mondayLayoutManager);
        transportList.setItemAnimator(new DefaultItemAnimator());

    }

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getTransportRouteListUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject resultdata = new JSONObject(result);
                        JSONObject dataArray = resultdata.getJSONObject("route");
                       pickupname = dataArray.getString("pickup_point_name");

                       routetitle.setText(dataArray.getString("route_title"));
                       vehicleNo.setText( dataArray.getString("vehicle_no"));
                       if(!dataArray.getString("vehicle_photo").equals("")) {
                           String bimgUrl = Utility.getSharedPreferences(getApplicationContext(), "imagesUrl") + "uploads/vehicle_photo/" + dataArray.getString("vehicle_photo");
                           Picasso.with(getApplicationContext()).load(bimgUrl).into(vehiclePhoto);
                           System.out.println("vehicle photo=" + bimgUrl);
                       }else{
                           Picasso.with(getApplicationContext()).load(R.drawable.transport_page).into(vehiclePhoto);
                       }
                       driverName.setText( dataArray.getString("driver_name"));
                       driverContact.setText( dataArray.getString("driver_contact"));
                       vehicleModel.setText( dataArray.getString("vehicle_model"));
                       driverLicence.setText( dataArray.getString("driver_licence"));
                       made.setText( dataArray.getString("manufacture_year"));

                        JSONArray data = resultdata.getJSONArray("pickup_point");
                        pickup_pointList.clear();
                        destination_distanceList.clear();
                        pickup_timeList.clear();
                        if (data.length() != 0) {
                            for(int i = 0; i < data.length(); i++) {
                                pickup_pointList.add(data.getJSONObject(i).getString("pickup_point"));
                                destination_distanceList.add(data.getJSONObject(i).getString("destination_distance"));
                                pickup_timeList.add(data.getJSONObject(i).getString("pickup_time"));
                            }
                            adapter = new StudentTransportAdapter(StudentTransportRoutes.this, pickup_pointList,destination_distanceList,pickup_timeList,pickupname);
                            transportList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.noData), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StudentTransportRoutes.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentTransportRoutes.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



}
