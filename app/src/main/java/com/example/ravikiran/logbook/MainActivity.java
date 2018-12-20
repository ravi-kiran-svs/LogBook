package com.example.ravikiran.logbook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity_Raw {

    private static final String TAG = "MainActivity";
    DBclass myDB;
    Button b_MB, b_loans, b_exp;
    ListView lv_shouldpay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DBclass(this);
        b_MB = findViewById(R.id.b_MB);
        b_loans = findViewById(R.id.b_loans);
        b_exp = findViewById(R.id.b_exp);
        lv_shouldpay = findViewById(R.id.lv_shouldpay);

        setListView();

        b_MB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(MainActivity.this, ManageBanksActivity.class);
                startActivityForResult(i1, 11);
            }
        });

        b_loans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(MainActivity.this, loansActivity.class);
                startActivityForResult(i2, 11);
            }
        });

        b_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i3 = new Intent(MainActivity.this, ExpenditureActivity.class);
                startActivityForResult(i3, 11);
            }
        });

        lv_shouldpay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, loansActivity.class);
                startActivityForResult(intent, 11);
            }
        });
    }

    private void setListView() {
        ArrayList<ID_Name_Bal_Object> e;
        e = myDB.getTable("LOAN_BALANCE", true);
        ListAdapter adapter = new Loan_Layout(this, e);
        lv_shouldpay.setAdapter(adapter);
        Log.d(TAG, "setListView: set.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11)  {
            if (resultCode == RESULT_CANCELED)  {
                setListView();
            }
        }
    }
}
