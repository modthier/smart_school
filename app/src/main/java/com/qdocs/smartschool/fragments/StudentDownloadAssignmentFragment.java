package com.qdocs.smartschool.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.smartschool.adapters.StudentDownloadsAdapter;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import static android.widget.Toast.makeText;

public class StudentDownloadAssignmentFragment extends Fragment {

    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    ListView listView;
    ArrayList<String> idList = new ArrayList<String>();
    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList <String> sharebyList = new ArrayList<String>();
    ArrayList <String> sharedateList = new ArrayList<String>();
    ArrayList <String> valid_uptoList = new ArrayList<String>();
    ArrayList <String> descriptionList = new ArrayList<String>();
    ArrayList <String> checkdate = new ArrayList<String>();
    ArrayList <String> uploaddateList = new ArrayList<String>();
    ArrayList<String> created_bylist = new ArrayList<String>();
    StudentDownloadsAdapter adapter;

    public StudentDownloadAssignmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Utility.isConnectingToInternet(getActivity())){
            params.put("role",  Utility.getSharedPreferences(getActivity().getApplicationContext(), Constants.loginType));
            params.put("student_id",  Utility.getSharedPreferences(getActivity().getApplicationContext(), "studentId"));
            params.put("classId", Utility.getSharedPreferences(getActivity().getApplicationContext(), "classId"));
            params.put("sectionId", Utility.getSharedPreferences(getActivity().getApplicationContext(), "sectionId"));
            params.put("user_parent_id", "");
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

        View mainView = inflater.inflate(R.layout.fragment_student_download_assignment, container, false);

        listView = (ListView) mainView.findViewById(R.id.fragment_studentDownload_assignment_listview);
        adapter = new StudentDownloadsAdapter(getActivity(), idList, nameList, sharedateList, sharebyList,valid_uptoList,descriptionList,uploaddateList,checkdate,created_bylist);
        listView.setAdapter(adapter);

        return mainView;
    }

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getActivity().getApplicationContext(), "apiUrl")+ Constants.getDownloadsLinksUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        System.out.println("Result=="+result);
                        JSONArray object = new JSONArray(result);
                        idList.clear();
                        nameList.clear();
                        sharebyList.clear();
                        sharedateList.clear();
                        valid_uptoList.clear();
                        uploaddateList.clear();
                        descriptionList.clear();
                        checkdate.clear();
                        created_bylist.clear();
                        Log.e("length", object.length()+"..");
                        for(int i = 0; i < object.length(); i++) {
                            idList.add(object.getJSONObject(i).getString("id"));
                            nameList.add(object.getJSONObject(i).getString("title"));
                            descriptionList.add(object.getJSONObject(i).getString("description"));
                            sharebyList.add(object.getJSONObject(i).getString("name")+" "+object.getJSONObject(i).getString("surname")+" ("+object.getJSONObject(i).getString("employee_id")+")");
                            sharedateList.add(Utility.parseDate("yyyy-MM-dd", Utility.getSharedPreferences(getActivity().getApplicationContext(), "dateFormat"), object.getJSONObject(i).getString("share_date")));
                            valid_uptoList.add(Utility.parseDate("yyyy-MM-dd", Utility.getSharedPreferences(getActivity().getApplicationContext(), "dateFormat"), object.getJSONObject(i).getString("valid_upto")));
                            uploaddateList.add(Utility.parseDate("yyyy-MM-dd hh:mm:ss", Utility.getSharedPreferences(getActivity().getApplicationContext(), "dateFormat"), object.getJSONObject(i).getString("created_at")));
                            checkdate.add(object.getJSONObject(i).getString("valid_upto"));
                            created_bylist.add(object.getJSONObject(i).getString("created_by"));
                        }
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(getActivity(), R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
