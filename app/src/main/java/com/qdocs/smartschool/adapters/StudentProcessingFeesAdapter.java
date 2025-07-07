package com.qdocs.smartschool.adapters;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.students.Payment;
import com.qdocs.smartschool.students.StudentOfflinePayment;
import com.qdocs.smartschool.students.StudentProcessingFees;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class StudentProcessingFeesAdapter extends RecyclerView.Adapter<StudentProcessingFeesAdapter.MyViewHolder> {
    private StudentProcessingFees context;
    private ArrayList<String> feesIdList;
    private ArrayList<String> feesCodeList;
    private ArrayList<String> dueDateList;
    private ArrayList<String> depositId;
    private ArrayList<String> feesSessionIdList;
    private ArrayList<String> statusList;
    private ArrayList<String> feesDetails;
    private ArrayList<String> feesTypeId;
    private ArrayList<String> feesCatList;
    private ArrayList<String> feesnameList;
    String is_offline_fee_payment;

    public StudentProcessingFeesAdapter(StudentProcessingFees activity, ArrayList<String> feesIdList, ArrayList<String> feesnameList, ArrayList<String> feesCodeList,
                                        ArrayList<String> dueDateList, ArrayList<String> feesDepositIdList, ArrayList<String> feesSessionIdList, ArrayList<String> statusList,
                                        ArrayList<String> feesDetails, ArrayList<String> feesTypeId, ArrayList<String> feesCatList) {

        this.context = activity;
        this.feesIdList = feesIdList;
        this.feesnameList = feesnameList;
        this.feesCodeList = feesCodeList;
        this.dueDateList = dueDateList;
        this.depositId = feesDepositIdList;
        this.feesSessionIdList = feesSessionIdList;
        this.statusList = statusList;
        this.feesDetails = feesDetails;
        this.feesTypeId = feesTypeId;
        this.feesCatList = feesCatList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView feecodeTV, paymentidTV, modeTV, dateTV, feesStatusTV, payBtn,discountpayment_id,descriptionTV,
                paidAmtTV,discountTV,fineTV,feefineTV,totalfeesTV,balanceamtTV,transportfeeDueDateTV,transportfeeAmtTV,transportfeefineTV,transportfineTV,transportdiscountTV,transportfeePaidAmtTV,transportfeeDueAmtTV,transportfeesAdapter_payBtn;
        private LinearLayout viewContainer, viewBtn, feesLay, discountLay,feescode_layout,transportfeesLay;
        private RelativeLayout header;
        private TextView discountNameTV, discountAmtTV,feesnameTV;

        public MyViewHolder(View rowView) {
            super(rowView);

            viewContainer = rowView.findViewById(R.id.studentFeesAdapter);
            feescode_layout = rowView.findViewById(R.id.feescode_layout);
            feesnameTV = rowView.findViewById(R.id.studentFeesAdapter_feenameTV);
            feecodeTV = rowView.findViewById(R.id.studentFeesAdapter_feecodeTV);

            paymentidTV = rowView.findViewById(R.id.studentFeesAdapter_paymentidTV);
             modeTV = rowView.findViewById(R.id.studentFeesAdapter_modeTV);
             dateTV = rowView.findViewById(R.id.studentFeesAdapter_dateTV);
            discountTV = rowView.findViewById(R.id.studentFeesAdapter_discountTV);
            fineTV = rowView.findViewById(R.id.studentFeesAdapter_fineTV);
            paidAmtTV = rowView.findViewById(R.id.studentFeesAdapter_paidamtTV);
            totalfeesTV = rowView.findViewById(R.id.studentFeesAdapter_totalfeesTV);
            feefineTV = rowView.findViewById(R.id.studentFeesAdapter_feefineTV);
            balanceamtTV = rowView.findViewById(R.id.studentFeesAdapter_balanceamtTV);
            descriptionTV = rowView.findViewById(R.id.studentFeesAdapter_descriptionTV);

            feesStatusTV = rowView.findViewById(R.id.feesAdapter_statusTV);
            header = rowView.findViewById(R.id.feesAdapter_nameHeader);

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
                .inflate(R.layout.adapter_processfees, parent, false);
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
        }else if(feesCatList.get(position).equals("transport")) {
            viewHolder.feesLay.setVisibility(View.VISIBLE);
            viewHolder.feescode_layout.setVisibility(View.VISIBLE);
            viewHolder.transportfeesLay.setVisibility(View.GONE);
            viewHolder.discountLay.setVisibility(View.GONE);
            showFeesCard(viewHolder, position);
        }
        Log.e("payBtn", Utility.getSharedPreferencesBoolean(context, Constants.showPaymentBtn)+"..");
        viewHolder.viewContainer.setOnClickListener(null);
    }



    private void showFeesCard(MyViewHolder viewHolder, final int position) {
        String currency = Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency);
        String  currency_price =  Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency_price);
        viewHolder.feesnameTV.setText(feesnameList.get(position));
        viewHolder.feecodeTV.setText(feesCodeList.get(position));
        String defaultDateFormat = Utility.getSharedPreferences(context.getApplicationContext(), "dateFormat");
        JSONObject feesDetailsJson = new JSONObject();

        try {
            feesDetailsJson = new JSONObject(feesDetails.get(position));
            System.out.println( "amount array length==" +feesDetailsJson.length());
            String paymentmode=feesDetailsJson.getString("payment_mode");
            if(paymentmode.equals("upi")){
                paymentmode="UPI";
            }else if(paymentmode.equals("bank_transfer")){
                paymentmode="Bank Transfer";
            }else if(paymentmode.equals("bank_payment")){
                paymentmode="Bank Payment";
            }else{
                paymentmode=paymentmode;
            }
            viewHolder.paymentidTV.setText(feesSessionIdList.get(position));
            viewHolder.descriptionTV.setText( feesDetailsJson.getString("description"));
            String cap = paymentmode.substring(0, 1).toUpperCase() + paymentmode.substring(1);
            viewHolder.modeTV.setText(cap);

            viewHolder.discountTV.setText( currency+Utility.changeAmount(feesDetailsJson.getString("amount_discount"),currency,currency_price));
            viewHolder.fineTV.setText( currency+Utility.changeAmount(feesDetailsJson.getString("amount_fine"),currency,currency_price));
            viewHolder.paidAmtTV.setText(currency+Utility.changeAmount( feesDetailsJson.getString("amount"),currency,currency_price));
            Float total=Float.parseFloat(feesDetailsJson.getString("amount"))+Float.parseFloat( feesDetailsJson.getString("amount_fine"));
            System.out.println("total=="+total);
            System.out.println("total fees=="+Utility.changeAmount(String.valueOf(total),currency,currency_price));
            viewHolder.totalfeesTV.setText( currency +  Utility.changeAmount(String.valueOf(total),currency,currency_price));
            viewHolder.feefineTV.setText( " + " + Utility.changeAmount(feesDetailsJson.getString("amount_fine"),currency,currency_price));
            viewHolder.dateTV.setText(dueDateList.get(position));




        } catch (JSONException je) {
            Log.e("Error Parseing Data", je.toString());
        }


        viewHolder.feesStatusTV.setText(statusList.get(position));


        if(Utility.getSharedPreferencesBoolean(context, Constants.showPaymentBtn))  {
            Log.e("testing", "testing 1");
            viewHolder.payBtn.setVisibility(View.GONE);
            if(statusList.get(position).equals("Paid")) {
                viewHolder.payBtn.setVisibility(View.GONE);
            } if (statusList.get(position).equals("Unpaid")) {
                viewHolder.payBtn.setVisibility(View.GONE);
            } if (statusList.get(position).equals("Partial")) {
                viewHolder.payBtn.setVisibility(View.GONE);
            }
        } else {
            Log.e("testing", "testing 2");
            viewHolder.payBtn.setVisibility(View.GONE);
        }

        //DECORATE
        viewHolder.header.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        viewHolder.payBtn.setText(Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency) + " " + context.getApplicationContext().getString(R.string.pay));
        //DECORATE

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

    private void showPaymentChooser(final String feesIdList, final String feesTypeId, final String feesSessionId, final String paymenttype, final String transfeesIdList) {
        final Dialog dialog = new Dialog(context);
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
