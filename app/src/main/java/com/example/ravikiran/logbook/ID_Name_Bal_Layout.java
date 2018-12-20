package com.example.ravikiran.logbook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ID_Name_Bal_Layout extends ArrayAdapter<ID_Name_Bal_Object> {

    //private static final String TAG = "Layout";
    private CalenderDate myCal;
    private DBclass myDB;
    private Activity activity;
    private String ActName;

    ID_Name_Bal_Layout(Context context, ArrayList<ID_Name_Bal_Object> e) {
        super(context, R.layout.layout_id_name_balance, e);

        myDB = new DBclass(context);
        myCal = new CalenderDate(context);
        activity = (Activity) context;
        ActName = activity.getClass().getSimpleName();
    }

    @NonNull
    public View getView (int i, View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater inf = LayoutInflater.from(getContext());
        @SuppressLint("ViewHolder") View v = inf.inflate(R.layout.layout_id_name_balance, parent, false);
        ID_Name_Bal_Object e = getItem(i);

        Button b_add = v.findViewById(R.id.b_add);
        TextView Name = v.findViewById(R.id.tv_name);
        TextView Bal = v.findViewById(R.id.tv_balance);

        final int TextColorBlack = activity.getResources().getColor(R.color.colortext);
        final int TextColorRed = activity.getResources().getColor(R.color.wrongText);

        assert e != null;
        final String s_name = e.getName();
        String s_bal = "";
        String d_title = "";
        if (ActName.equals("ManageBanksActivity"))  {
            s_bal = "Balance: Rs." + e.getBalance();
            d_title = "Add to " + s_name;
        }   else if (ActName.equals("ExpenditureActivity")) {
            s_bal = "Expenditure: Rs." + e.getBalance();
            d_title = "Spend on  " + s_name;
        }

        Name.setText(s_name);
        Bal.setText(s_bal);

        final String finalD_title = d_title;
        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint({"ViewHolder", "InflateParams"})
                View v1 = inf.inflate(R.layout.dialog_manage_banks, null);
                final int[] errorCode = new int[1];
                final Spinner sp_from = v1.findViewById(R.id.sp_from);
                ArrayList<String> sp_list = myDB.getColumnNames(new String[]{"MONEY_HOLDERS", "LOAN_BALANCE"});
                sp_list.add("void (nothing)");
                sp_list.remove(s_name);
                ArrayAdapter<String> adap = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item, sp_list);
                sp_from.setAdapter(adap);
                final EditText et_amount = v1.findViewById(R.id.et_amount);
                final TextView tv_error = v1.findViewById(R.id.tv_error);
                tv_error.setText("");

                et_amount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {   }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {   }
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void afterTextChanged(Editable editable) {
                        if  (et_amount.getText().toString().equals("")) {
                            errorCode[0] = 0;
                            tv_error.setText("");

                        }   else if (Long.valueOf(et_amount.getText().toString()) == 0) {
                            errorCode[0] = 1;
                            et_amount.setTextColor(TextColorRed);
                            tv_error.setText("Zero not accepted");

                        }   else if (myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(sp_from.getSelectedItem().toString())
                                && Long.valueOf(et_amount.getText().toString()) > myDB.getAmount(sp_from.getSelectedItem().toString())) {
                            errorCode[0] = 2;
                            et_amount.setTextColor(TextColorRed);
                            tv_error.setText("Insufficient funds.");

                        }   else    {
                            errorCode[0] = -1;
                            et_amount.setTextColor(TextColorBlack);
                            tv_error.setText("");
                        }
                    }
                });

                sp_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if  (et_amount.getText().toString().equals("")) {
                            errorCode[0] = 0;
                            tv_error.setText("");

                        }   else if (Long.valueOf(et_amount.getText().toString()) == 0) {
                            errorCode[0] = 1;
                            et_amount.setTextColor(TextColorRed);
                            tv_error.setText("Zero not accepted");

                        }   else if (myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(sp_from.getSelectedItem().toString())
                                && Long.valueOf(et_amount.getText().toString()) > myDB.getAmount(sp_from.getSelectedItem().toString())) {
                            errorCode[0] = 2;
                            et_amount.setTextColor(TextColorRed);
                            tv_error.setText("Insufficient funds.");

                        }   else    {
                            errorCode[0] = -1;
                            et_amount.setTextColor(TextColorBlack);
                            tv_error.setText("");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                AlertDialog.Builder build = new AlertDialog.Builder(activity);
                build.setTitle(finalD_title)
                        .setView(v1)
                        .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (errorCode[0] == -1) {
                                    String s_from = sp_from.getSelectedItem().toString();
                                    long s_amount = Long.valueOf(et_amount.getText().toString());
                                    myDB.transfer(s_from, s_name, s_amount);
                                    //TODO Add "\n" ie next line character for toasts

                                    if (ActName.equals("ManageBanksActivity")) {
                                        if (s_from.equals("void (nothing)"))
                                            myCal.showToast("Rs." + s_amount + " added to " + s_name + ".");
                                        else if (myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(s_from))
                                            myCal.showToast("Rs." + s_amount + " transferred to " + s_name + " from " + s_from + ".");
                                        else if (myDB.getColumnNames(new String[]{"LOAN_BALANCE"}).contains(s_from))
                                            myCal.showToast("Rs." + s_amount + " added to " + s_name + " by " + s_from + ".");

                                    } else if (ActName.equals("ExpenditureActivity")) {
                                        if (s_from.equals("void (nothing)"))
                                            myCal.showToast("Rs." + s_amount + " spent on " + s_name + ".");
                                        else if (myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(s_from))
                                            myCal.showToast("Rs." + s_amount + " spent on " + s_name + " from " + s_from + ".");
                                        else if (myDB.getColumnNames(new String[]{"LOAN_BALANCE"}).contains(s_from))
                                            myCal.showToast("Rs." + s_amount + "spent on " + s_name + ", paid by " + s_from + ".");
                                    }

                                    Intent intent = new Intent(getContext(), activity.getClass());
                                    getContext().startActivity(intent);
                                    activity.finish();

                                }   else if (errorCode[0] == 0) {
                                    myCal.showToast("Empty values not accepted.");

                                }   else if (errorCode[0] == 1){
                                    myCal.showToast("Zero is not an accepted value.");

                                }   else if (errorCode[0] == 2){
                                    myCal.showToast("Action Failed. Insufficient funds.");
                                }
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Do nothing bitch...
                            }
                        });

                build.create().show();
            }
        });

        return v;
    }
}