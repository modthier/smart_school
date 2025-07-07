package com.qdocs.smartschool.adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
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
import com.qdocs.smartschool.model.Album1;
import com.qdocs.smartschool.students.CbseExaminationActivity;
import com.qdocs.smartschool.students.StudentAttendance;
import com.qdocs.smartschool.students.StudentBehaviourReport;
import com.qdocs.smartschool.students.StudentClassTimetable;
import com.qdocs.smartschool.students.StudentDocuments;
import com.qdocs.smartschool.students.StudentExaminationList;
import com.qdocs.smartschool.students.StudentOnlineCourse;
import com.qdocs.smartschool.students.StudentSyllabusStatus;
import com.qdocs.smartschool.students.StudentTimeline;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AcademicModuleAdapter extends RecyclerView.Adapter<AcademicModuleAdapter.MyViewHolder> {
    private FragmentActivity context;
    private List<Album1> albumList;
    public Map<String, String> aparams = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    View view;
    public AcademicModuleAdapter(FragmentActivity context, List<Album1> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_module_selection, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView modulename;
        LinearLayout layout;
        ImageView moduleiamge;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            modulename = (TextView) itemView.findViewById(R.id.modulename);
            moduleiamge = (ImageView) itemView.findViewById(R.id.moduleiamge);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Utility.setLocale(context, Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
        final Album1 album = albumList.get(position);
        holder.modulename.setText(album.getName());
        if(position==0) {
            if (album.getName().equals("class_timetable")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.timeTable));
            } else if (album.getName().equals("syllabus_status")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.syllabus_status));
            } else if (album.getName().equals("attendance")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.attendance));
            } else if (album.getName().equals("examinations")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.examination));
            } else if (album.getName().equals("student_timeline")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.timeLine));
            } else if (album.getName().equals("mydocuments")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.documents));
            } else if (album.getName().equals("behaviour_records")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.behavioureport));
            }else if(album.getName().equals("cbseexam")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.cbseexamination));
            }
        }else if(position==1) {
            if (album.getName().equals("syllabus_status")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.syllabus_status));
            } else if (album.getName().equals("attendance")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.attendance));
            } else if (album.getName().equals("examinations")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.examination));
            } else if (album.getName().equals("student_timeline")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.timeLine));
            } else if (album.getName().equals("mydocuments")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.documents));
            } else if (album.getName().equals("behaviour_records")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.behavioureport));
            }else if(album.getName().equals("cbseexam")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.cbseexamination));
            }
        }else if(position==2){
            if (album.getName().equals("attendance")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.attendance));
            } else if (album.getName().equals("examinations")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.examination));
            } else if (album.getName().equals("student_timeline")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.timeLine));
            } else if (album.getName().equals("mydocuments")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.documents));
            } else if (album.getName().equals("behaviour_records")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.behavioureport));
            }else if(album.getName().equals("cbseexam")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.cbseexamination));
            }
        }else if(position==3){
            if (album.getName().equals("examinations")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.examination));
            } else if (album.getName().equals("student_timeline")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.timeLine));
            } else if (album.getName().equals("mydocuments")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.documents));
            } else if (album.getName().equals("behaviour_records")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.behavioureport));
            }else if(album.getName().equals("cbseexam")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.cbseexamination));
            }
        }else if(position==4){
            if (album.getName().equals("student_timeline")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.timeLine));
            } else if (album.getName().equals("mydocuments")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.documents));
            } else if (album.getName().equals("behaviour_records")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.behavioureport));
            }else if(album.getName().equals("cbseexam")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.cbseexamination));
            }
        }else if(position==5){
            if (album.getName().equals("mydocuments")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.documents));
            } else if (album.getName().equals("behaviour_records")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.behavioureport));
            }else if(album.getName().equals("cbseexam")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.cbseexamination));
            }
        }else if(position==6) {
            if (album.getName().equals("behaviour_records")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.behavioureport));
            }else if(album.getName().equals("cbseexam")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.cbseexamination));
            }
        }else if(position==7) {
            if(album.getName().equals("cbseexam")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.cbseexamination));
            }
        }

       //  holder.modulename.setText(album.getName());
        if(album.getValue().equals("0")){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }else{
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.itemView.setVisibility(View.VISIBLE);
        }
         Picasso.with(context).load(album.getThumbnail()).fit().centerInside().placeholder(null).into(holder.moduleiamge);
         holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==0){
                        if(album.getName().equals("class_timetable")){
                            Intent classtimetableintent = new Intent(context, StudentClassTimetable.class);
                            context.startActivity(classtimetableintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                            holder.modulename.setText(context.getApplicationContext().getString(R.string.timeTable));
                        }else if(album.getName().equals("syllabus_status")){
                            Intent syllabustatusintent = new Intent(context, StudentSyllabusStatus.class);
                            context.startActivity(syllabustatusintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("attendance")){
                            Intent attendanceintent = new Intent(context, StudentAttendance.class);
                            context.startActivity(attendanceintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("examinations")){
                            Intent examintent = new Intent(context, StudentExaminationList.class);
                            context.startActivity(examintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("student_timeline")){
                            Intent timelineintent = new Intent(context, StudentTimeline.class);
                            context.startActivity(timelineintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("mydocuments")){
                            Intent documentintent = new Intent(context, StudentDocuments.class);
                            context.startActivity(documentintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("behaviour_records")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssbr");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "ssbr");
                        }else if(album.getName().equals("cbseexam")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sscbse");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "sscbse");
                        }

                    }else if(position==1){
                        if(album.getName().equals("syllabus_status")){
                            Intent syllabustatusintent = new Intent(context, StudentSyllabusStatus.class);
                            context.startActivity(syllabustatusintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("attendance")){
                            Intent attendanceintent = new Intent(context, StudentAttendance.class);
                            context.startActivity(attendanceintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("examinations")){
                            Intent examintent = new Intent(context, StudentExaminationList.class);
                            context.startActivity(examintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("student_timeline")){
                            Intent timelineintent = new Intent(context, StudentTimeline.class);
                            context.startActivity(timelineintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("mydocuments")){
                            Intent documentintent = new Intent(context, StudentDocuments.class);
                            context.startActivity(documentintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("behaviour_records")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssbr");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "ssbr");
                        }else if(album.getName().equals("cbseexam")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sscbse");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "sscbse");
                        }


                    }else if(position==2){
                        if(album.getName().equals("attendance")){
                            Intent attendanceintent = new Intent(context, StudentAttendance.class);
                            context.startActivity(attendanceintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("examinations")){
                            Intent examintent = new Intent(context, StudentExaminationList.class);
                            context.startActivity(examintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("student_timeline")){
                            Intent timelineintent = new Intent(context, StudentTimeline.class);
                            context.startActivity(timelineintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("mydocuments")){
                            Intent documentintent = new Intent(context, StudentDocuments.class);
                            context.startActivity(documentintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("behaviour_records")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssbr");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "ssbr");
                        }else if(album.getName().equals("cbseexam")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sscbse");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "sscbse");
                        }


                    }else if(position==3){
                        if(album.getName().equals("examinations")){
                            Intent examintent = new Intent(context, StudentExaminationList.class);
                            context.startActivity(examintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("student_timeline")){
                            Intent timelineintent = new Intent(context, StudentTimeline.class);
                            context.startActivity(timelineintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("mydocuments")){
                            Intent documentintent = new Intent(context, StudentDocuments.class);
                            context.startActivity(documentintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("behaviour_records")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssbr");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "ssbr");
                        }else if(album.getName().equals("cbseexam")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sscbse");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "sscbse");
                        }
                    }else if(position==4){
                        if(album.getName().equals("student_timeline")){
                            Intent timelineintent = new Intent(context, StudentTimeline.class);
                            context.startActivity(timelineintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("mydocuments")){
                            Intent documentintent = new Intent(context, StudentDocuments.class);
                            context.startActivity(documentintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("behaviour_records")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssbr");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "ssbr");
                        }else if(album.getName().equals("cbseexam")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sscbse");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "sscbse");
                        }
                    }else if(position==5){
                        if(album.getName().equals("mydocuments")){
                            Intent documentintent = new Intent(context, StudentDocuments.class);
                            context.startActivity(documentintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("behaviour_records")){
                            Intent behaviourintent = new Intent(context, StudentBehaviourReport.class);
                            context.startActivity(behaviourintent);
                            context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                           /* aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssbr");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "ssbr");*/
                        }else if(album.getName().equals("cbseexam")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sscbse");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "sscbse");
                        }
                    }else if(position==6){

                        if(album.getName().equals("behaviour_records")){
                            Intent behaviourintent = new Intent(context, StudentBehaviourReport.class);
                            context.startActivity(behaviourintent);
                            context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                            /*aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssbr");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "ssbr");*/
                        }else if(album.getName().equals("cbseexam")){
                            Intent cbseintent = new Intent(context, CbseExaminationActivity.class);
                            context.startActivity(cbseintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                            /*aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sscbse");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "sscbse");*/
                        }


                    }else if(position==7){
                        if(album.getName().equals("cbseexam")){
                            Intent cbseintent = new Intent(context, CbseExaminationActivity.class);
                            context.startActivity(cbseintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                           /* aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sscbse");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "sscbse");*/
                        }


                    }
                }
            });
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    private void CheckAddon(String bodyParams, final String type) {

        final String requestBody = bodyParams;
        String url = "https://sstrace.qdocs.in/postlic/verifyaddon";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {

                    try {

                        JSONObject object = new JSONObject(result);
                        if(object.getString("status").equals("1")) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            builder.setCancelable(false);
                            builder.setMessage( R.string.verificationMessage);

                            builder.setTitle("");
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            android.app.AlertDialog alert = builder.create();
                            alert.show();
                        }else{
                            if(type.equals("ssbr")){
                                Intent behaviourintent = new Intent(context, StudentBehaviourReport.class);
                                context.startActivity(behaviourintent);
                                context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                            }else if(type.equals("sscbse")){
                                Intent cbseintent = new Intent(context, CbseExaminationActivity.class);
                                context.startActivity(cbseintent);
                                context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                            }
                        }
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
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
}
