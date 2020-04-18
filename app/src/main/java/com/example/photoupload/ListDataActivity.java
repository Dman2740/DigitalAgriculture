package com.example.photoupload;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListDataActivity extends AppCompatActivity
{
    private static final String TAG="ListDataActivity";
    DatabaseHelper mDatabaseHelper;
    private ListView mListView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mListView=findViewById(R.id.listView);
        mDatabaseHelper=new DatabaseHelper(this);
        populateListView();
    }

    private void populateListView()
    {
        Log.d(TAG, "populateListView: Displaying data in the ListView. ");
        Cursor data = mDatabaseHelper.getPlantData();
        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext())
        {
            listData.add(data.getString(0));//Id
            listData.add(data.getString(1));//Category
            listData.add(data.getString(2));//Name
            listData.add(data.getString(4));//Pollinator
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);
    }
}
