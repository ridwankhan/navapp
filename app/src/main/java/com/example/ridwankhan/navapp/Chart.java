package com.example.ridwankhan.navapp;
//import com.example.util.PeakDetector;

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
import android.widget.TextView;

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
    //PeakDetector peakDetector;
    //LinkedList<Integer> peakIndices;
    double[] peakDataSet;
    double peakAverage = 0.0;
    double prevActivation = 0.0;
    double overallActivation = 0.0;
    //int sum = 0;

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
        calculateData();
        TextView avgActivation = (TextView) v.findViewById(R.id.avg_activation_detail);
        TextView prevAct = (TextView) v.findViewById(R.id.previous_set_activation_detail);
        TextView ovAct = (TextView) v.findViewById(R.id.overall_exercise_activation_detail);

        avgActivation.setText(String.valueOf(peakAverage));
        prevAct.setText(String.valueOf(prevActivation));
        ovAct.setText(String.valueOf(overallActivation));

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

    private void calculateData(){
        //get data for this set
        ArrayList<DataPoint> currSetData = mCallback.getCurrentSetArray();

        //calculations and data
        //for the most recent set id for this user, select the average peak amplitude,
        //previous set's average amplitude, and calculate the new overall average and set them equal
        //to peakAverage, prevActivation, overallActivation
                //        peakDataSet = new double[currSetData.size()];
                //        //prepare data for peak detection algorithm
                //        for(int i = 0; i < currSetData.size(); i++){
                //            peakDataSet[i] = (double)currSetData.get(i).getVal();
                //        }
                //
                //        peakIndices = peakDetector.findPeaks(peakDataSet, 3, 0, 0, false);
                //
                //        //average peak amplitude calculation
                //        for (int i = 0;  i < peakIndices.size(); i++){
                //            sum += peakDataSet[peakIndices.get(i)];
                //        }
                //        peakAverage = sum/(peakIndices.size());

        //chart data display
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        Description desc = new Description();
        desc.setText("Set Data");
        Log.d("DESC", desc.getText());
        lineChart.setDescription(desc);

        ArrayList<Entry> yValues = new ArrayList<>();

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
