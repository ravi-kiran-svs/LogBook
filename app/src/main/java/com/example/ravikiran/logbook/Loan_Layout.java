package com.example.ravikiran.logbook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class Loan_Layout extends ArrayAdapter<ID_Name_Bal_Object> {

    Loan_Layout(Context context, ArrayList<ID_Name_Bal_Object> e) {
        super(context, R.layout.layout_loan, e);
    }

    @NonNull
    public View getView (int i, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inf = LayoutInflater.from(getContext());
        @SuppressLint("ViewHolder")
        View v = inf.inflate(R.layout.layout_loan, parent, false);
        ID_Name_Bal_Object e = getItem(i);

        //ImageView sign = v.findViewById(R.id.iv_sign);
        TextView Text = v.findViewById(R.id.tv_text);

        assert e != null;
        String s_name = e.getName();
        long s_bal = e.getBalance();
        String s_text;

        if (s_bal == 0)
            s_text = s_name + " is balanced.";
        else if (s_bal > 0)
            s_text = s_name + " borrowed from " + "You Rs." + abs(s_bal);
        else
            s_text = s_name + " demands " + "You Rs." + abs(s_bal);

        //sign.setImageResource(R.drawable.xyz);
        Text.setText(s_text);

        return v;
    }
}
