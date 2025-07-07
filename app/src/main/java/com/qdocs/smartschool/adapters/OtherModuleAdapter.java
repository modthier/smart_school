package com.qdocs.smartschool.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.model.Album1;
import com.qdocs.smartschool.students.StudentAppyLeave;
import com.qdocs.smartschool.students.StudentFees;
import com.qdocs.smartschool.students.StudentHostel;
import com.qdocs.smartschool.students.StudentLibraryBookIssued;
import com.qdocs.smartschool.students.StudentTasks;
import com.qdocs.smartschool.students.StudentTeachersList;
import com.qdocs.smartschool.students.StudentTransportRoutes;
import com.qdocs.smartschool.students.StudentVisitorBook;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.squareup.picasso.Picasso;
import java.util.List;

public class OtherModuleAdapter extends RecyclerView.Adapter<OtherModuleAdapter.MyViewHolder> {
    private FragmentActivity context;
    private List<Album1> albumList;

    View view;
    public OtherModuleAdapter(FragmentActivity context, List<Album1> albumList) {
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
        LinearLayout layout,view;
        ImageView moduleiamge;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            modulename = (TextView) itemView.findViewById(R.id.modulename);
            moduleiamge = (ImageView) itemView.findViewById(R.id.moduleiamge);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            view = (LinearLayout) itemView.findViewById(R.id.view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Utility.setLocale(context, Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
        final Album1 album = albumList.get(position);
         holder.modulename.setText(album.getName());
        if(position==0) {
            if (album.getName().equals("fees")) {
                holder.modulename.setText(context.getApplicationContext().getString(R.string.fees));
            }else if(album.getName().equals("apply_leave")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.applyleave));
            }else if(album.getName().equals("visitor_book")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.visitorbook));
            }else if(album.getName().equals("transport_routes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.transportRoute));
            }else if(album.getName().equals("hostel_rooms")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.hostelRoom));
            }else if(album.getName().equals("calendar_to_do_list")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.todolist));
            }else if(album.getName().equals("library")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.library));
            }else if(album.getName().equals("teachers_rating")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.teachers));
            }
        }else if(position==1){
            if(album.getName().equals("apply_leave")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.applyleave));
            }else if(album.getName().equals("visitor_book")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.visitorbook));
            }else if(album.getName().equals("transport_routes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.transportRoute));
            }else if(album.getName().equals("hostel_rooms")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.hostelRoom));
            }else if(album.getName().equals("calendar_to_do_list")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.todolist));
            }else if(album.getName().equals("library")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.library));
            }else if(album.getName().equals("teachers_rating")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.teachers));
            }
        }else if(position==2){
            if(album.getName().equals("visitor_book")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.visitorbook));
            }else if(album.getName().equals("transport_routes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.transportRoute));
            }else if(album.getName().equals("hostel_rooms")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.hostelRoom));
            }else if(album.getName().equals("calendar_to_do_list")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.todolist));
            }else if(album.getName().equals("library")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.library));
            }else if(album.getName().equals("teachers_rating")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.teachers));
            }
        }else if(position==3){
            if(album.getName().equals("transport_routes")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.transportRoute));
            }else if(album.getName().equals("hostel_rooms")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.hostelRoom));
            }else if(album.getName().equals("calendar_to_do_list")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.todolist));
            }else if(album.getName().equals("library")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.library));
            }else if(album.getName().equals("teachers_rating")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.teachers));
            }
        }else if(position==4){
            if(album.getName().equals("hostel_rooms")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.hostelRoom));
            }else if(album.getName().equals("calendar_to_do_list")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.todolist));
            }else if(album.getName().equals("library")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.library));
            }else if(album.getName().equals("teachers_rating")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.teachers));
            }
        }else if(position==5){
            if(album.getName().equals("calendar_to_do_list")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.todolist));
            }else if(album.getName().equals("library")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.library));
            }else if(album.getName().equals("teachers_rating")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.teachers));
            }
        }else if(position==6){
            if(album.getName().equals("library")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.library));
            }else if(album.getName().equals("teachers_rating")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.teachers));
            }
        }else if(position==7){
            if(album.getName().equals("teachers_rating")){
                holder.modulename.setText(context.getApplicationContext().getString(R.string.teachers));
            }
        }
        if(album.getValue().equals("0")){
            holder.layout.setVisibility(View.GONE);
            holder.view.setVisibility(View.VISIBLE);
        }else{
            holder.layout.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.GONE);
        }
         Picasso.with(context).load(album.getThumbnail()).fit().centerInside().placeholder(null).into(holder.moduleiamge);
         holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==0){
                        if(album.getName().equals("fees")){
                            Intent feeslistintent = new Intent(context, StudentFees.class);
                            context.startActivity(feeslistintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("apply_leave")){
                            Intent leaveintent = new Intent(context, StudentAppyLeave.class);
                            context.startActivity(leaveintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("visitor_book")){
                            Intent visitorintent = new Intent(context, StudentVisitorBook.class);
                            context.startActivity(visitorintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("transport_routes")){
                            Intent transportintent = new Intent(context, StudentTransportRoutes.class);
                            context.startActivity(transportintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("hostel_rooms")){
                            Intent hostelintent = new Intent(context, StudentHostel.class);
                            context.startActivity(hostelintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("calendar_to_do_list")){
                            Intent taskintent = new Intent(context, StudentTasks.class);
                            context.startActivity(taskintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("library")){
                            Intent libraryintent = new Intent(context, StudentLibraryBookIssued.class);
                            context.startActivity(libraryintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("teachers_rating")){
                            Intent teacherlistintent = new Intent(context, StudentTeachersList.class);
                            context.startActivity(teacherlistintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }

                    }else if(position==1){

                        if(album.getName().equals("apply_leave")){
                            Intent leaveintent = new Intent(context, StudentAppyLeave.class);
                            context.startActivity(leaveintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("visitor_book")){
                            Intent visitorintent = new Intent(context, StudentVisitorBook.class);
                            context.startActivity(visitorintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("transport_routes")){
                            Intent transportintent = new Intent(context, StudentTransportRoutes.class);
                            context.startActivity(transportintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("hostel_rooms")){
                            Intent hostelintent = new Intent(context, StudentHostel.class);
                            context.startActivity(hostelintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("calendar_to_do_list")){
                            Intent taskintent = new Intent(context, StudentTasks.class);
                            context.startActivity(taskintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("library")){
                            Intent libraryintent = new Intent(context, StudentLibraryBookIssued.class);
                            context.startActivity(libraryintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("teachers_rating")){
                            Intent teacherlistintent = new Intent(context, StudentTeachersList.class);
                            context.startActivity(teacherlistintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }
                    }else if(position==2){

                        if(album.getName().equals("visitor_book")){
                            Intent visitorintent = new Intent(context, StudentVisitorBook.class);
                            context.startActivity(visitorintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("transport_routes")){
                            Intent transportintent = new Intent(context, StudentTransportRoutes.class);
                            context.startActivity(transportintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("hostel_rooms")){
                            Intent hostelintent = new Intent(context, StudentHostel.class);
                            context.startActivity(hostelintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("calendar_to_do_list")){
                            Intent taskintent = new Intent(context, StudentTasks.class);
                            context.startActivity(taskintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("library")){
                            Intent libraryintent = new Intent(context, StudentLibraryBookIssued.class);
                            context.startActivity(libraryintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("teachers_rating")){
                            Intent teacherlistintent = new Intent(context, StudentTeachersList.class);
                            context.startActivity(teacherlistintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }


                    }else if(position==3){

                        if(album.getName().equals("transport_routes")){
                            Intent transportintent = new Intent(context, StudentTransportRoutes.class);
                            context.startActivity(transportintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("hostel_rooms")){
                            Intent hostelintent = new Intent(context, StudentHostel.class);
                            context.startActivity(hostelintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("calendar_to_do_list")){
                            Intent taskintent = new Intent(context, StudentTasks.class);
                            context.startActivity(taskintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("library")){
                            Intent libraryintent = new Intent(context, StudentLibraryBookIssued.class);
                            context.startActivity(libraryintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("teachers_rating")){
                            Intent teacherlistintent = new Intent(context, StudentTeachersList.class);
                            context.startActivity(teacherlistintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }

                    }else if(position==4){

                        if(album.getName().equals("hostel_rooms")){
                            Intent hostelintent = new Intent(context, StudentHostel.class);
                            context.startActivity(hostelintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("calendar_to_do_list")){
                            Intent taskintent = new Intent(context, StudentTasks.class);
                            context.startActivity(taskintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("library")){
                            Intent libraryintent = new Intent(context, StudentLibraryBookIssued.class);
                            context.startActivity(libraryintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("teachers_rating")){
                            Intent teacherlistintent = new Intent(context, StudentTeachersList.class);
                            context.startActivity(teacherlistintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }


                    }else if(position==5){

                        if(album.getName().equals("calendar_to_do_list")){
                            Intent taskintent = new Intent(context, StudentTasks.class);
                            context.startActivity(taskintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("library")){
                            Intent libraryintent = new Intent(context, StudentLibraryBookIssued.class);
                            context.startActivity(libraryintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("teachers_rating")){
                            Intent teacherlistintent = new Intent(context, StudentTeachersList.class);
                            context.startActivity(teacherlistintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }
                    }else if(position==6){

                        if(album.getName().equals("library")){
                            Intent libraryintent = new Intent(context, StudentLibraryBookIssued.class);
                            context.startActivity(libraryintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }else if(album.getName().equals("teachers_rating")){
                            Intent teacherlistintent = new Intent(context, StudentTeachersList.class);
                            context.startActivity(teacherlistintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                        }

                    }else if(position==7){

                        if(album.getName().equals("teachers_rating")){
                            Intent teacherlistintent = new Intent(context, StudentTeachersList.class);
                            context.startActivity(teacherlistintent);
                            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
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


}
