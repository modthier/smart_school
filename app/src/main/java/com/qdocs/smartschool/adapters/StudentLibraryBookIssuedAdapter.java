package com.qdocs.smartschool.adapters;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.students.StudentLibraryBookIssued;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import java.util.ArrayList;

public class StudentLibraryBookIssuedAdapter extends RecyclerView.Adapter<StudentLibraryBookIssuedAdapter.MyViewHolder> {

    private StudentLibraryBookIssued context;
    private ArrayList<String> bookNameList;
    private ArrayList<String> authorNameList;
    private ArrayList<String> bookNoList;
    private ArrayList<String> issueDateList;
    private ArrayList<String> returnDateList;
    private ArrayList<String> statusList;
    private ArrayList<String> due_return_dateList;


    public StudentLibraryBookIssuedAdapter(StudentLibraryBookIssued studentLibraryBookIssued, ArrayList<String> bookNameList, ArrayList<String> authorNameList,
                                           ArrayList<String> bookNoList, ArrayList<String> issueDateList, ArrayList<String> returnDateList, ArrayList<String> statusList, ArrayList<String> due_return_dateList) {

        this.context = studentLibraryBookIssued;
        this.bookNameList = bookNameList;
        this.authorNameList = authorNameList;
        this.bookNoList = bookNoList;
        this.issueDateList = issueDateList;
        this.returnDateList = returnDateList;
        this.statusList = statusList;
        this.due_return_dateList = due_return_dateList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bookNameTV, authorNameTV, issuedOnTV, bookNoTV, returnDateTV,duereturnDate,returned_status_layout ;
        LinearLayout nameView;


        public MyViewHolder(View view) {
            super(view);

            nameView = (LinearLayout) view.findViewById(R.id.adapter_studentlibrary_headLayout);
            bookNameTV = (TextView) view.findViewById(R.id.adapter_student_libraryBookIssue_bookNameTV);
            authorNameTV = (TextView) view.findViewById(R.id.adapter_student_libraryBookIssue_authorNameTV);
            issuedOnTV = (TextView) view.findViewById(R.id.adapter_student_libraryBookIssued_issueDateTV);
            bookNoTV = (TextView) view.findViewById(R.id.adapter_student_libraryBookIssued_bookNoTV);
            returnDateTV = (TextView) view.findViewById(R.id.adapter_student_libraryBookIssued_returnDateTV);
            duereturnDate = (TextView) view.findViewById(R.id.adapter_student_libraryBookIssued_duereturnDateTV);
            returned_status_layout = (TextView) view.findViewById(R.id.Adapter_statusTV);


        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_library_book_issued, parent, false);

        return new MyViewHolder(itemView);
    }

    @NonNull
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.bookNameTV.setText(bookNameList.get(position));
        holder.authorNameTV.setText(authorNameList.get(position));
        holder.issuedOnTV.setText(issueDateList.get(position));
        holder.bookNoTV.setText(bookNoList.get(position));
        holder.returnDateTV.setText(returnDateList.get(position));
        holder.duereturnDate.setText(due_return_dateList.get(position));

        if(statusList.get(position).equals("1")) {
            holder.returned_status_layout.setText(context.getApplicationContext().getString(R.string.returned));
            holder.returned_status_layout.setBackgroundResource(R.drawable.green_border);
        } else {
            holder.returned_status_layout.setText(context.getApplicationContext().getString(R.string.notReturned));
            holder.returned_status_layout.setBackgroundResource(R.drawable.red_border);
        }
        //DECORATE
        holder.nameView.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
    }

    @Override
    public int getItemCount() {
        return bookNameList.size();
    }

}
