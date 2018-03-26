package com.example.ridwankhan.navapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
//import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {



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


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
//
//    @Override
//    public void onFragmentInteraction(Uri uri){
//        //you can leave it empty
//    }

}
