package com.example.ravikiran.logbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MBhistoryActivity extends AppCompatActivity {

    private static final String TAG = "MBHistoryActivity";
    DBclass myDB;
    ListView lv_MBhistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mbhistory);

        myDB = new DBclass(this);
        lv_MBhistory = findViewById(R.id.lv_MBhistory);
        setListView();
    }

    private void setListView() {
        ArrayList<ID_Name_Bal_Object> e;
        e = myDB.getLog(new String[]{getIntent().getStringExtra("Name")});
        ListAdapter adapter = new MB_History_Layout(this, e);
        lv_MBhistory.setAdapter(adapter);
        Log.d(TAG, "setListView: set.");
    }
}
