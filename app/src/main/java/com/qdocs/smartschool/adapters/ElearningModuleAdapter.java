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
import com.qdocs.smartschool.students.StudentDailyAssignment;
import com.qdocs.smartschool.students.StudentDownloads;
import com.qdocs.smartschool.students.StudentGmeetLiveClasses;
import com.qdocs.smartschool.students.StudentHomeworkNew;
import com.qdocs.smartschool.students.StudentLiveClasses;
import com.qdocs.smartschool.students.StudentOnlineCourse;
import com.qdocs.smartschool.students.StudentOnlineExam;
import com.qdocs.smartschool.students.StudentSyllabusTimetable;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ElearningModuleAdapter extends RecyclerView.Adapter<ElearningModuleAdapter.MyViewHolder> {
    private FragmentActivity context;
    private List<Album1> albumList;
    ArrayList<String> moduleCodeList=new ArrayList<String>();
    ArrayList<String> moduleStatusList=new ArrayList<String>();
    public Map<String, String> aparams = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    View view;
    public ElearningModuleAdapter(FragmentActivity context, List<Album1> albumList) {
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Utility.setLocale(context, Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
        final Album1 album = albumList.get(position);
         holder.modulename.setText(album.getName());
        if(position==0) {
            if (album.getName().equals("homework")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.homework));
            }else if(album.getName().equals("daily_assignment")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.daily_assignment));
            }else if(album.getName().equals("lesson_plan")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.lessonplan));
            }else if(album.getName().equals("online_examination")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.onlineexam));
            }else if(album.getName().equals("download_center")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.downloadCenter));
            }else if(album.getName().equals("online_course")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.online_course));
            }else if(album.getName().equals("live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.liveclasses));
            }else if(album.getName().equals("gmeet_live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.livegmeetclasses));
            }
        }else if(position==1){
            if(album.getName().equals("daily_assignment")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.daily_assignment));
            }else if(album.getName().equals("lesson_plan")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.lessonplan));
            }else if(album.getName().equals("online_examination")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.onlineexam));
            }else if(album.getName().equals("download_center")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.downloadCenter));
            }else if(album.getName().equals("online_course")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.online_course));
            }else if(album.getName().equals("live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.liveclasses));
            }else if(album.getName().equals("gmeet_live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.livegmeetclasses));
            }
        }else if(position==2){
            if(album.getName().equals("lesson_plan")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.lessonplan));
            }else if(album.getName().equals("online_examination")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.onlineexam));
            }else if(album.getName().equals("download_center")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.downloadCenter));
            }else if(album.getName().equals("online_course")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.online_course));
            }else if(album.getName().equals("live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.liveclasses));
            }else if(album.getName().equals("gmeet_live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.livegmeetclasses));
            }
        }else if(position==3){
            if(album.getName().equals("online_examination")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.onlineexam));
            }else if(album.getName().equals("download_center")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.downloadCenter));
            }else if(album.getName().equals("online_course")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.online_course));
            }else if(album.getName().equals("live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.liveclasses));
            }else if(album.getName().equals("gmeet_live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.livegmeetclasses));
            }
        }else if(position==4){
            if(album.getName().equals("download_center")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.downloadCenter));
            }else if(album.getName().equals("online_course")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.online_course));
            }else if(album.getName().equals("live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.liveclasses));
            }else if(album.getName().equals("gmeet_live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.livegmeetclasses));
            }
        }else if(position==5){
            if(album.getName().equals("online_course")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.online_course));
            }else if(album.getName().equals("live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.liveclasses));
            }else if(album.getName().equals("gmeet_live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.livegmeetclasses));
            }
        }else if(position==6){
            if(album.getName().equals("live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.liveclasses));
            }else if(album.getName().equals("gmeet_live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.livegmeetclasses));
            }
        }else if(position==7){
            if(album.getName().equals("gmeet_live_classes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.livegmeetclasses));
            }
        }
        if(album.getValue().equals("0")){
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            // notifyItemRemoved(position);
        }else{
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.itemView.setVisibility(View.VISIBLE);
        }
        Picasso.with(context).load(album.getThumbnail()).fit().centerInside().placeholder(null).into(holder.moduleiamge);
        System.out.println("moduleCodeList=="+moduleCodeList.toString());

         holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==0){
                        if(album.getName().equals("homework")){

                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent homeworkintent = new Intent(context, StudentHomeworkNew.class);
                            context.startActivity(homeworkintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("daily_assignment")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent assignmentintent = new Intent(context, StudentDailyAssignment.class);
                            context.startActivity(assignmentintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("lesson_plan")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent lessonplanintent = new Intent(context, StudentSyllabusTimetable.class);
                            context.startActivity(lessonplanintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("online_examination")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent onlineexamintent = new Intent(context, StudentOnlineExam.class);
                            context.startActivity(onlineexamintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("download_center")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent downloadintent = new Intent(context, StudentDownloads.class);
                            context.startActivity(downloadintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("online_course")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssoclc");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "ssoclc");
                        }else if(album.getName().equals("live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sszlc");
                            JSONObject zobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", zobj.toString());
                            CheckAddon(zobj.toString(), "sszlc");
                        }else if(album.getName().equals("gmeet_live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssglc");
                            JSONObject gobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", gobj.toString());
                            CheckAddon(gobj.toString(), "ssglc");
                        }

                    }else if(position==1){
                        if(album.getName().equals("daily_assignment")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent assignmentintent = new Intent(context, StudentDailyAssignment.class);
                            context.startActivity(assignmentintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("lesson_plan")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent lessonplanintent = new Intent(context, StudentSyllabusTimetable.class);
                            context.startActivity(lessonplanintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("online_examination")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent onlineexamintent = new Intent(context, StudentOnlineExam.class);
                            context.startActivity(onlineexamintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("download_center")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent downloadintent = new Intent(context, StudentDownloads.class);
                            context.startActivity(downloadintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("online_course")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssoclc");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "ssoclc");
                        }else if(album.getName().equals("live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sszlc");
                            JSONObject zobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", zobj.toString());
                            CheckAddon(zobj.toString(), "sszlc");
                        }else if(album.getName().equals("gmeet_live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssglc");
                            JSONObject gobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", gobj.toString());
                            CheckAddon(gobj.toString(), "ssglc");
                        }
                    }else if(position==2){
                        if(album.getName().equals("lesson_plan")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent lessonplanintent = new Intent(context, StudentSyllabusTimetable.class);
                            context.startActivity(lessonplanintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("online_examination")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent onlineexamintent = new Intent(context, StudentOnlineExam.class);
                            context.startActivity(onlineexamintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("download_center")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent downloadintent = new Intent(context, StudentDownloads.class);
                            context.startActivity(downloadintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("online_course")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssoclc");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "ssoclc");
                        }else if(album.getName().equals("live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sszlc");
                            JSONObject zobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", zobj.toString());
                            CheckAddon(zobj.toString(), "sszlc");
                        }else if(album.getName().equals("gmeet_live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssglc");
                            JSONObject gobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", gobj.toString());
                            CheckAddon(gobj.toString(), "ssglc");
                        }
                    }else if(position==3){

                        if(album.getName().equals("online_examination")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent onlineexamintent = new Intent(context, StudentOnlineExam.class);
                            context.startActivity(onlineexamintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("download_center")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent downloadintent = new Intent(context, StudentDownloads.class);
                            context.startActivity(downloadintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("online_course")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssoclc");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "ssoclc");
                        }else if(album.getName().equals("live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sszlc");
                            JSONObject zobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", zobj.toString());
                            CheckAddon(zobj.toString(), "sszlc");
                        }else if(album.getName().equals("gmeet_live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssglc");
                            JSONObject gobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", gobj.toString());
                            CheckAddon(gobj.toString(), "ssglc");
                        }

                    }else if(position==4){

                        if(album.getName().equals("download_center")){
                            Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                            Intent downloadintent = new Intent(context, StudentDownloads.class);
                            context.startActivity(downloadintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("online_course")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssoclc");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "ssoclc");
                        }else if(album.getName().equals("live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sszlc");
                            JSONObject zobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", zobj.toString());
                            CheckAddon(zobj.toString(), "sszlc");
                        }else if(album.getName().equals("gmeet_live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssglc");
                            JSONObject gobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", gobj.toString());
                            CheckAddon(gobj.toString(), "ssglc");
                        }


                    }else if(position==5){

                        if(album.getName().equals("online_course")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssoclc");
                            JSONObject ocobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", ocobj.toString());
                            CheckAddon(ocobj.toString(), "ssoclc");
                        }else if(album.getName().equals("live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sszlc");
                            JSONObject zobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", zobj.toString());
                            CheckAddon(zobj.toString(), "sszlc");
                        }else if(album.getName().equals("gmeet_live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssglc");
                            JSONObject gobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", gobj.toString());
                            CheckAddon(gobj.toString(), "ssglc");
                        }

                    }else if(position==6){

                        if(album.getName().equals("live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "sszlc");
                            JSONObject zobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", zobj.toString());
                            CheckAddon(zobj.toString(), "sszlc");
                        }else if(album.getName().equals("gmeet_live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssglc");
                            JSONObject gobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", gobj.toString());
                            CheckAddon(gobj.toString(), "ssglc");
                        }

                    }else if(position==7){

                        if(album.getName().equals("gmeet_live_classes")){
                            aparams.put("site_url", Utility.getSharedPreferences(context.getApplicationContext(), Constants.imagesUrl));
                            aparams.put("addontype", "ssglc");
                            JSONObject gobj = new JSONObject(aparams);
                            Log.e("CheckAddon params", gobj.toString());
                            CheckAddon(gobj.toString(), "ssglc");
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
                                if(type.equals("sszlc")){
                                    Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                                    Intent liveclasses = new Intent(context, StudentLiveClasses.class);
                                    context.startActivity(liveclasses);
                                    context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                                }else if(type.equals("ssoclc")){
                                    Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                                    Intent online_course = new Intent(context, StudentOnlineCourse.class);
                                    context.startActivity(online_course);
                                    context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
                                }else if(type.equals("ssglc")){
                                    Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
                                    Intent gmeetliveclasses = new Intent(context, StudentGmeetLiveClasses.class);
                                    context.startActivity(gmeetliveclasses);
                                    context.overridePendingTransition(R.anim.slide_leftright,  R.anim.no_animation);
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

    public  void setMenu(){

    }



}
