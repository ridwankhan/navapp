package com.example.ridwankhan.navapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Chart extends Fragment {

    LineChart lineChart;
    DataCommunication mCallback;

    public Chart() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chart, container, false);
        //call the widgets
        lineChart = (LineChart)v.findViewById(R.id.Line_Chart);
        drawLineChart();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (DataCommunication) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DataCommunication");
        }
    }

    private void drawLineChart(){
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        Description desc = new Description();
        desc.setText("Test Line Chart");
        Log.d("DESC", desc.getText());
        lineChart.setDescription(desc);

        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<Integer> curr = mCallback.getCurrentSetArray();

        for(Integer i = 0; i < curr.size(); i++){
            yValues.add(new Entry(i, curr.get(i)));
        }
//        yValues.add(new Entry(0, 60f));
//        yValues.add(new Entry(1, 50f));
//        yValues.add(new Entry(2, 70f));
//        yValues.add(new Entry(3, 30f));
//        yValues.add(new Entry(4, 50f));
//        yValues.add(new Entry(5, 60f));
//        yValues.add(new Entry(6, 65f));

        LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");
        set1.setFillAlpha(110);
        set1.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.BLACK);

        lineChart.setData(data);
        lineChart.invalidate();
    }

}
