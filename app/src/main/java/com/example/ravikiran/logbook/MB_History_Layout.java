package com.example.ravikiran.logbook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class MB_History_Layout extends ArrayAdapter<ID_Name_Bal_Object>{

    DBclass myDB;
    private CalenderDate myCal;
    private String ActName;

    MB_History_Layout(Context context, ArrayList<ID_Name_Bal_Object> e) {
        super(context, R.layout.layout_mbhistory, e);

        myDB = new DBclass(context);
        myCal = new CalenderDate(context);
        Activity a = (Activity) context;
        ActName = a.getClass().getSimpleName();
    }

    @NonNull
    public View getView(int i, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inf = LayoutInflater.from(getContext());
        @SuppressLint("ViewHolder")
        View v = inf.inflate(R.layout.layout_mbhistory, parent, false);
        ID_Name_Bal_Object e = getItem(i);

        //ImageView sign =      v.findViewById(R.id.iv_sign);
        TextView tv_date =      v.findViewById(R.id.tv_date);
        TextView tv_amount =    v.findViewById(R.id.tv_amount);
        TextView tv_verb =      v.findViewById(R.id.tv_verb1);
        TextView tv_noun =      v.findViewById(R.id.tv_noun);

        assert e != null;
        String s_date = e.getDate();
        String s_name = e.getName();
        long s_bal = e.getBalance();

        String s_verb = "";
        String s_amount;

        s_date = myCal.getDate(s_date);
        s_amount = "Rs." + abs(s_bal) ;

        if(s_bal >= 0) {
            if (ActName.equals("MBhistoryActivity")) {
                if (s_name.equals("void (nothing)")) s_verb = " added.";
                else if (myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(s_name))
                    s_verb = " transferred from ";
                else if (myDB.getColumnNames(new String[]{"LOAN_BALANCE"}).contains(s_name))
                    s_verb = " added by ";

            } else if (ActName.equals("exphistoryActivity")) {
                if (s_name.equals("void (nothing)")) s_verb = " spent.";
                else if (myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(s_name))
                    s_verb = " spent from ";
                else if (myDB.getColumnNames(new String[]{"LOAN_BALANCE"}).contains(s_name))
                    s_verb = " paid by ";
            }
        }
        else {
            if(s_name.equals("void (nothing)")) s_verb = "Vanished.";
            else if(myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(s_name))    s_verb = " transferred to ";
            else if(myDB.getColumnNames(new String[]{"MONEY_PULLERS"}).contains(s_name))    s_verb = " spent on ";
            else if(myDB.getColumnNames(new String[]{"LOAN_BALANCE"}).contains(s_name))     s_verb = " loaned to ";
        }
        if(s_name.equals("void (nothing)")) s_name = "";
        else                                s_name = s_name + ".";

        //sign.setImageResource(R.drawable.xyz);
        tv_date.setText(s_date);
        tv_amount.setText(s_amount);
        tv_verb.setText(s_verb);
        tv_noun.setText(s_name);

        return v;
    }
}
