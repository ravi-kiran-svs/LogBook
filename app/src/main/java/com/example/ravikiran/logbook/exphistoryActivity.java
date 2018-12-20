package com.example.ravikiran.logbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class exphistoryActivity extends AppCompatActivity {

    private static final String TAG = "expHistoryActivity";
    DBclass myDB;
    ListView lv_ExpHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exphistory);

        myDB = new DBclass(this);
        lv_ExpHistory = findViewById(R.id.lv_ExpHistory);
        setListView();
    }

    private void setListView() {
        ArrayList<ID_Name_Bal_Object> e;
        e = myDB.getLog(new String[]{getIntent().getStringExtra("Name")});
        ListAdapter adapter = new MB_History_Layout(this, e);
        lv_ExpHistory.setAdapter(adapter);
        Log.d(TAG, "setListView: set.");
    }
}
