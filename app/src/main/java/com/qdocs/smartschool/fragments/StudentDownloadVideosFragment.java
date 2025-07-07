package com.qdocs.smartschool.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
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
import com.qdocs.smartschool.adapters.StudentDownloadVideoAdapter;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import static android.widget.Toast.makeText;


public class StudentDownloadVideosFragment extends Fragment {

    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    RecyclerView recyclerview;
    ArrayList<String> idList = new ArrayList<String>();
    ArrayList<String> titleList = new ArrayList<String>();
    ArrayList <String> descriptionList = new ArrayList<String>();
    ArrayList <String> video_linkList = new ArrayList<String>();
    ArrayList <String> created_byList = new ArrayList<String>();
    ArrayList <String> thumb_pathList = new ArrayList<String>();
    ArrayList <String> created_by = new ArrayList<String>();
    StudentDownloadVideoAdapter adapter;
    SwipeRefreshLayout pullToRefresh;
    public StudentDownloadVideosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Utility.isConnectingToInternet(getActivity())){
            params.put("class_id", Utility.getSharedPreferences(getActivity().getApplicationContext(), "classId"));
            params.put("section_id", Utility.getSharedPreferences(getActivity().getApplicationContext(), "sectionId"));
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
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_student_download_videos, container, false);
        pullToRefresh=mainView.findViewById(R.id.pullToRefresh);
        recyclerview = (RecyclerView) mainView.findViewById(R.id.recyclerview);
        adapter = new StudentDownloadVideoAdapter(getActivity(),recyclerview,idList, titleList, descriptionList, video_linkList, thumb_pathList,created_byList,created_by);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(adapter);
        pullToRefresh = mainView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                if(Utility.isConnectingToInternet(getActivity())){
                    params.put("class_id", Utility.getSharedPreferences(getActivity().getApplicationContext(), "classId"));
                    params.put("section_id", Utility.getSharedPreferences(getActivity().getApplicationContext(), "sectionId"));
                    JSONObject obj=new JSONObject(params);
                    Log.e("params ", obj.toString());
                    getDataFromApi(obj.toString());
                }else{
                    makeText(getActivity(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return mainView;

    }

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getActivity().getApplicationContext(), "apiUrl")+ Constants.getVideoTutorialUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                pullToRefresh.setRefreshing(false);
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        System.out.println("Result=="+result);
                        JSONObject object = new JSONObject(result);

                            JSONArray dataArray = object.getJSONArray("result");

                            idList.clear();
                            titleList.clear();
                            descriptionList.clear();
                            video_linkList.clear();
                            created_byList.clear();
                            thumb_pathList.clear();
                            created_by.clear();
                        if(dataArray.length() != 0) {
                            pullToRefresh.setVisibility(View.VISIBLE);
                            for (int i = 0; i < dataArray.length(); i++) {
                                idList.add(dataArray.getJSONObject(i).getString("id"));
                                titleList.add(dataArray.getJSONObject(i).getString("title"));
                                descriptionList.add(dataArray.getJSONObject(i).getString("description"));
                                created_byList.add(dataArray.getJSONObject(i).getString("name") + " " + dataArray.getJSONObject(i).getString("surname") + " (" + dataArray.getJSONObject(i).getString("employee_id") + ")");
                                video_linkList.add(dataArray.getJSONObject(i).getString("video_link"));
                                thumb_pathList.add(dataArray.getJSONObject(i).getString("thumb_path") + dataArray.getJSONObject(i).getString("thumb_name"));
                                created_by.add(dataArray.getJSONObject(i).getString("created_by"));
                            }
                            adapter.notifyDataSetChanged();

                        }else{
                            pullToRefresh.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
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
