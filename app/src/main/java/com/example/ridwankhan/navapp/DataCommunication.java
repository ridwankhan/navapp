package com.example.ridwankhan.navapp;

import com.example.database.DataPoint;

import java.util.ArrayList;

/**
 * Created by moalhayek on 4/5/18.
 */

public interface DataCommunication {
    public void setCurrentSetArray(ArrayList<DataPoint> curr);
    public ArrayList<DataPoint> getCurrentSetArray();
    public void clearCurrSetArray();    /// might not need this
}
