package com.example.ravikiran.logbook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ManageBanksActivity extends Activity_Raw {

    private static final String TAG = "ManageBanksActivity";
    DBclass myDB;
    ListView lv_mblist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_banks);

        myDB = new DBclass(this);
        lv_mblist = findViewById(R.id.lv_mblist);

        setListView();

        lv_mblist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ID_Name_Bal_Object e = (ID_Name_Bal_Object) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ManageBanksActivity.this, MBhistoryActivity.class);
                intent.putExtra("Name", e.getName());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    private void setListView() {
        ArrayList<ID_Name_Bal_Object> e;
        e = myDB.getTable("MONEY_HOLDERS", false);
        ListAdapter adapter = new ID_Name_Bal_Layout(this, e);
        lv_mblist.setAdapter(adapter);
        Log.d(TAG, "setListView set.");
    }
}
