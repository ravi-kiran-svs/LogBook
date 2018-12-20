package com.example.ravikiran.logbook;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AddRow extends AppCompatActivity {

    //private static final String TAG = "MainActivity";
    DBclass myDB;
    CalenderDate myCal;
    TextView tv_from, tv_error_0, tv_error_1;
    EditText et_name, et_bal;
    Spinner sp_add, sp_sign, sp_from;
    Button b_add;
    int[] errorCode;
    int[] TextColor;
    char stateCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_row);

        myDB    = new DBclass(this);
        myCal   = new CalenderDate(this);
        sp_add  = findViewById(R.id.sp_add);
        et_name = findViewById(R.id.et_name);
        sp_sign = findViewById(R.id.sp_sign);
        et_bal  = findViewById(R.id.et_bal);
        tv_from = findViewById(R.id.tv_from);
        sp_from = findViewById(R.id.sp_from);
        b_add   = findViewById(R.id.b_add);
        tv_error_0 = findViewById(R.id.tv_error_0);
        tv_error_1 = findViewById(R.id.tv_error_1);

        errorCode = new int[] {1,0};
        TextColor = new int[] {this.getResources().getColor(R.color.colortext), this.getResources().getColor(R.color.wrongText)};
        stateCode = 'Z';

        //TODO : get extras from intent to set initial select.

        ArrayAdapter<String> adapter_add = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[] {"Money Holder", "Expense", "Friend"});
        sp_add.setAdapter(adapter_add);

        ArrayAdapter<String> adapter_sign = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[] {"loaned you", "borrowed"});
        sp_sign.setAdapter(adapter_sign);

        ArrayAdapter<String> adapter_from = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new String[] {""});
        sp_from.setAdapter(adapter_from);

        sp_add.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nestedConditions(i,
                        et_bal.getText().toString(),
                        sp_sign.getSelectedItemPosition(),
                        sp_from.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sp_sign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nestedConditions(sp_add.getSelectedItemPosition(),
                        et_bal.getText().toString(),
                        i,
                        sp_from.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sp_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nestedConditions(sp_add.getSelectedItemPosition(),
                        et_bal.getText().toString(),
                        sp_sign.getSelectedItemPosition(),
                        sp_from.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        et_bal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                nestedConditions(sp_add.getSelectedItemPosition(),
                        editable.toString(),
                        sp_sign.getSelectedItemPosition(),
                        sp_from.getSelectedItem().toString());
            }
        });

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    errorCode[0] = 1;
                    et_name.setTextColor(TextColor[0]);
                    tv_error_0.setText("");
                }   else if (editable.toString().equals("void (nothing)") ||
                        myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(editable.toString()) ||
                        myDB.getColumnNames(new String[]{"MONEY_PULLERS"}).contains(editable.toString()) ||
                        myDB.getColumnNames(new String[]{"LOAN_BALANCE"}).contains(editable.toString())) {
                    errorCode[0] = 2;
                    et_name.setTextColor(TextColor[1]);
                    tv_error_0.setText("Name already exists.");
                }   else {
                    errorCode[0] = 0;
                    et_name.setTextColor(TextColor[0]);
                    tv_error_0.setText("");
                }
            }
        });

        //TODO : when adding expenses change the order as others should be last;
        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long s_bal;
                if (et_bal.getText().toString().equals("")) s_bal = 0;
                else    s_bal = Long.valueOf(et_bal.getText().toString());

                if (errorCode[0] == 0 && errorCode[1] == 0) {
                    switch (sp_add.getSelectedItemPosition())   {
                        case 0:
                            myDB.AddRow("MONEY_HOLDERS",
                                    et_name.getText().toString());
                            if (s_bal != 0) {
                                myDB.transfer(sp_from.getSelectedItem().toString(),
                                        et_name.getText().toString(),
                                        s_bal);
                                if (sp_from.getSelectedItem().toString().equals("void (nothing)"))
                                    myCal.showToast("New MoneyHolder \"" +
                                            et_name.getText().toString() +
                                            "\" added with balance Rs." +
                                            s_bal +
                                            ".");
                                else
                                    myCal.showToast("New MoneyHolder \"" +
                                            et_name.getText().toString() +
                                            "\" added with balance Rs." +
                                            s_bal +
                                            " from \"" +
                                            sp_from.getSelectedItem().toString() +
                                            "\".");
                            }   else
                                myCal.showToast("New MoneyHolder \"" +
                                        et_name.getText().toString() +
                                        "\" added.");
                            break;
                        case 1:
                            myDB.AddRow("MONEY_PULLERS",
                                    et_name.getText().toString());
                            if (s_bal != 0) {
                                myDB.transfer(sp_from.getSelectedItem().toString(),
                                        et_name.getText().toString(),
                                        s_bal);
                                if (sp_from.getSelectedItem().toString().equals("void (nothing)"))
                                    myCal.showToast("New Expense \"" +
                                            et_name.getText().toString() +
                                            "\" added with initial amount Rs." +
                                            s_bal +
                                            ".");
                                else
                                    myCal.showToast("New Expense \"" +
                                            et_name.getText().toString() +
                                            "\" added with initial amount Rs." +
                                            s_bal +
                                            " from \"" +
                                            sp_from.getSelectedItem().toString() +
                                            "\".");
                            }   else
                                myCal.showToast("New Expense \"" +
                                        et_name.getText().toString() +
                                        "\" added.");
                            break;
                        case 2:
                            myDB.AddRow("LOAN_BALANCE",
                                    et_name.getText().toString());
                            if (s_bal != 0) {
                                switch (sp_sign.getSelectedItemPosition()) {
                                    case 0:
                                        myDB.transfer(et_name.getText().toString(),
                                                sp_from.getSelectedItem().toString(),
                                                s_bal);
                                        if (myDB.getColumnNames(new String[]{"MONEY_PULLERS"}).
                                                contains(sp_from.getSelectedItem().toString()))
                                            myCal.showToast("New Friend \"" +
                                                    et_name.getText().toString() +
                                                    "\" loaned you Rs." +
                                                    s_bal +
                                                    " for \"" +
                                                    sp_from.getSelectedItem().toString() +
                                                    "\".");
                                        else
                                            myCal.showToast("New Friend \"" +
                                                    et_name.getText().toString() +
                                                    "\" loaned you Rs." +
                                                    s_bal +
                                                    " added to \"" +
                                                    sp_from.getSelectedItem().toString() +
                                                    "\".");
                                        break;
                                    case 1:
                                        myDB.transfer(sp_from.getSelectedItem().toString(),
                                                et_name.getText().toString(),
                                                s_bal);
                                        if (sp_from.getSelectedItem().toString().equals("void (nothing)"))
                                            myCal.showToast("New Friend \"" +
                                                    et_name.getText().toString() +
                                                    "\" borrowed Rs." +
                                                    s_bal +
                                                    ".");
                                        else
                                            myCal.showToast("New Friend \"" +
                                                    et_name.getText().toString() +
                                                    "\" borrowed Rs." +
                                                    s_bal +
                                                    " from \"" +
                                                    sp_from.getSelectedItem().toString() +
                                                    "\".");
                                        break;
                                }
                                break;
                            }   else
                                myCal.showToast("New Friend \"" +
                                        et_name.getText().toString() +
                                        "\" added and is balanced.");
                    }
                    finish();
                }   else if (errorCode[0] == 1) {
                    myCal.showToast("Empty name is not accepted.");
                }   else if (errorCode[0] == 2) {
                    myCal.showToast("Name \"" + et_name.getText().toString() + "\" already exists.");
                }   else if (errorCode[1] == 1) {
                    myCal.showToast("Insufficient funds.");
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void nestedConditions(int i_add, String s_bal, int i_sign, String s_from)  {
        int y_from = tv_from.getMeasuredHeight() + 24;
        switch (i_add) {
            case 0:
                if (s_bal.equals("") || Long.valueOf(s_bal) == 0) {
                    // A
                    if (stateCode != 'A') {
                        sp_sign.setEnabled(false);
                        et_bal.setHint("Balance (Default 0)");
                        tv_from.setVisibility(View.INVISIBLE);
                        sp_from.setEnabled(false);
                        b_add.setTranslationY(-y_from);
                        stateCode = 'A';
                    }
                }   else    {
                    // B
                    if (stateCode != 'B') {
                        sp_sign.setEnabled(false);
                        tv_from.setVisibility(View.VISIBLE);
                        tv_from.setText("From");
                        sp_from.setEnabled(true);
                        ArrayList<String> list_from_0 = myDB.getColumnNames(new String[]{"MONEY_HOLDERS", "LOAN_BALANCE"});
                        list_from_0.add(0, "void (nothing)");
                        ArrayAdapter<String> adapter_from_0 = new ArrayAdapter<>(AddRow.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                list_from_0);
                        sp_from.setAdapter(adapter_from_0);
                        b_add.setTranslationY(0);
                        stateCode = 'B';
                    }
                }
                break;

            case 1:
                if (s_bal.equals("") || Long.valueOf(s_bal) == 0) {
                    // C
                    if (stateCode != 'C') {
                        sp_sign.setEnabled(false);
                        et_bal.setHint("Initial expense (Default 0)");
                        tv_from.setVisibility(View.INVISIBLE);
                        sp_from.setEnabled(false);
                        b_add.setTranslationY(-y_from);
                        stateCode = 'C';
                    }
                }   else    {
                    // D
                    if (stateCode != 'D') {
                        sp_sign.setEnabled(false);
                        tv_from.setVisibility(View.VISIBLE);
                        tv_from.setText("From");
                        sp_from.setEnabled(true);
                        ArrayList<String> list_from_1 = myDB.getColumnNames(new String[]{"MONEY_HOLDERS", "LOAN_BALANCE"});
                        list_from_1.add(0, "void (nothing)");
                        ArrayAdapter<String> adapter_from_1 = new ArrayAdapter<>(AddRow.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                list_from_1);
                        sp_from.setAdapter(adapter_from_1);
                        b_add.setTranslationY(0);
                        stateCode = 'D';
                    }
                }
                break;

            case 2:
                if (s_bal.equals("") || Long.valueOf(s_bal) == 0) {
                    // E
                    if (stateCode != 'E') {
                        sp_sign.setEnabled(true);
                        et_bal.setHint("Amount");
                        tv_from.setVisibility(View.INVISIBLE);
                        sp_from.setEnabled(false);
                        b_add.setTranslationY(-y_from);
                        stateCode = 'E';
                    }
                }   else    {
                    switch (i_sign)    {
                        case 0:
                            if  (stateCode != 'G' &&
                                    stateCode != 'H')   {
                                sp_from.setEnabled(true);
                                ArrayAdapter<String> adapter_from_3 = new ArrayAdapter<>(AddRow.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        myDB.getColumnNames(new String[]{"MONEY_PULLERS", "MONEY_HOLDERS"}));
                                sp_from.setAdapter(adapter_from_3);
                            }

                            if (myDB.getColumnNames(new String[] {"MONEY_PULLERS"}).contains(s_from))  {
                                // G
                                if (stateCode != 'G') {
                                    sp_sign.setEnabled(true);
                                    tv_from.setVisibility(View.VISIBLE);
                                    tv_from.setText("For");
                                    b_add.setTranslationY(0);
                                    stateCode = 'G';
                                }
                            }   else if (myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(s_from))  {
                                // H
                                if (stateCode != 'H')   {
                                    sp_sign.setEnabled(true);
                                    tv_from.setVisibility(View.VISIBLE);
                                    tv_from.setText("Add to");
                                    b_add.setTranslationY(0);
                                    stateCode = 'H';
                                }
                            }
                            break;

                        case 1:
                            // F
                            if (stateCode != 'F') {
                                sp_sign.setEnabled(true);
                                tv_from.setVisibility(View.VISIBLE);
                                tv_from.setText("From");
                                sp_from.setEnabled(true);
                                ArrayList<String> list_from_2 = myDB.getColumnNames(new String[]{"MONEY_HOLDERS"});
                                list_from_2.add(0, "void (nothing)");
                                ArrayAdapter<String> adapter_from_2 = new ArrayAdapter<>(AddRow.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        list_from_2);
                                sp_from.setAdapter(adapter_from_2);
                                b_add.setTranslationY(0);
                                stateCode = 'F';
                            }
                    }
                }
                break;
        }

        if (tv_from.isCursorVisible() &&
                tv_from.getText().toString().equals("From") &&
                myDB.getColumnNames(new String[]{"MONEY_HOLDERS"}).contains(s_from) &&
                Long.valueOf(s_bal) > myDB.getAmount(s_from)) {
            errorCode[1] = 1;
            et_bal.setTextColor(TextColor[1]);
            tv_error_1.setText("Insufficient funds.");
        }   else {
            errorCode[1] = 0;
            et_bal.setTextColor(TextColor[0]);
            tv_error_1.setText("");
        }
    }
}
