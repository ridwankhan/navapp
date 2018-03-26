package com.example.ridwankhan.navapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
//import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {


    private LineChart lineChart;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Home home = new Home();
                    android.app.FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.contentLayout, home, home.getTag()).commit();

                    return true;
                case R.id.navigation_dashboard:
//                    setupFragment(R.id.fragment_dash_board, "Dashboard");
//                    break;
                    DashBoard dashBoard = new DashBoard();
                    android.app.FragmentManager manager2 = getFragmentManager();
                    manager2.beginTransaction().replace(R.id.contentLayout, dashBoard, dashBoard.getTag()).commit();
                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart = (LineChart) findViewById(R.id.line_chart);
        float yValues [] = {10,20,30,0,40,60};
        String xValues [] = {"first","second","third","fourth","fifth","sixth"};
        drawLineChart(yValues, xValues);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void drawLineChart(float[] yValues, String[] xValues){
        Description desc = new Description();
        desc.setText("Test Line Chart");
        lineChart.setDescription(desc);
        ArrayList<Entry> yData = new ArrayList<>();
        for (int i = 0; i < yValues.length; i++){
            yData.add(new Entry(yValues[i],i));
        }
        ArrayList<String> xData = new ArrayList<>();
        for (int i = 0; i < xValues.length; i++){
            xData.add(xValues[i]);
        }
        LineDataSet lineDataSet = new LineDataSet(yData, "Test Line Chart");
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        LineData lineData = new LineData((ILineDataSet)xData, lineDataSet);
        lineData.setValueTextSize(13f);
        lineData.setValueTextColor(Color.BLACK);

        lineChart.setData(lineData);
        lineChart.invalidate();
    }
//
//    @Override
//    public void onFragmentInteraction(Uri uri){
//        //you can leave it empty
//    }

}
