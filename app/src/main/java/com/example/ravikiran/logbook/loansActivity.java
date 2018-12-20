package com.example.ravikiran.logbook;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class loansActivity extends AppCompatActivity {

    private static final String TAG = "loansActivity";
    private CalenderDate myCal;
    DBclass myDB;
    ListView lv_loanlist;
    Button b_history, b_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans);

        myDB = new DBclass(this);
        myCal = new CalenderDate(this);
        lv_loanlist = findViewById(R.id.lv_loanlist);
        b_history = findViewById(R.id.b_history);
        b_add = findViewById(R.id.b_add);

        setListView();

        lv_loanlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ID_Name_Bal_Object e = (ID_Name_Bal_Object) lv_loanlist.getItemAtPosition(i);
                final String s_xyz = e.getName();

                final LayoutInflater inf = LayoutInflater.from(loansActivity.this);
                @SuppressLint({"ViewHolder", "InflateParams"})
                View v = inf.inflate(R.layout.dialog_loans, null);
                TextView tv_xyz = v.findViewById(R.id.tv_xyz);
                final Spinner sp_sign = v.findViewById(R.id.sp_sign);
                final TextView tv_for = v.findViewById(R.id.tv_for);
                final Spinner sp_for = v.findViewById(R.id.sp_for);
                final EditText et_amount = v.findViewById(R.id.et_amount);
                final int[] errorCode = new int[1];

                final int TextColorBlack = getResources().getColor(R.color.colortext);
                final int TextColorRed = getResources().getColor(R.color.wrongText);

                tv_xyz.setText(s_xyz);
                ArrayAdapter<String> sp_sign_adapter = new ArrayAdapter<>(loansActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, new String[]{"takes from", "gives"});
                //try the sign stuff...
                sp_sign.setAdapter(sp_sign_adapter);

                tv_for.setText("From");
                ArrayList<String> sp_for_list = new ArrayList<>();
                ArrayAdapter<String> sp_for_adapter = new ArrayAdapter<>(loansActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, sp_for_list);
                sp_for.setAdapter(sp_for_adapter);
                final TextView tv_error = v.findViewById(R.id.tv_error);
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

                        }   else if (sp_sign.getSelectedItemPosition()==0
                                && Long.valueOf(et_amount.getText().toString()) > myDB.getAmount(sp_for.getSelectedItem().toString())) {
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

                sp_for.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                        }   else if (sp_sign.getSelectedItemPosition()==0
                                && Long.valueOf(et_amount.getText().toString()) > myDB.getAmount(sp_for.getSelectedItem().toString())) {
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

                sp_sign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String tv_tv = "";
                        ArrayList<String> sp_sp = new ArrayList<>();
                        if(i == 0) {
                            tv_tv = "From";
                            sp_sp = myDB.getColumnNames(new String[]{"MONEY_HOLDERS"});
                        }
                        else if(i == 1) {
                            tv_tv = "Add to";
                            sp_sp = myDB.getColumnNames(new String[] {"MONEY_HOLDERS", "MONEY_PULLERS"});
                        }
                        tv_for.setText(tv_tv);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(loansActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, sp_sp);
                        sp_for.setAdapter(adapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //Nothing doing...
                    }
                });

                AlertDialog.Builder build = new AlertDialog.Builder(loansActivity.this);
                build.setView(v)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (errorCode[0] == -1) {
                                    String s_from = "";
                                    String s_to = "";
                                    if (sp_sign.getSelectedItemPosition() == 0) {
                                        s_from = sp_for.getSelectedItem().toString();
                                        s_to = s_xyz;
                                    } else if (sp_sign.getSelectedItemPosition() == 1) {
                                        s_from = s_xyz;
                                        s_to = sp_for.getSelectedItem().toString();
                                    }
                                    long s_amount = Long.valueOf(et_amount.getText().toString());
                                    myDB.transfer(s_from, s_to, s_amount);

                                    if (sp_sign.getSelectedItemPosition() == 0) {
                                        if (myDB.getAmount(s_xyz) >= 0)
                                            myCal.showToast("Rs." + s_amount + " borrowed by " + s_to + " from" + s_from + ".");
                                        else if (myDB.getAmount(s_xyz) < 0)
                                            myCal.showToast("Rs." + s_amount + " returned to " + s_to + " from" + s_from + ".");

                                    } else if (sp_sign.getSelectedItemPosition() == 1) {
                                        if (myDB.getAmount(s_xyz) >= 0) {
                                            if (myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(s_to))
                                                myCal.showToast("Rs." + s_amount + " returned by " + s_from + " to " + s_to + ".");
                                            else if (myDB.getColumnNames(new String[]{"MONEY_PULLERS"}).contains(s_to))
                                                myCal.showToast("Rs." + s_amount + " returned by " + s_from + ", used for " + s_to + ".");

                                        } else if (myDB.getAmount(s_xyz) < 0) {
                                            if (myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(s_to))
                                                myCal.showToast("Rs." + s_amount + " returned by " + s_from + ", deposited in " + s_to + ".");
                                            else if (myDB.getColumnNames(new String[]{"MONEY_PULLERS"}).contains(s_to))
                                                myCal.showToast("Rs." + s_amount + " returned by " + s_from + " for " + s_to + ".");
                                        }
                                    }
                                    setListView();

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

        b_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loansActivity.this, loanhistoryActivity.class);
                startActivity(intent);
            }
        });

        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loansActivity.this, AddRow.class);
                startActivity(intent);
            }
        });
    }

    private void setListView() {
        ArrayList<ID_Name_Bal_Object> e;
        e = myDB.getTable("LOAN_BALANCE", false);
        ListAdapter adapter = new Loan_Layout(this, e);
        lv_loanlist.setAdapter(adapter);
        Log.d(TAG, "setListView: set.");
    }
}
