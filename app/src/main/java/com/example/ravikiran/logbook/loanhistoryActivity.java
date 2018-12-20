package com.example.ravikiran.logbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class loanhistoryActivity extends AppCompatActivity {

    private static final String TAG = "loanHistoryActivity";
    DBclass myDB;
    ListView lv_loanHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loanhistory);

        myDB = new DBclass(this);
        lv_loanHistory = findViewById(R.id.lv_loanHistory);
        setListView();
    }

    private void setListView() {
        ArrayList<ID_Name_Bal_Object> e;
        e = myDB.getFullLog();
        ListAdapter adapter = new Loan_History_Layout(this, e);
        lv_loanHistory.setAdapter(adapter);
        Log.d(TAG, "setListView: set.");
    }
}
