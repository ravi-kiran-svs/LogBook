package com.example.ravikiran.logbook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Loan_History_Layout extends ArrayAdapter<ID_Name_Bal_Object>{

    DBclass myDB;
    private CalenderDate myCal;

    Loan_History_Layout(@NonNull Context context, ArrayList<ID_Name_Bal_Object> e) {
        super(context, R.layout.layout_loan_history, e);

        myDB = new DBclass(context);
        myCal = new CalenderDate(context);
    }

    @NonNull
    public View getView(int i, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inf = LayoutInflater.from(getContext());
        @SuppressLint("ViewHolder")
        View v = inf.inflate(R.layout.layout_loan_history, parent, false);
        ID_Name_Bal_Object e = getItem(i);

        //ImageView sign =      v.findViewById(R.id.iv_sign);
        TextView tv_date =      v.findViewById(R.id.tv_date);
        TextView tv_amount =    v.findViewById(R.id.tv_amount);
        TextView tv_verb1 =      v.findViewById(R.id.tv_verb1);
        TextView tv_noun1 =      v.findViewById(R.id.tv_noun1);
        TextView tv_verb2 =      v.findViewById(R.id.tv_verb2);
        TextView tv_noun2 =      v.findViewById(R.id.tv_noun2);

        assert e != null;
        String s_date = e.getDate();
        String s_giver = e.getGiver();
        String s_taker = e.getTaker();
        long s_bal = e.getBalance();

        String s_verb1 = "";
        String s_noun1 = "";
        String s_verb2 = "";
        String s_noun2 = "";
        String s_amount;

        s_date = myCal.getDate(s_date);
        s_amount = "Rs." + s_bal;

        if (myDB.getColumnNames(new String[]{"LOAN_BALANCE"}).contains(s_taker))   {
            if (myDB.getAmount(s_taker) >= 0)
                s_verb1 = " borrowed by ";
            else if (myDB.getAmount(s_taker) < 0)
                s_verb1 = " returned to ";
            s_noun1 = s_taker;

            if (myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(s_giver))
                s_verb2 = " from ";
            s_noun2 = s_giver + ".";

        }   else if (myDB.getColumnNames(new String[]{"LOAN_BALANCE"}).contains(s_giver))   {
            if (myDB.getAmount(s_giver) >= 0) {
                s_verb1 = " returned by ";
                if (myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(s_taker))
                    s_verb2 = " to ";
                else if (myDB.getColumnNames(new String[]{"MONEY_PULLERS"}).contains(s_taker))
                    s_verb2 = ", used for ";

            }   else if (myDB.getAmount(s_giver) < 0) {
                s_verb1 = " loaned by ";
                if (myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(s_taker))
                    s_verb2 = ", deposited in ";
                else if (myDB.getColumnNames(new String[]{"MONEY_PULLERS"}).contains(s_taker))
                    s_verb2 = " for ";
            }
            s_noun1 = s_giver;
            s_noun2 = s_taker + ".";
        }

        //sign.setImageResource(R.drawable.xyz);
        tv_date.setText(s_date);
        tv_amount.setText(s_amount);
        tv_verb1.setText(s_verb1);
        tv_noun1.setText(s_noun1);
        tv_verb2.setText(s_verb2);
        tv_noun2.setText(s_noun2);

        return v;
    }
}
