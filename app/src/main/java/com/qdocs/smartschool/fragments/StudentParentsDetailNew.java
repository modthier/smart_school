package com.qdocs.smartschool.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.makeText;

@SuppressLint("ValidFragment")
public class StudentParentsDetailNew extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    CardView mother_layout,father_layout,guardian_layout;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    TextView father_name,father_contact,father_occupation,mother_name,mother_contact,mother_occupation,guardian_name,guardian_contact,guardian_occupation,guardian_Relation,guardian_email,guardian_address;
    public String defaultDateFormat, currency;
    ImageView fatherImage,motherImage,guardianImage;
    LinearLayout fathername_layout,fathercontact_layout,fatheroccup_layout;
    LinearLayout mothername_layout,mothercontact_layout,motheroccup_layout;
    LinearLayout guardianName_layout,guardiancontact_layout,guardiaoccup_layout,guardianrelation_layout,guardianemail_layout,guardianaddress_layout ;
    @SuppressLint("ValidFragment")
    public StudentParentsDetailNew() {
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
            makeText(getActivity(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_student_profile_parents_new, container, false);
        defaultDateFormat = Utility.getSharedPreferences(getActivity(), "dateFormat");
        currency = Utility.getSharedPreferences(getActivity(), Constants.currency);

        father_layout=mainView.findViewById(R.id.father_layout);
        fathername_layout=mainView.findViewById(R.id.fathername_layout);
        fathercontact_layout=mainView.findViewById(R.id.fathercontact_layout);
        fatheroccup_layout=mainView.findViewById(R.id.fatheroccup_layout);
        father_name=mainView.findViewById(R.id.student_profile_parent_fatherName);
        father_contact=mainView.findViewById(R.id.student_profile_parent_fatherContact);
        father_occupation=mainView.findViewById(R.id.student_profile_parent_fatherOccupation);
        fatherImage=mainView.findViewById(R.id.student_profile_parent_fatherImage);

        mother_layout=mainView.findViewById(R.id.mother_layout);
        mothername_layout=mainView.findViewById(R.id.mothername_layout);
        mothercontact_layout=mainView.findViewById(R.id.mothercontact_layout);
        motheroccup_layout=mainView.findViewById(R.id.motheroccup_layout);
        mother_name=mainView.findViewById(R.id.student_profile_parent_motherName);
        mother_contact=mainView.findViewById(R.id.student_profile_parent_motherContact);
        mother_occupation=mainView.findViewById(R.id.student_profile_parent_motherOccupation);
        motherImage=mainView.findViewById(R.id.student_profile_parent_motherImage);

        guardian_layout=mainView.findViewById(R.id.guardian_layout);
        guardianName_layout=mainView.findViewById(R.id.guardianName_layout);
        guardiancontact_layout=mainView.findViewById(R.id.guardiancontact_layout);
        guardiaoccup_layout=mainView.findViewById(R.id.guardiaoccup_layout);
        guardianrelation_layout=mainView.findViewById(R.id.guardianrelation_layout);
        guardianemail_layout=mainView.findViewById(R.id.guardianemail_layout);
        guardianaddress_layout=mainView.findViewById(R.id.guardianaddress_layout);
        guardian_name=mainView.findViewById(R.id.student_profile_parent_guardianName);
        guardian_contact=mainView.findViewById(R.id.student_profile_parent_guardianContact);
        guardian_occupation=mainView.findViewById(R.id.student_profile_parent_guardianOccupation);
        guardian_Relation=mainView.findViewById(R.id.student_profile_parent_guardianRelation);
        guardian_email=mainView.findViewById(R.id.student_profile_parent_guardianEmail);
        guardian_address=mainView.findViewById(R.id.student_profile_parent_guardianAddress);
        guardianImage=mainView.findViewById(R.id.student_profile_parent_guardianImage);
        loadData();
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

                if (result != null) {
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        final JSONObject dataArray = obj.getJSONObject("student_result");
                        String defaultDateFormat = Utility.getSharedPreferences(getActivity().getApplicationContext(), "dateFormat");

                        father_name.setText(dataArray.getString("father_name"));
                        father_contact.setText(dataArray.getString("father_phone"));
                        father_contact.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View arg0) {
                                String father_phone = null;
                                try {
                                    father_phone = dataArray.getString("father_phone");
                                    System.out.println("PHONE=="+father_phone);
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", father_phone, null));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        father_occupation.setText(dataArray.getString("father_occupation"));

                        mother_name.setText(dataArray.getString("mother_name"));
                        mother_contact.setText(dataArray.getString("mother_phone"));
                        mother_contact.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View arg0) {
                                String mother_phone = null;
                                try {
                                    mother_phone = dataArray.getString("mother_phone");
                                    System.out.println("PHONE=="+mother_phone);
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mother_phone, null));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        mother_occupation.setText(dataArray.getString("mother_occupation"));
                        guardian_name.setText(dataArray.getString("guardian_name"));
                        guardian_contact.setText(dataArray.getString("guardian_phone"));
                        guardian_contact.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View arg0) {
                                String guardian_phone = null;
                                try {
                                    guardian_phone = dataArray.getString("guardian_phone");
                                    System.out.println("PHONE=="+guardian_phone);
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", guardian_phone, null));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        guardian_occupation.setText(dataArray.getString("guardian_occupation"));
                        guardian_Relation.setText(dataArray.getString("guardian_relation"));
                        guardian_email.setText(dataArray.getString("guardian_email"));
                        guardian_address.setText(dataArray.getString("guardian_address"));

                        String father_Image = Utility.getSharedPreferences(getActivity().getApplicationContext(), "imagesUrl")+ dataArray.getString("father_pic");
                        String mother_Image = Utility.getSharedPreferences(getActivity().getApplicationContext(), "imagesUrl") + dataArray.getString("mother_pic");
                        String guardian_Image = Utility.getSharedPreferences(getActivity().getApplicationContext(), "imagesUrl")+dataArray.getString("guardian_pic");

                        System.out.println("Father Image"+ father_Image);
                        System.out.println("Mother Image"+mother_Image);
                        System.out.println("Guardian Image"+ guardian_Image);
                        if(dataArray.getString("guardian_relation").equals("Father")){
                            Picasso.with(getActivity()).load(guardian_Image).placeholder(R.drawable.default_image).memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE).into(guardianImage);
                        }else if(dataArray.getString("guardian_relation").equals("Mother")){
                            Picasso.with(getActivity()).load(guardian_Image).placeholder(R.drawable.default_female).memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE).into(guardianImage);
                        }else{
                            Picasso.with(getActivity()).load(guardian_Image).placeholder(R.drawable.placeholder_user).memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE).into(guardianImage);
                        }

                        Picasso.with(getActivity()).load(father_Image).placeholder(R.drawable.default_image).memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE).into(fatherImage);
                        Picasso.with(getActivity()).load(mother_Image).placeholder(R.drawable.default_female).memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE).into(motherImage);


                        JSONObject fieldsArray = obj.getJSONObject("student_fields");
                        System.out.println("fieldsArray=="+fieldsArray);
                        if(!fieldsArray.has("father_name")){
                            fathername_layout.setVisibility(View.GONE);
                        } if(!fieldsArray.has("father_phone")){
                            fathercontact_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("father_occupation")){
                            fatheroccup_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("father_pic")) {
                            fatherImage.setVisibility(View.GONE);
                        }if(!fieldsArray.has("mother_name")){
                            mothername_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("mother_phone")){
                            mothercontact_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("mother_occupation")){
                            motheroccup_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("mother_pic")) {
                            motherImage.setVisibility(View.GONE);
                        }if(!fieldsArray.has("guardian_name")) {
                            guardianName_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("guardian_phone")) {
                            guardiancontact_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("guardian_occupation")) {
                            guardiaoccup_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("guardian_relation")){
                            guardianrelation_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("guardian_email")){
                            guardianemail_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("guardian_address")) {
                            guardianaddress_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("guardian_pic")) {
                            guardianImage.setVisibility(View.GONE);
                        }if(!fieldsArray.has("father_name") && !fieldsArray.has("father_phone") && !fieldsArray.has("father_pic") && !fieldsArray.has("father_occupation")){
                            father_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("mother_name") && !fieldsArray.has("mother_phone") && !fieldsArray.has("mother_pic") && !fieldsArray.has("mother_occupation")){
                            mother_layout.setVisibility(View.GONE);
                        }if(!fieldsArray.has("guardian_name") && !fieldsArray.has("guardian_phone")
                                && !fieldsArray.has("guardian_occupation") && !fieldsArray.has("guardian_relation")
                                && !fieldsArray.has("guardian_email")&& !fieldsArray.has("guardian_address")&& !fieldsArray.has("guardian_pic")){
                            guardian_layout.setVisibility(View.GONE);
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