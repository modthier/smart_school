package com.qdocs.smartschool.adapters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.qdocs.smartschool.students.StudentAppyLeave;
import com.qdocs.smartschool.students.StudentTasks;
import com.qdocs.smartschool.students.StudentTasksEdit;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.makeText;

public class StudentTaskAdapter extends RecyclerView.Adapter<StudentTaskAdapter.MyViewHolder> {

    private FragmentActivity context;
    private ArrayList<String> taskIdList;
    private ArrayList<String> taskTitleList;
    private ArrayList<String> taskStatusList;
    private ArrayList<String> taskDateList;
    private Map<String, String> deleteTaskParams = new Hashtable<String, String>();
    private Map<String, String> updateTaskParams = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();
    RecyclerView taskListView;
    public Map<String, String> createTaskParams = new Hashtable<String, String>();
    String startweek;
    private boolean isDateSelected = false;

    public StudentTaskAdapter(FragmentActivity studentTasks,RecyclerView taskListView, ArrayList<String> taskIdList,
                              ArrayList<String> taskTitleList, ArrayList<String> taskStatusList, ArrayList<String> taskDateList) {

        this.context = studentTasks;
        this.taskIdList = taskIdList;
        this.taskTitleList = taskTitleList;
        this.taskStatusList = taskStatusList;
        this.taskDateList = taskDateList;
        this.taskListView = taskListView;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView taskTV, taskDateTV;
        CheckBox taskCheckbox;
        RelativeLayout header;
        ImageView edit;

        public MyViewHolder(View view) {
            super(view);
            taskTV = view.findViewById(R.id.adapter_student_task_TaskNameTV);
            taskCheckbox = view.findViewById(R.id.adapter_student_task_checkbox);
            header = view.findViewById(R.id.adapter_student_task_header);
            taskDateTV = view.findViewById(R.id.adapter_student_task_TaskDateTV);
            edit = view.findViewById(R.id.edit);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_tasks, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //DECORATE
        holder.header.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        //setCheckBoxColor(holder.taskCheckbox, R.color.colorAccent, Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.primaryColour)));
       String defaultDateFormat = Utility.getSharedPreferences(context, "dateFormat");
        holder.taskTV.setText(taskTitleList.get(position));
        holder.taskDateTV.setText(Utility.parseDate("yyyy-MM-dd HH:mm:ss", defaultDateFormat,taskDateList.get(position)));
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, StudentTasksEdit.class);
                intent.putExtra("title",taskTitleList.get(position));
                intent.putExtra("date",taskDateList.get(position));
                intent.putExtra("id",taskIdList.get(position));
                context.startActivity(intent);
            }
        });

        if(taskStatusList.get(position).equals("yes")) {
            holder.taskCheckbox.setChecked(true);
            holder.taskTV.setPaintFlags(holder.taskTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.taskCheckbox.setChecked(false);
        }

        holder.header.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                MenuItem delete = contextMenu.add(0, position, 0, "Delete");
                delete.setOnMenuItemClickListener(onDeleteMenu);
            }

        });

        holder.taskCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkedStatus) {

                updateTaskParams.put("task_id", taskIdList.get(position));
                if(checkedStatus) {
                    updateTaskParams.put("status", "yes");
                } else {
                    updateTaskParams.put("status", "no");
                }
                if (Utility.isConnectingToInternet(context.getApplicationContext())) {
                    JSONObject obj=new JSONObject(updateTaskParams);
                    Log.e("change status params ", obj.toString());
                    changeStatusApi(obj.toString());
                } else {
                    makeText(context.getApplicationContext(),R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private final MenuItem.OnMenuItemClickListener onDeleteMenu = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(final MenuItem item) {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setMessage(R.string.deleteMsg);
            builder.setTitle("");
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Log.e("itemId", item.getItemId()+"..");
                    if (Utility.isConnectingToInternet(context.getApplicationContext())) {
                        deleteTaskParams.put("task_id", taskIdList.get(item.getItemId()));
                        JSONObject obj=new JSONObject(deleteTaskParams);
                        Log.e("params ", obj.toString());
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



            return true;
        }
    };

    @Override
    public int getItemCount() {
        return taskIdList.size();
    }


    private void createTaskApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(context, "apiUrl")+Constants.createTaskUrl;
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

                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_LONG).show();

                        if(status.equals("1")) {
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
                headers.put("User-ID", Utility.getSharedPreferences(context, "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(context, "accessToken"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);//Creating a Request Queue
        requestQueue.add(stringRequest);//Adding request to the queue
    }

    private void deleteTaskApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(context.getApplicationContext(), "apiUrl") + Constants.deleteTaskUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        String success = object.getString("status");
                        Snackbar snackbar = Snackbar.make(taskListView, object.getString("msg"),Snackbar.LENGTH_SHORT);
                        snackbar.show();
                      //  Toast.makeText(context.getApplicationContext(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (success.equals("1")) {
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

    private void changeStatusApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(context.getApplicationContext(), "apiUrl") + Constants.markTaskUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);
                        String success = object.getString("status");
                      //  Toast.makeText(context.getApplicationContext(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (success.equals("1")) {
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
