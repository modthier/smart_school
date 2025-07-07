package com.qdocs.smartschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.utils.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class CourseReviewAdapterNew extends RecyclerView.Adapter<CourseReviewAdapterNew.MyViewHolder>  {
    private Context context;
     ArrayList<String> reviewidList ;
     ArrayList<String> reviewList ;
     ArrayList<String> dateList ;
     ArrayList<String> ratingList ;
     ArrayList<String> nameList ;
     ArrayList<String> imageList ;
    private Fragment fragment;

    public CourseReviewAdapterNew(Context context, ArrayList<String> reviewidList, ArrayList<String> reviewList, ArrayList<String> ratingList, ArrayList<String> dateList, ArrayList<String> nameList, ArrayList<String> imageList) {
        this.context = context;
        this.ratingList = ratingList;
        this.reviewidList = reviewidList;
        this.reviewList = reviewList;
        this.dateList = dateList;
        this.nameList = nameList;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public CourseReviewAdapterNew.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_review, parent, false);
        return new CourseReviewAdapterNew.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseReviewAdapterNew.MyViewHolder holder, int position) {
        holder.name.setText(nameList.get(position));
        holder.date.setText(dateList.get(position));
        holder.review.setText(reviewList.get(position));
        holder.rating.setRating(Float.parseFloat(ratingList.get(position)));
        String imgUrl = Utility.getSharedPreferences(context.getApplicationContext(), "imagesUrl")+imageList.get(position);
        Picasso.with(context).load(imgUrl).placeholder(R.drawable.placeholder_user).memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE).into(holder.createdimage);
    }

    @Override
    public int getItemCount() {
        return reviewidList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,date,review;
        RatingBar rating;
        ImageView createdimage;
        public MyViewHolder(@NonNull View view) {
            super(view);
            review = view.findViewById(R.id.review);
            date = view.findViewById(R.id.date);
            name = view.findViewById(R.id.name);
            createdimage = view.findViewById(R.id.createdimage);
            rating = view.findViewById(R.id.rating);
        }
    }
}
