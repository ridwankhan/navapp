package com.example.ridwankhan.navapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
//import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.database.DataPoint;

import java.util.ArrayList;

public class MainActivityNav extends AppCompatActivity implements DataCommunication {

    private ArrayList<DataPoint> currSet;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.app.FragmentManager manager = getFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Home home = new Home();
                    manager.beginTransaction().replace(R.id.contentLayout, home, home.getTag()).commit();

                    return true;
                case R.id.navigation_dashboard:
                    Chart chart = new Chart();
                    manager.beginTransaction().replace(R.id.contentLayout, chart, chart.getTag()).commit();
                    return true;
                case R.id.navigation_notifications:
                    ledControl led = new ledControl();
                    manager.beginTransaction().replace(R.id.contentLayout, led, led.getTag()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void setCurrentSetArray(ArrayList<DataPoint> curr){

        currSet = curr;
    }

    @Override
    public ArrayList<DataPoint> getCurrentSetArray(){
        return currSet;
    }

    @Override
    public void clearCurrSetArray(){
        currSet.clear();
    }


//
//    @Override
//    public void onFragmentInteraction(Uri uri){
//        //you can leave it empty
//    }

}
