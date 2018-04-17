package com.example.ridwankhan.navapp;
import com.example.util.PeakDetector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;

import com.example.database.DataPoint;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.LinkedList;

public class Chart extends Fragment {

    LineChart lineChart;
    DataCommunication mCallback;
    PeakDetector peakDetector;
    LinkedList<Integer> peakIndices;
    double[] peakDataSet = new double[mCallback.getCurrentSetArray().size()];
    int sum = 0;


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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (DataCommunication) activity;
            System.out.println("called");
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DataCommunication");
        }
    }

    private void drawLineChart(){
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        Description desc = new Description();
        desc.setText("Set Data");
        Log.d("DESC", desc.getText());
        lineChart.setDescription(desc);

        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<DataPoint> currSetData = mCallback.getCurrentSetArray();

        //prepare data for peak detection algorithm
        for(int i = 0; i < currSetData.size(); i++){
            peakDataSet[i] = (double)currSetData.get(i).getVal();
        }

        peakIndices = peakDetector.findPeaks(peakDataSet, 3, 0, 0, false);

        //average peak amplitude calculation
        for (int i = 0;  i < peakIndices.size(); i++){
            sum += peakDataSet[peakIndices.get(i)];
        }
        double averagePeakAmp = sum/(peakIndices.size());
        Log.d("AVERAGE PEAK:", String.valueOf(averagePeakAmp));

        for(int i = 0; i < currSetData.size(); i++){
            DataPoint currData = currSetData.get(i);
            int newVal = currData.getVal();
            long newTime = currData.getDataTimeStamp();

            //should probably be using newTime instead of i here
            yValues.add(new Entry(i, newVal));
        }

        LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");
        set1.setFillAlpha(110);
        //set1.setColors(ColorTemplate.COLORFUL_COLORS);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        //data.setValueTextSize(13f);
        //data.setValueTextColor(Color.BLACK);

        lineChart.setData(data);
        lineChart.invalidate();
    }

}
