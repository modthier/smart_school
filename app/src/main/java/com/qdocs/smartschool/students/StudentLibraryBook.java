package com.qdocs.smartschool.students;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.adapters.StudentLibraryBookAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import static android.widget.Toast.makeText;

public class StudentLibraryBook extends BaseActivity {
    RecyclerView bookListView;
    ArrayList<String> bookidList = new ArrayList<String>();
    ArrayList<String> bookNameList = new ArrayList<String>();
    ArrayList<String> authorNameList = new ArrayList<String>();
    ArrayList<String> subjectNameList = new ArrayList<String>();
    ArrayList<String> publisherNameList = new ArrayList<String>();
    ArrayList<String> rackNoList = new ArrayList<String>();
    ArrayList<String> quantityList = new ArrayList<String>();
    ArrayList<String> priceList = new ArrayList<String>();
    ArrayList<String> postDate = new ArrayList<String>();
    StudentLibraryBookAdapter adapter;
    SwipeRefreshLayout pullToRefresh;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    LinearLayout nodata,data_layout;
    CardView card_view_outer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_library_book_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        titleTV.setText(getApplicationContext().getString(R.string.library));
        card_view_outer = findViewById(R.id.card_view_outer);
        card_view_outer.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        nodata = (LinearLayout) findViewById(R.id.nodata);
        data_layout = (LinearLayout) findViewById(R.id.data_layout);
        bookListView = (RecyclerView) findViewById(R.id.student_libraryBook_listview);
        if(Utility.isConnectingToInternet(getApplicationContext())){
            getDataFromApi();
        }else{
            makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }

        adapter = new StudentLibraryBookAdapter(StudentLibraryBook.this, bookNameList, authorNameList, subjectNameList,
                publisherNameList, rackNoList, quantityList, priceList, postDate);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        bookListView.setLayoutManager(mLayoutManager);
        bookListView.setItemAnimator(new DefaultItemAnimator());
        bookListView.setAdapter(adapter);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                if(Utility.isConnectingToInternet(getApplicationContext())){
                    getDataFromApi();
                }else{
                    makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getDataFromApi () {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getLibraryBookListUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                pullToRefresh.setRefreshing(false);
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);
                        System.out.println("Result==="+result);

                        bookidList.clear();
                        bookNameList.clear();
                        authorNameList.clear();
                        subjectNameList.clear();
                        publisherNameList.clear();
                        rackNoList.clear();
                        quantityList.clear();
                        priceList.clear();
                        postDate.clear();

                            JSONArray dataArray = object.getJSONArray("data");
                        if (dataArray.length() != 0) {
                            nodata.setVisibility(View.GONE);
                            data_layout.setVisibility(View.VISIBLE);
                            for (int i = 0; i < dataArray.length(); i++) {

                                bookidList.add(dataArray.getJSONObject(i).getString("id"));
                                bookNameList.add(dataArray.getJSONObject(i).getString("book_title"));
                                authorNameList.add(dataArray.getJSONObject(i).getString("author"));
                                subjectNameList.add(dataArray.getJSONObject(i).getString("subject"));
                                publisherNameList.add(dataArray.getJSONObject(i).getString("publish"));
                                rackNoList.add(dataArray.getJSONObject(i).getString("rack_no"));
                                quantityList.add(dataArray.getJSONObject(i).getString("qty"));

                                priceList.add(currency + Utility.changeAmount(dataArray.getJSONObject(i).getString("perunitcost"),currency,currency_price));
                                if (dataArray.getJSONObject(i).getString("postdate").equals("0000-00-00")) {
                                    postDate.add("");
                                } else {
                                    postDate.add(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dataArray.getJSONObject(i).getString("postdate")));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            nodata.setVisibility(View.VISIBLE);
                            data_layout.setVisibility(View.GONE);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    pullToRefresh.setVisibility(View.GONE);

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Log.e("Volley Error", volleyError.toString());
                        Toast.makeText(StudentLibraryBook.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(StudentLibraryBook.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
