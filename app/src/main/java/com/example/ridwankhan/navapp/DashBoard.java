package com.example.ridwankhan.navapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
///import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;

import com.example.database.AppDatabase;
import com.example.database.ExerciseData;
import com.example.database.SetData;
import com.example.util.*;

public class DashBoard extends Fragment {
    private Context mContext;
    View view;
    ListView lvList;
    ArrayList<ListData> prevData = new ArrayList<ListData>();
    ListAdapter listAdapter;
    String Date = "";
    String Muscle = "";
    String Exercise = "";
    Integer Set = 0;
    Integer Weight = 0;
    Double Activation = 0.0;
    int SetId;
    private AppDatabase db;

    private OnFragmentInteractionListener mListener;

    public DashBoard(){
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashBoard.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoard newInstance(String param1, String param2) {
        DashBoard fragment = new DashBoard();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_dash_board, container, false);
        lvList = (ListView) view.findViewById(R.id.lvList);
        lvList.setAdapter(listAdapter);

        //get data from database on previous workouts here instead
        SetData[] setData = db.exerciseDao().getSets();

        //for each array element of set data ...

        for(int i = 0; i < setData.length; i ++){
            //get exercise data
            ExerciseData exerciseData = db.exerciseDao().getExerciseData(setData[i].getExerciseID());
            Date = setData[i].getSetTimeStamp().toString();
            Muscle = exerciseData.getMuscleGroup();
            Exercise = exerciseData.getExerciseName();
            Set = setData[i].getSetNumber();
            Weight = setData[i].getWeight();
            Activation = setData[i].getPeakAverage();
            SetId = setData[i].getSetID();

            //check the data
            if (Date.length() == 0) {
                Date = "No Date";
            }
            if (Muscle.length() == 0) {
                Muscle = "No Muscle";
            }
            if (Exercise.length() == 0) {
                Exercise = "No Exercise";
            }

            ListData mLog = new ListData();
            mLog.setDate(Date);
            mLog.setMuscle(Muscle);
            mLog.setExercise(Exercise);
            mLog.setSet(Set);
            mLog.setWeight(Weight);
            mLog.setActivation(Activation);
            mLog.setSetId(SetId);
            prevData.add(mLog);
            listAdapter.notifyDataSetChanged();
        }

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListData mLog = listAdapter.getItem(position);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(((ViewGroup)getView().getParent()).getId(), Chart.newInstance(mLog.getSetId())).commit();
                //Toast.makeText(getActivity(), "Date: " + mLog.getDate() + "  Score: " + mLog.getScore() + "  Muscle: " + mLog.getMuscle() + "  Exercise: " + mLog.getExercise() + "  Set: " + mLog.getSet() + "  Weight: " + mLog.getWeight() + "  Activation: " + mLog.getActivation(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        listAdapter = new ListAdapter(getActivity(), prevData);
        db = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                AppDatabase.class,
                "perfectPumpDB"
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listAdapter = new ListAdapter(getActivity(), prevData);
        db = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                AppDatabase.class,
                "perfectPumpDB"
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
