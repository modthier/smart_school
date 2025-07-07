package com.qdocs.smartschool.adapters;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qdocs.smartschool.students.Payment;
import com.qdocs.smartschool.students.StudentFees;
import com.qdocs.smartschool.students.StudentOfflinePayment;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import com.qdocs.smartschool.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class StudentFeesAdapter extends RecyclerView.Adapter<StudentFeesAdapter.MyViewHolder> {
    private StudentFees context;
    private ArrayList<String> feesIdList;
    private ArrayList<String> feesCodeList;
    private ArrayList<String> dueDateList;
    private ArrayList<String> amtList;
    private ArrayList<String> balanceAmtList;
    private ArrayList<String> paidAmtList;
    private ArrayList<String> depositId;
    private ArrayList<String> feesSessionIdList;
    private ArrayList<String> statusList;
    private ArrayList<String> feesDetails;
    private ArrayList<String> feesTypeId;
    private ArrayList<String> feesCatList;
    private ArrayList<String> discountNameList;
    private ArrayList<String> discountStatusList;
    private ArrayList<String> discountAmtList;
    private ArrayList<String> discountpayment_idList;
    private ArrayList<String> discAmtList;
    private ArrayList<String> feesnameList;
    private ArrayList<String> fineAmtList;
    private ArrayList<String> amtfineList;
    private ArrayList<String> transportdueDateList;
    private ArrayList<String> transportamtfineList;
    private ArrayList<String> transportpaidAmtList;
    private ArrayList<String> transportdiscAmtList;
    private ArrayList<String> transportbalanceAmtList;
    private ArrayList<String> transportfeesDepositIdList;

    private ArrayList<String> transportstatusList;
    private ArrayList<String> transportamtList;
    private ArrayList<String> transportfineAmtList;
    String is_offline_fee_payment;

    public StudentFeesAdapter(StudentFees activity, ArrayList<String> feesIdList, ArrayList<String> feesnameList, ArrayList<String> feesCodeList,
                              ArrayList<String> dueDateList, ArrayList<String> amtList, ArrayList<String> paidAmtList,
                              ArrayList<String> balanceAmtList, ArrayList<String> feesDepositIdList, ArrayList<String> feesSessionIdList, ArrayList<String> statusList,
                              ArrayList<String> feesDetails, ArrayList<String> feesTypeId, ArrayList<String> feesCatList,
                              ArrayList<String> discountNameList, ArrayList<String> discountAmtList,
                              ArrayList<String> discountStatusList, ArrayList<String> discountpayment_idList, ArrayList<String> discAmtList,
                              ArrayList<String> fineAmtList, ArrayList<String> amtfineList, ArrayList<String> transportdueDateList, ArrayList<String> transportamtfineList,
                              ArrayList<String> transportpaidAmtList, ArrayList<String> transportdiscAmtList, ArrayList<String> transportbalanceAmtList,
                              ArrayList<String> transportfeesDepositIdList, ArrayList<String> transportamtList, ArrayList<String> transportfineAmtList, ArrayList<String> transportstatusList) {

        this.context = activity;
        this.feesIdList = feesIdList;
        this.feesnameList = feesnameList;
        this.feesCodeList = feesCodeList;
        this.dueDateList = dueDateList;
        this.amtList = amtList;
        this.balanceAmtList = balanceAmtList;
        this.paidAmtList = paidAmtList;
        this.depositId = feesDepositIdList;
        this.feesSessionIdList = feesSessionIdList;
        this.statusList = statusList;
        this.feesDetails = feesDetails;
        this.feesTypeId = feesTypeId;
        this.feesCatList = feesCatList;
        this.transportstatusList = transportstatusList;
        this.discountNameList = discountNameList;
        this.discountStatusList = discountStatusList;
        this.discountAmtList = discountAmtList;
        this.discountpayment_idList = discountpayment_idList;
        this.fineAmtList = fineAmtList;
        this.discAmtList = discAmtList;
        this.amtfineList = amtfineList;
        this.transportdueDateList = transportdueDateList;
        this.transportamtfineList = transportamtfineList;
        this.transportpaidAmtList = transportpaidAmtList;
        this.transportdiscAmtList = transportdiscAmtList;
        this.transportbalanceAmtList = transportbalanceAmtList;
        this.transportfeesDepositIdList = transportfeesDepositIdList;
        this.transportfineAmtList = transportfineAmtList;
        this.transportamtList = transportamtList;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView feecodeTV, feesDueDateTV, feesAmtTV, feesPaidAmtTV, feesDueAmtTV, feesStatusTV, payBtn,discountpayment_id,
                discountTV,fineTV,feefineTV,transportfeeDueDateTV,transportfeeAmtTV,transportfeefineTV,transportfineTV,transportdiscountTV,transportfeePaidAmtTV,transportfeeDueAmtTV,transportfeesAdapter_payBtn;
        private LinearLayout viewContainer, viewBtn, feesLay, discountLay,feescode_layout,transportfeesLay;
        private RelativeLayout header;
        private TextView discountNameTV, discountAmtTV,feesnameTV;

        public MyViewHolder(View rowView) {
            super(rowView);

            viewContainer = rowView.findViewById(R.id.studentFeesAdapter);
            feescode_layout = rowView.findViewById(R.id.feescode_layout);
            feesnameTV = rowView.findViewById(R.id.studentFeesAdapter_feenameTV);
            feecodeTV = rowView.findViewById(R.id.studentFeesAdapter_feecodeTV);
            feesDueDateTV = rowView.findViewById(R.id.studentFeesAdapter_feeDueDateTV);
            feesAmtTV = rowView.findViewById(R.id.studentFeesAdapter_feeAmtTV);
            feesPaidAmtTV = rowView.findViewById(R.id.studentFeesAdapter_feePaidAmtTV);
            discountTV = rowView.findViewById(R.id.studentFeesAdapter_discountTV);
            fineTV = rowView.findViewById(R.id.studentFeesAdapter_fineTV);
            feesDueAmtTV = rowView.findViewById(R.id.studentFeesAdapter_feeDueAmtTV);
            feesStatusTV = rowView.findViewById(R.id.feesAdapter_statusTV);
            header = rowView.findViewById(R.id.feesAdapter_nameHeader);
            viewBtn = rowView.findViewById(R.id.studentFeesAdapter_viewBtn);
            payBtn = rowView.findViewById(R.id.feesAdapter_payBtn);
            discountpayment_id = rowView.findViewById(R.id.studentFeesAdapter_discountpayment_idTV);
            feesLay = rowView.findViewById(R.id.studentFeesAdapter_feesLay);
            transportfeesLay = rowView.findViewById(R.id.studentFeesAdapter_transportfeesLay);
            discountLay = rowView.findViewById(R.id.studentFeesAdapter_discountLay);
            discountAmtTV = rowView.findViewById(R.id.studentFeesAdapter_discountAmtTV);
            feefineTV = rowView.findViewById(R.id.studentFeesAdapter_feefineTV);
            transportfeeDueDateTV = rowView.findViewById(R.id.transportfeeDueDateTV);
            transportfeeAmtTV = rowView.findViewById(R.id.transportfeeAmtTV);
            transportfeefineTV = rowView.findViewById(R.id.transportfeefineTV);
            transportfineTV = rowView.findViewById(R.id.transportfineTV);
            transportdiscountTV = rowView.findViewById(R.id.transportdiscountTV);
            transportfeePaidAmtTV = rowView.findViewById(R.id.transportfeePaidAmtTV);
            transportfeeDueAmtTV = rowView.findViewById(R.id.transportfeeDueAmtTV);
            transportfeesAdapter_payBtn = rowView.findViewById(R.id.transportfeesAdapter_payBtn);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_student_fees, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
        //FEES VIEW
        if(feesCatList.get(position).equals("fees")) {
            viewHolder.feesLay.setVisibility(View.VISIBLE);
            viewHolder.feescode_layout.setVisibility(View.VISIBLE);
            viewHolder.transportfeesLay.setVisibility(View.GONE);
            viewHolder.discountLay.setVisibility(View.GONE);
            showFeesCard(viewHolder, position);
        } else if(feesCatList.get(position).equals("transport")) {
            viewHolder.feesLay.setVisibility(View.GONE);
            viewHolder.feescode_layout.setVisibility(View.VISIBLE);
            viewHolder.transportfeesLay.setVisibility(View.VISIBLE);
            viewHolder.discountLay.setVisibility(View.GONE);
            showTransportFeesCard(viewHolder, position);
        } else {
            viewHolder.feesLay.setVisibility(View.GONE);
            viewHolder.discountLay.setVisibility(View.VISIBLE);
            viewHolder.feescode_layout.setVisibility(View.GONE);
            viewHolder.transportfeesLay.setVisibility(View.GONE);
            viewHolder.viewBtn.setVisibility(View.GONE);
            showDiscountCard(viewHolder, position);
        }
        Log.e("payBtn", Utility.getSharedPreferencesBoolean(context, Constants.showPaymentBtn)+"..");
        viewHolder.viewContainer.setOnClickListener(null);
    }

    private void showDiscountCard(MyViewHolder viewHolder, final int position) {
        viewHolder.feesnameTV.setText(context.getString(R.string.paymentDiscount)+"-"+discountNameList.get(position));
        String discountMsg = context.getString(R.string.discountMsg) +" ";

        discountMsg += discountAmtList.get(position) + " " + discountStatusList.get(position) /*+" - " + discountpayment_idList.get(position)*/;
        viewHolder.discountAmtTV.setText(discountMsg);
    }

    private void showFeesCard(MyViewHolder viewHolder, final int position) {
        viewHolder.feesnameTV.setText(feesnameList.get(position));
        viewHolder.feecodeTV.setText(feesCodeList.get(position));
        String defaultDateFormat = Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat");
        if(dueDateList.get(position).equals("0000-00-00")){
            viewHolder.feesDueDateTV.setText("No Due Date");
        }else{
            viewHolder.feesDueDateTV.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dueDateList.get(position)));
        }
        viewHolder.feesAmtTV.setText(amtList.get(position));
        viewHolder.feefineTV.setText(amtfineList.get(position));
        viewHolder.fineTV.setText(fineAmtList.get(position));
        viewHolder.discountTV.setText(discAmtList.get(position));
        viewHolder.feesPaidAmtTV.setText(paidAmtList.get(position));
        viewHolder.feesDueAmtTV.setText(balanceAmtList.get(position));
    //    viewHolder.feesStatusTV.setText(statusList.get(position));

        if(statusList.get(position).equals("Paid")) {
            viewHolder.feesStatusTV.setText(context.getApplicationContext().getString(R.string.paid));
            viewHolder.feesStatusTV.setBackgroundResource(R.drawable.green_border);
            viewHolder.feesDueDateTV.setBackgroundResource(R.color.transparent);
//            viewHolder.payBtn.setVisibility(View.GONE);
            viewHolder.viewBtn.setVisibility(View.VISIBLE);
        } if (statusList.get(position).equals("Unpaid")) {
            viewHolder.feesStatusTV.setText(context.getApplicationContext().getString(R.string.unpaid));
            viewHolder.feesStatusTV.setBackgroundResource(R.drawable.red_border); viewHolder.feesDueDateTV.setBackgroundResource(R.color.transparent);

//            viewHolder.payBtn.setVisibility(View.VISIBLE);
            viewHolder.viewBtn.setVisibility(View.GONE);
            if(dueDateList.get(position).equals("0000-00-00")){
                viewHolder.feesDueDateTV.setText("No Due Date");
                viewHolder.feesDueDateTV.setBackgroundResource(R.color.transparent);
            }else{
                viewHolder.feesDueDateTV.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dueDateList.get(position)));
                if(checkDueDate(dueDateList.get(position))) {
                    viewHolder.feesDueDateTV.setBackgroundResource(R.drawable.red_border);

                    float scale = context.getResources().getDisplayMetrics().density;
                    int dpAsPixels = (int) (5*scale + 0.5f);

                    viewHolder.feesDueDateTV.setPadding(dpAsPixels, 0, dpAsPixels, 0);
                    viewHolder.feesDueDateTV.setTextColor(Color.WHITE);

                } else {
                    viewHolder.feesDueDateTV.setBackgroundResource(R.color.transparent);
                }
            }

        } if (statusList.get(position).equals("Partial")) {
            viewHolder.feesStatusTV.setText(context.getApplicationContext().getString(R.string.partial));
            viewHolder.feesStatusTV.setBackgroundResource(R.drawable.yellow_border);
//          viewHolder.payBtn.setVisibility(View.VISIBLE);
            viewHolder.viewBtn.setVisibility(View.VISIBLE);
            if(checkDueDate(dueDateList.get(position))) {
                if(dueDateList.get(position).equals("0000-00-00")){
                    viewHolder.feesDueDateTV.setText(context.getApplicationContext().getString(R.string.noduedate));
                    viewHolder.feesDueDateTV.setBackgroundResource(R.color.transparent);
                }else{
                    viewHolder.feesDueDateTV.setBackgroundResource(R.drawable.red_border);
                }

                float scale = context.getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (5*scale + 0.5f);

                viewHolder.feesDueDateTV.setPadding(dpAsPixels, 0, dpAsPixels, 0);
                viewHolder.feesDueDateTV.setTextColor(Color.WHITE);

            } else {
                viewHolder.feesDueDateTV.setBackgroundResource(R.color.transparent);
            }
        }

        if(Utility.getSharedPreferencesBoolean(context, Constants.showPaymentBtn))  {
            Log.e("testing", "testing 1");
            viewHolder.payBtn.setVisibility(View.VISIBLE);

            if(statusList.get(position).equals("Paid")) {
                viewHolder.payBtn.setVisibility(View.GONE);
            } if (statusList.get(position).equals("Unpaid")) {
                viewHolder.payBtn.setVisibility(View.VISIBLE);
            } if (statusList.get(position).equals("Partial")) {
                viewHolder.payBtn.setVisibility(View.VISIBLE);
            }
        } else {
            Log.e("testing", "testing 2");
            viewHolder.payBtn.setVisibility(View.GONE);
        }

        //DECORATE
        viewHolder.header.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        viewHolder.payBtn.setText(Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency) + " " + context.getApplicationContext().getString(R.string.pay));
        //DECORATE

        viewHolder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickview) {

                ArrayList<String> paymentId = new ArrayList<>();
                ArrayList<String> paymentDate = new ArrayList<>();
                ArrayList<String> paymentDiscount = new ArrayList<>();
                ArrayList<String> paymentFine = new ArrayList<>();
                ArrayList<String> paymentPaid = new ArrayList<>();
                ArrayList<String> paymentNote = new ArrayList<>();


                JSONObject feesDetailsJson = new JSONObject();

                try {
                    feesDetailsJson = new JSONObject(feesDetails.get(position));

                    Iterator<String> iter = feesDetailsJson.keys();
                    while(iter.hasNext()){
                        String key = iter.next();

                        JSONObject object1 = feesDetailsJson.getJSONObject(key);
                        String paymentmode=object1.getString("payment_mode");
                        if(paymentmode.equals("upi")){
                            paymentmode=context.getApplicationContext().getString(R.string.upi);
                        }else if(paymentmode.equals("bank_transfer")){
                            paymentmode=context.getApplicationContext().getString(R.string.banktransfer);
                        }else if(paymentmode.equals("bank_payment")){
                            paymentmode=context.getApplicationContext().getString(R.string.bankpayment);
                        }else if(paymentmode.equals("Cash")){
                            paymentmode=context.getApplicationContext().getString(R.string.cash);
                        }else{
                            paymentmode=object1.getString("payment_mode");
                        }

                        paymentId.add(depositId.get(position) + "/" + object1.getString("inv_no") + "(" + paymentmode + ")" );
                        paymentDate.add(Utility.parseDate("yyyy-MM-dd", Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat"), object1.getString("date")));
                        paymentDiscount.add(object1.getString("amount_discount"));
                        paymentFine.add(object1.getString("amount_fine"));
                        paymentPaid.add(object1.getString("amount"));
                        paymentNote.add(object1.getString("description"));
                    }
                } catch (JSONException je) {
                    Log.e("Error Parseing Data", je.toString());
                }

                View view = context.getLayoutInflater().inflate(R.layout.fragment_fees_bottom_sheet, null);
                view.setMinimumHeight(500);

                ImageView crossBtn = view.findViewById(R.id.fees_bottomSheet_crossBtn);
                TextView header = view.findViewById(R.id.fees_bottomSheet_header);
                RecyclerView hostelListView = view.findViewById(R.id.fees_bottomSheet_listview);

                header.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));

                StudentFeesDetailAdapter adapter = new StudentFeesDetailAdapter(context, paymentId, paymentDate, paymentDiscount, paymentFine, paymentPaid, paymentNote);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context.getApplicationContext());
                hostelListView.setLayoutManager(mLayoutManager);
                hostelListView.setItemAnimator(new DefaultItemAnimator());
                hostelListView.setAdapter(adapter);
                final BottomSheetDialog dialog = new BottomSheetDialog(context);

                dialog.setContentView(view);
                dialog.show();

                crossBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        is_offline_fee_payment=Utility.getSharedPreferences(context.getApplicationContext(), "is_offline_fee_payment");
        if(is_offline_fee_payment.equals("1")){
            viewHolder.payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPaymentChooser(feesIdList.get(position),feesTypeId.get(position),feesSessionIdList.get(position),"fees","");
                }
            });
        }else{

            viewHolder.payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent asd = new Intent(context, Payment.class);
                    asd.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    asd.putExtra("feesId", feesIdList.get(position));
                    asd.putExtra("feesTypeId",feesTypeId.get(position));
                    asd.putExtra("paymenttype","fees");
                    asd.putExtra("transfeesIdList","");
                    context.startActivity(asd);
                }
            });

        }

    }

    private void showTransportFeesCard(MyViewHolder viewHolder, final int position) {
        viewHolder.feesnameTV.setText("Transport Fees");
        viewHolder.feecodeTV.setText(feesCodeList.get(position));
        String defaultDateFormat = Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat");
        if(transportdueDateList.get(position).equals("0000-00-00")){
            viewHolder.transportfeeDueDateTV.setText("No Due Date");
        }else{
            viewHolder.transportfeeDueDateTV.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, transportdueDateList.get(position)));
        }
        viewHolder.transportfeeAmtTV.setText(transportamtList.get(position));
        if(transportamtfineList.get(position).equals("+.00")){
            viewHolder.transportfeefineTV.setText("");
        }else{
            viewHolder.transportfeefineTV.setText(transportamtfineList.get(position));
        }
        viewHolder.transportfineTV.setText(transportfineAmtList.get(position));
        viewHolder.transportdiscountTV.setText(transportdiscAmtList.get(position));
        viewHolder.transportfeePaidAmtTV.setText(transportpaidAmtList.get(position));
        viewHolder.transportfeeDueAmtTV.setText(transportbalanceAmtList.get(position));
        viewHolder.feesStatusTV.setText(transportstatusList.get(position));

        if(transportstatusList.get(position).equals("Paid")) {
            viewHolder.feesStatusTV.setText(context.getApplicationContext().getString(R.string.paid));
            viewHolder.feesStatusTV.setBackgroundResource(R.drawable.green_border);
            viewHolder.transportfeeDueDateTV.setBackgroundResource(R.color.transparent);
            viewHolder.payBtn.setVisibility(View.GONE);
            viewHolder.viewBtn.setVisibility(View.VISIBLE);
        } if (transportstatusList.get(position).equals("Unpaid")) {
            viewHolder.feesStatusTV.setText(context.getApplicationContext().getString(R.string.unpaid));
            viewHolder.feesStatusTV.setBackgroundResource(R.drawable.red_border);
            viewHolder.transportfeeDueDateTV.setBackgroundResource(R.color.transparent);
            viewHolder.payBtn.setVisibility(View.VISIBLE);
            viewHolder.viewBtn.setVisibility(View.GONE);
            if(transportdueDateList.get(position).equals("0000-00-00")){
                viewHolder.transportfeeDueDateTV.setText("No Due Date");
                viewHolder.transportfeeDueDateTV.setBackgroundResource(R.color.transparent);
            }else{
                viewHolder.transportfeeDueDateTV.setText(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, transportdueDateList.get(position)));
                if(checkDueDate(transportdueDateList.get(position))) {
                    viewHolder.transportfeeDueDateTV.setBackgroundResource(R.drawable.red_border);
                    float scale = context.getResources().getDisplayMetrics().density;
                    int dpAsPixels = (int) (5*scale + 0.5f);
                    viewHolder.transportfeeDueDateTV.setPadding(dpAsPixels, 0, dpAsPixels, 0);
                    viewHolder.transportfeeDueDateTV.setTextColor(Color.WHITE);
                } else {
                    viewHolder.transportfeeDueDateTV.setBackgroundResource(R.color.transparent);
                }
            }
        } if (transportstatusList.get(position).equals("Partial")) {
            viewHolder.feesStatusTV.setText(context.getApplicationContext().getString(R.string.partial));
            viewHolder.feesStatusTV.setBackgroundResource(R.drawable.yellow_border);
//            viewHolder.payBtn.setVisibility(View.VISIBLE);
            viewHolder.viewBtn.setVisibility(View.VISIBLE);
            if(checkDueDate(transportdueDateList.get(position))) {
                if(transportdueDateList.get(position).equals("0000-00-00")){
                    viewHolder.transportfeeDueDateTV.setText(context.getApplicationContext().getString(R.string.noduedate));
                    viewHolder.transportfeeDueDateTV.setBackgroundResource(R.color.transparent);
                }else{
                    viewHolder.transportfeeDueDateTV.setBackgroundResource(R.drawable.red_border);
                }
                float scale = context.getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (5*scale + 0.5f);

                viewHolder.transportfeeDueDateTV.setPadding(dpAsPixels, 0, dpAsPixels, 0);
                viewHolder.transportfeeDueDateTV.setTextColor(Color.WHITE);
            } else {
                viewHolder.transportfeeDueDateTV.setBackgroundResource(R.color.transparent);
            }
        }

        if(Utility.getSharedPreferencesBoolean(context, Constants.showPaymentBtn))  {
            Log.e("testing", "testing 1");
            viewHolder.transportfeesAdapter_payBtn.setVisibility(View.VISIBLE);

            if(transportstatusList.get(position).equals("Paid")) {
                viewHolder.transportfeesAdapter_payBtn.setVisibility(View.GONE);
            } if (transportstatusList.get(position).equals("Unpaid")) {
                viewHolder.transportfeesAdapter_payBtn.setVisibility(View.VISIBLE);
            } if (transportstatusList.get(position).equals("Partial")) {
                viewHolder.transportfeesAdapter_payBtn.setVisibility(View.VISIBLE);
            }
        } else {
            Log.e("testing", "testing 2");
            viewHolder.transportfeesAdapter_payBtn.setVisibility(View.GONE);
        }

        //DECORATE
        viewHolder.header.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        viewHolder.transportfeesAdapter_payBtn.setText(Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency) + " " + context.getApplicationContext().getString(R.string.pay));
        //DECORATE
        is_offline_fee_payment=Utility.getSharedPreferences(context.getApplicationContext(), "is_offline_fee_payment");
        if(is_offline_fee_payment.equals("1")){
            viewHolder.transportfeesAdapter_payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPaymentChooser("","",feesSessionIdList.get(position),"transport_fees",feesIdList.get(position));
                }
            });
        }else{
            viewHolder.transportfeesAdapter_payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent asd = new Intent(context, Payment.class);
                    asd.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    asd.putExtra("feesId", "");
                    asd.putExtra("feesTypeId","");
                    asd.putExtra("paymenttype","transport_fees");
                    asd.putExtra("transfeesIdList",feesIdList.get(position));
                    context.startActivity(asd);
                }
            });
        }

        viewHolder.viewBtn.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View clickview) {

                ArrayList<String> paymentId = new ArrayList<>();
                ArrayList<String> paymentDate = new ArrayList<>();
                ArrayList<String> paymentDiscount = new ArrayList<>();
                ArrayList<String> paymentFine = new ArrayList<>();
                ArrayList<String> paymentPaid = new ArrayList<>();
                ArrayList<String> paymentNote = new ArrayList<>();

                JSONObject feesDetailsJson = new JSONObject();

                try {
                    feesDetailsJson = new JSONObject(feesDetails.get(position));

                    feesDetailsJson.length();

                    for (int k = 1; k<=feesDetailsJson.length(); k++) {

                        JSONObject fee = feesDetailsJson.getJSONObject(k+"");
                        String paymentmode=fee.getString("payment_mode");
                        if(paymentmode.equals("upi")){
                            paymentmode=context.getApplicationContext().getString(R.string.upi);
                        }else if(paymentmode.equals("bank_transfer")){
                            paymentmode=context.getApplicationContext().getString(R.string.banktransfer);
                        }else if(paymentmode.equals("bank_payment")){
                            paymentmode=context.getApplicationContext().getString(R.string.bankpayment);
                        }else if(paymentmode.equals("Cash")){
                            paymentmode=context.getApplicationContext().getString(R.string.cash);
                        }else{
                            paymentmode=fee.getString("payment_mode");
                        }

                        paymentId.add(transportfeesDepositIdList.get(position) + "/" + fee.getString("inv_no") + "(" + paymentmode + ")" );
                        paymentDate.add(Utility.parseDate("yyyy-MM-dd", Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat"), fee.getString("date")));
                        paymentDiscount.add(fee.getString("amount_discount"));
                        paymentFine.add(fee.getString("amount_fine"));
                        paymentPaid.add(fee.getString("amount"));
                        paymentNote.add(fee.getString("description"));
                    }
                } catch (JSONException je) {
                    Log.e("Error Parseing Data", je.toString());
                }

                View view = context.getLayoutInflater().inflate(R.layout.fragment_fees_bottom_sheet, null);
                view.setMinimumHeight(500);

                ImageView crossBtn = view.findViewById(R.id.fees_bottomSheet_crossBtn);
                TextView header = view.findViewById(R.id.fees_bottomSheet_header);
                RecyclerView hostelListView = view.findViewById(R.id.fees_bottomSheet_listview);

                header.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));

                StudentFeesDetailAdapter adapter = new StudentFeesDetailAdapter(context, paymentId, paymentDate, paymentDiscount, paymentFine, paymentPaid, paymentNote);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context.getApplicationContext());
                hostelListView.setLayoutManager(mLayoutManager);
                hostelListView.setItemAnimator(new DefaultItemAnimator());
                hostelListView.setAdapter(adapter);
                final BottomSheetDialog dialog = new BottomSheetDialog(context);

                dialog.setContentView(view);
                dialog.show();

                crossBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private void showPaymentChooser(final String feesIdList, final String feesTypeId, final String feesSessionId, final String paymenttype, final String transfeesIdList) {
        final Dialog dialog = new Dialog(context);
        Utility.setLocale(context.getApplicationContext(), Utility.getSharedPreferences(context.getApplicationContext(), Constants.langCode));
        dialog.setContentView(R.layout.choose_payment_mode);
        dialog.setCanceledOnTouchOutside(false);
        RelativeLayout headerLay = (RelativeLayout) dialog.findViewById(R.id.addTask_dialog_header);
        final LinearLayout onlinePayment = (LinearLayout) dialog.findViewById(R.id.onlinePayment);
        final LinearLayout offlinePayment = (LinearLayout) dialog.findViewById(R.id.offlinePayment);
        ImageView closeBtn = (ImageView) dialog.findViewById(R.id.addTask_dialog_crossIcon);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        onlinePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent asd = new Intent(context, Payment.class);
                asd.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                asd.putExtra("feesId", feesIdList);
                asd.putExtra("feesTypeId",feesTypeId);
                asd.putExtra("paymenttype",paymenttype);
                asd.putExtra("transfeesIdList",transfeesIdList);
                context.startActivity(asd);
                dialog.dismiss();
            }
        });
        offlinePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("feesId= "+feesIdList+"feesTypeId= "+feesTypeId+"feesSessionId= "+feesSessionId+"paymenttype= "+paymenttype+"transfeesId="+transfeesIdList);
                Intent asd = new Intent(context, StudentOfflinePayment.class);
                asd.putExtra("feesId", feesIdList);
                asd.putExtra("feesTypeId",feesTypeId);
                asd.putExtra("feesSessionId",feesSessionId);
                asd.putExtra("paymenttype",paymenttype);
                asd.putExtra("transfeesIdList",transfeesIdList);
                context.startActivity(asd);
                dialog.dismiss();
            }
        });

        headerLay.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.primaryColour)));
        dialog.show();
    }


    private boolean checkDueDate(String dueDateStr) {
        try {
            Date todayDate = new Date();
            Date dueDate =new SimpleDateFormat("yyyy-MM-dd").parse(dueDateStr);

            if(todayDate.after(dueDate)) {
                return true;
            } else {
                return  false;
            }

        } catch (ParseException e) {
            Log.e("Parse Exp", e.toString());
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return feesIdList.size();
    }
}
