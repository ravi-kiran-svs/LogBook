package com.example.ravikiran.logbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ExpenditureActivity extends AppCompatActivity {

    private static final String TAG = "ExpenditureActivity";
    DBclass myDB;
    ListView lv_explist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure);

        myDB = new DBclass(this);
        lv_explist = findViewById(R.id.lv_explist);

        setListView();

        lv_explist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ID_Name_Bal_Object e = (ID_Name_Bal_Object) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ExpenditureActivity.this, exphistoryActivity.class);
                intent.putExtra("Name", e.getName());
                startActivity(intent);
            }
        });
    }

    private void setListView() {
        ArrayList<ID_Name_Bal_Object> e;
        e = myDB.getTable("MONEY_PULLERS", false);
        ListAdapter adapter = new ID_Name_Bal_Layout(this, e);
        lv_explist.setAdapter(adapter);
        Log.d(TAG, "setListView: set.");
    }
}
