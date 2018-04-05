package com.example.ridwankhan.navapp;

import java.util.ArrayList;

/**
 * Created by moalhayek on 4/5/18.
 */

public interface DataCommunication {
    public void setCurrentSetArray(ArrayList<Integer> curr);
    public ArrayList<Integer> getCurrentSetArray();
    public void clearCurrSetArray();    /// might not need this
}
