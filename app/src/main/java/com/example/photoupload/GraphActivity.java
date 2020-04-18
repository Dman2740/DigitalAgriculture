package com.example.photoupload;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GraphActivity extends AppCompatActivity {
    Spinner spinner1, spinner2;
    DatabaseHelper mDatabaseHelper;
    LineChart lineChart;
    ArrayList<String> listCategories = new ArrayList<String>();
    LineDataSet lineDataSet;
    XAxis xAxis;
    String currentTF;
    String currentDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner_layout);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date=new Date();
        currentDate=sdf.format(date);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        lineChart = findViewById(R.id.chart1);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setDragEnabled(true);
        xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yAxis = lineChart.getAxisLeft();
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        populateDropDown();
        populateGraph();
        selectedItems();
    }

    public void populateDropDown() {
        mDatabaseHelper = new DatabaseHelper(this);
        ArrayList<String> listTimeFrame = new ArrayList<String>();
        listTimeFrame.add("Total Number Collected");
        listTimeFrame.add("Yearly");
        listTimeFrame.add("Monthly");
        listTimeFrame.add("Weekly");
        listTimeFrame.add("Daily");
        listCategories = mDatabaseHelper.getAllCategories();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listTimeFrame);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCategories);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
    }


    public void populateGraph()//default it populates graph without selection
    {
            ArrayList<Entry> dataset = mDatabaseHelper.getAllGraphData(listCategories);
       // for(int i=0;i<listCategories.size();i++) {
            //String eachCategory=listCategories.get(i);
           // ArrayList<Entry> dataset = mDatabaseHelper.getMonthlyData(eachCategory, currentDate);
            lineDataSet = new LineDataSet(dataset, "Collected Number");
            lineDataSet.setColor(Color.BLACK);
            lineDataSet.setMode(LineDataSet.Mode.LINEAR);
            lineDataSet.setDrawCircles(true);
            lineDataSet.setCubicIntensity(0.15f);
            lineDataSet.setCircleColor(Color.DKGRAY);
            lineDataSet.setLineWidth(2);
            LineData lineData = new LineData(lineDataSet);
            lineChart.setData(lineData);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getCategory()));
           // xAxis.setValueFormatter(new IndexAxisValueFormatter(getMonthly()));//getCategory
        //}

    }

    public ArrayList<String> getCategory()
    {
        ArrayList<String> label = new ArrayList<>();
        label.add(0, "All Categories");
        for (int i = 1; i < listCategories.size(); i++)//i=0
        {
            label.add(listCategories.get(i));
        }
        return label;
    }

    public void selectedItems()
    {
        currentTF=spinner1.getSelectedItem().toString();
        String currentCategory=spinner2.getSelectedItem().toString();
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(currentTF.equals("All Categories"))
                {
                    populateGraph();
                }
                else if(currentTF.equals("Yearly"))
                {

                }
                else if(currentTF.equals("Monthly"))
                {
                    for(int i=0;i<listCategories.size();i++) {
                        String eachCategory=listCategories.get(i);
                        ArrayList<Entry> monthSet = mDatabaseHelper.getMonthlyData(eachCategory, currentDate);
                        lineDataSet = new LineDataSet(monthSet, "Collected Number");
                        lineDataSet.setColor(Color.BLACK);
                        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
                        lineDataSet.setDrawCircles(true);
                        lineDataSet.setCubicIntensity(0.15f);
                        lineDataSet.setCircleColor(Color.DKGRAY);
                        lineDataSet.setLineWidth(2);
                        LineData lineData = new LineData(lineDataSet);
                        lineChart.setData(lineData);
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(getMonthly()));
                    }
                }
                else if(currentTF.equals("Weekly"))
                {

                }
                else if(currentTF.equals("Daily"))
                {
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(getDaily()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    public ArrayList<String> getDaily()
    {
        ArrayList<String> days = new ArrayList<>();
        days.add(0,"Mon");
        days.add(1,"Tue");
        days.add(2,"Wed");
        days.add(3,"Thu");
        days.add(4,"Fri");
        days.add(5,"Sat");
        days.add(6,"Sun");
        return days;
    }
    public ArrayList<String> getMonthly()
    {
        ArrayList<String> months = new ArrayList<>();
        months.add(0,"01");
        months.add(1,"02");
        months.add(2,"03");
        months.add(3,"04");
        months.add(4,"05");
        months.add(5,"06");
        months.add(6,"07");
        months.add(7,"08");
        months.add(8,"09");
        months.add(9,"10");
        months.add(10,"11");
        months.add(11,"12");
        return months;
    }

}
