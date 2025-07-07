package com.qdocs.smartschool.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.students.BehaviourComment;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StudentBehaviourReportAdapter extends RecyclerView.Adapter<StudentBehaviourReportAdapter.MyViewHolder> {

    private FragmentActivity context;
    private ArrayList<String> titleList;
    private ArrayList<String> dateList;
    private ArrayList<String> descriptionList;
    private ArrayList<String> idList;
    private ArrayList<String> join_url;
    private ArrayList<String> pointlist;
    private ArrayList<String> staff_namelist;
    private ArrayList<String> arrays;
    private ArrayList<String> role_nameList;
    private ArrayList<String> commentcountList;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    String defaultDatetimeFormat;
    String permission;
    public StudentBehaviourReportAdapter(FragmentActivity studentonlineexam, ArrayList<String> titleList, ArrayList<String> dateList,
                                         ArrayList<String> descriptionList, ArrayList<String> idList, ArrayList<String> join_url, ArrayList<String> pointlist, ArrayList<String> staff_namelist,
                                         ArrayList<String> arrays,ArrayList<String> role_nameList,ArrayList<String> commentcountList) {
        this.context = studentonlineexam;
        this.titleList = titleList;
        this.dateList = dateList;
        this.descriptionList = descriptionList;
        this.idList = idList;
        this.join_url = join_url;
        this.pointlist = pointlist;
        this.staff_namelist = staff_namelist;
        this.arrays = arrays;
        this.role_nameList = role_nameList;
        this.commentcountList = commentcountList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titlename, date, point, description,count,assigned_by,unread_count;
        FrameLayout comments;
        LinearLayout headLayout;

        public MyViewHolder(View view) {
            super(view);
            comments = view.findViewById(R.id.comments);
            titlename = view.findViewById(R.id.titlename);
            headLayout = view.findViewById(R.id.adapter_student_headLayout);
            date = view.findViewById(R.id.date);
            point = view.findViewById(R.id.point);
            count = view.findViewById(R.id.unread_count);
            description = view.findViewById(R.id.description);
            assigned_by = view.findViewById(R.id.assigned_by);
            unread_count = view.findViewById(R.id.unread_count);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_behaviour, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String superadmin_restriction = Utility.getSharedPreferences(context.getApplicationContext(), Constants.superadmin_restriction);
        defaultDatetimeFormat = Utility.getSharedPreferences(context.getApplicationContext(), "datetimeFormat");
        holder.headLayout.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        holder.titlename.setText(titleList.get(position));
        if(commentcountList.get(position).equals("0")){
            holder.unread_count.setVisibility(View.GONE);
        }else{
            holder.unread_count.setVisibility(View.VISIBLE);
            holder.unread_count.setText(commentcountList.get(position));
        }


        holder.date.setText(dateList.get(position));
        holder.point.setText(pointlist.get(position));
        holder.description.setText(descriptionList.get(position));

        if(superadmin_restriction.equals("enabled")){
            holder.assigned_by.setText(staff_namelist.get(position));
        }else{
            if(role_nameList.get(position).equals("Super Admin")){
                holder.assigned_by.setText("");
            }else{
                holder.assigned_by.setText(staff_namelist.get(position));
            }
        }
        System.out.println("Permission Enable 2==="+arrays);


         if(arrays.contains("")){
             holder.comments.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Intent intent=new Intent(context, BehaviourComment.class);
                     intent.putExtra("id",idList.get(position));
                     intent.putExtra("permission","1");
                     context.startActivity(intent);
                 }
             });
         }else{
             if(arrays.contains("student,parent")){
                 permission="2";
                 System.out.println("Permission Enable for both");
             }if(arrays.contains("parent")){
                 permission="1";
                 System.out.println("Permission Enable for parents");
             }if(arrays.contains("student")){
                 permission="0";
                 System.out.println("Permission Enable for student");
             }
             holder.comments.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Intent intent=new Intent(context, BehaviourComment.class);
                     intent.putExtra("id",idList.get(position));
                     intent.putExtra("permission",permission);
                     context.startActivity(intent);
                 }
             });
         }
    }

    @Override
    public int getItemCount() {
        return idList.size();
    }

    private void getDataFromApi (String bodyParams) {
        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(context.getApplicationContext(), "apiUrl")+Constants.gmeethistoryUrl;
        Log.e("gmeethistoryUrl==", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Log.e(" Result", result);
                        JSONObject obj = new JSONObject(result);
                        Toast.makeText(context, ""+obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(context.getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(context.getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(context.getApplicationContext(), "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext()); //Creating a Request Queue
        requestQueue.add(stringRequest);  //Adding request to the queue
    }

}


