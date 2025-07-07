package com.qdocs.smartschool.adapters;


import static android.widget.Toast.makeText;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class BehaviourCommentAdapter extends RecyclerView.Adapter<BehaviourCommentAdapter.MyViewHolder> {
    private FragmentActivity context;
    StudentTeacherDetailsAdapter adapter;
    private ArrayList<String> namelist;
    private ArrayList<String> datelist;
    private ArrayList<String> messagelist;
    private ArrayList<String> student_imagelist;
    private ArrayList<String> typelist;
    private ArrayList<String> idlist;
    private Map<String, String> deleteTaskParams = new Hashtable<String, String>();
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();

    public BehaviourCommentAdapter(FragmentActivity studentClassTimetable, ArrayList<String> namelist, ArrayList<String> datelist, ArrayList<String> messagelist,
                                   ArrayList<String> student_imagelist, ArrayList<String> typelist, ArrayList<String> idlist) {
        this.context = studentClassTimetable;
        this.namelist = namelist;
        this.datelist = datelist;
        this.messagelist = messagelist;
        this.student_imagelist = student_imagelist;
        this.typelist = typelist;
        this.idlist = idlist;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, date,comment,delete;
        ImageView createdimage;
        public MyViewHolder(View view) {
            super(view);
            createdimage = (ImageView) view.findViewById(R.id.createdimage);
            name = (TextView) view.findViewById(R.id.name);
            date = (TextView) view.findViewById(R.id.date);
            comment = (TextView) view.findViewById(R.id.comment);
            delete = (TextView) view.findViewById(R.id.delete);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commentlist, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       holder.name.setText(namelist.get(position));
       holder.date.setText(datelist.get(position));
       holder.comment.setText(messagelist.get(position));
       if(typelist.get(position).equals("student")){
           holder.delete.setVisibility(View.VISIBLE);
           holder.delete.setTextColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.primaryColour)));
           holder.delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                   builder.setCancelable(false);
                   builder.setMessage(R.string.deleteMsg);
                   builder.setTitle("");
                   builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                       public void onClick(DialogInterface dialog, int which) {
                           if (Utility.isConnectingToInternet(context.getApplicationContext())) {
                               deleteTaskParams.put("incident_comment_id", idlist.get(position));
                               JSONObject obj=new JSONObject(deleteTaskParams);
                               Log.e("params ", obj.toString());
                               System.out.println("delete="+obj.toString());
                               deleteTaskApi(obj.toString());
                           } else {
                               makeText(context.getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                           }

                       }
                   });
                   builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                       }
                   });
                   android.app.AlertDialog alert = builder.create();
                   alert.show();

               }
           });
       }else{
           holder.delete.setVisibility(View.GONE);
       }

       String imgUrl = student_imagelist.get(position);
       Picasso.with(context).load(imgUrl).placeholder(R.drawable.placeholder_user).memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE).into(holder.createdimage);
       System.out.println("imgUrl=="+imgUrl);

    }



    @Override
    public int getItemCount(){
        return idlist.size();
    }

    private void deleteTaskApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(context.getApplicationContext(), "apiUrl") + Constants.deleteincidentcommentsUrl;
        Log.e("URL", url);
        System.out.println("URL"+url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        String success = object.getString("result");
                       /* Snackbar snackbar = Snackbar.make(taskListView, object.getString("msg"),Snackbar.LENGTH_SHORT);
                        snackbar.show();*/

                        if (success.equals("Success")) {
                            Toast.makeText(context.getApplicationContext(),R.string.successdelete, Toast.LENGTH_SHORT).show();
                            context.finish();
                            context.startActivity(context.getIntent());
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
                Toast.makeText(context, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(context.getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(context.getApplicationContext(), "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(context); //Creating a Request Queue
        requestQueue.add(stringRequest);//Adding request to the queue
    }
}

