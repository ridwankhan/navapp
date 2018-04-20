package com.example.ridwankhan.navapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import  android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.AppDatabase;
import com.example.database.DataPoint;
import com.example.database.DataPointConverters;
import com.example.database.ExerciseData;
import com.example.database.SetData;
import com.example.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter btAdapter = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // not quite sure how this string was generated

    //added for receiving data:
    Handler bluetoothIn;
    final int handlerState = 0;                        //used to identify handler message
    private StringBuilder recDataString = new StringBuilder();
    private Home.ConnectedThread mConnectedThread;

    ArrayList<DataPoint> sensorVals = new ArrayList<>();
    boolean isSet = false;

    private AppDatabase db;

    int currExerciseID;
    //increments after each set, returns to 0 upon new exercise
    int currSetNumber = 0;

    PeakDetector peakDetector;
    LinkedList<Integer> peakIndices;
    double[] peakDataSet;
    int sum = 0;

    DataCommunication mCallback;

    public Home() {
        // Required empty public constructor
    }

    Button start, save, btnDis, setExercise;
    TextView sensor;
    EditText editWeight, editSet;
    Spinner editMuscle, editWorkout;
    //create a list of items for the spinner.
    String[] muscles = new String[]{"Bicep", "Tricep", "Chest"};
    String[] workouts = new String[]{"Bicep Curl", "Hammer Curl", "Rope Pull Down", "Bench Press"};
    //create an adapter to describe how the items are displayed, adapters are used in several places in android.
    //There are multiple variations of this, but this is the basic variant.
    ArrayAdapter<String> musclesAdapter;
    ArrayAdapter<String> workoutsAdapter;
    boolean firstClick = true;
    boolean toggleExercise = true;
    int randomNum;

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //receive the address of the bluetooth device
        Intent newint = getActivity().getIntent();
        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS);


        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg){
                if (msg.what == handlerState){      //if message is what we want
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);      //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");    //determine the end of line
                    if (endOfLineIndex > 0) {               // make sure there is data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string

                        if (recDataString.charAt(0) == '#')                             //if it starts with # we know it is what we are looking for
                        {
                            int firstEndIndex = recDataString.indexOf("+");

                            String sensor0 = recDataString.substring(1, firstEndIndex);             //get sensor value from string between indices 1 and the +
                            String time = recDataString.substring(firstEndIndex+1, endOfLineIndex); // time stamp comes after
                            sensor.setText(sensor0);    //update the textviews with sensor values

                            //let's do a little type conversion so our data point can fit in our custom class
                            int dataVal = Integer.parseInt(sensor0);
                            long timeStamp = Long.parseLong(time);
                            DataPoint newData = new DataPoint(dataVal, timeStamp);

                            //push onto the arrayList
                            sensorVals.add(newData);
                            //sensorVals.add(Integer.parseInt(sensor0));      // add the integer to the array
                        }
                        recDataString.delete(0, recDataString.length());                    //clear all string data
                        dataInPrint = "";
                    }



                }
            }
        };

        new Home.ConnectBT().execute();


    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        start = (Button) view.findViewById(R.id.start);
        start.setVisibility(View.GONE);
        btnDis = (Button)view.findViewById(R.id.disconnect);
        save = (Button)view.findViewById(R.id.save);
        save.setVisibility(View.GONE);
        sensor = (TextView)view.findViewById(R.id.sens);
        editWeight = (EditText)view.findViewById(R.id.editWeight);
        editWeight.setEnabled(false);
        editSet = (EditText)view.findViewById(R.id.editSet);
        editSet.setEnabled(false);
        editMuscle = (Spinner)view.findViewById(R.id.editMuscle);
        editWorkout = (Spinner)view.findViewById(R.id.editWorkout);
        setExercise = (Button)view.findViewById(R.id.setExercise);

        //set the spinners adapter to the previously created one.
        editMuscle.setAdapter(musclesAdapter);
        editWorkout.setAdapter(workoutsAdapter);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if (firstClick) {
                firstClick = false;
                start.setText("Stop");
                start.setBackgroundColor(Color.RED);
                startSet();
//                    Random rn = new Random();
//                    randomNum = rn.nextInt(1023 - 300 + 1) + 300;
//                    sens.setText(Integer.toString(randomNum));
            }
            else {
                start.setText("Start");
                start.setBackgroundColor(Color.GREEN);
                firstClick = true;
                stopSet();

            }



            }
        });

        setExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (toggleExercise) {
                    toggleExercise = false;
                    setExercise.setText("Back");
                    setExercise.setBackgroundColor(Color.RED);
                    editMuscle.setEnabled(false);
                    editWorkout.setEnabled(false);
                    editSet.setEnabled(true);
                    editWeight.setEnabled(true);
                    start.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);
                    startExercise();
                }
                else {
                    setExercise.setText("Start Exercise");
                    setExercise.setBackgroundColor(Color.GREEN);
                    toggleExercise = true;
                    editMuscle.setEnabled(true);
                    editWorkout.setEnabled(true);
                    editSet.setEnabled(false);
                    editWeight.setEnabled(false);
                    start.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                    stopExercise();
                }



            }
        });

        btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSet();
            }
        });

        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        db = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                AppDatabase.class,
                "perfectPumpDB"
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (DataCommunication) context;
            System.out.println("called");
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DataCommunication");
        }

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        musclesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, muscles);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        workoutsAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, workouts);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        db = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                AppDatabase.class,
                "perfectPumpDB"
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (DataCommunication) activity;
            System.out.println("called");
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DataCommunication");
        }

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        musclesAdapter = new ArrayAdapter<>(activity.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, muscles);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        workoutsAdapter = new ArrayAdapter<>(activity.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, workouts);
    }

    private void Disconnect() {
        if (btSocket != null) { //If the btSocket is busy
            try{
                btSocket.close(); //close connection
            }
            catch (IOException e){
                msg("Error");
            }
        }
        getActivity().finish();
    }

    private void startSet(){
        isSet = true;
        sensorVals.clear();
        currSetNumber++;
        msg("Set Started");
    }

    private void stopSet(){
        isSet = false;
        mCallback.setCurrentSetArray(sensorVals);
        msg("Set Stopped");
        //System.out.println(Arrays.toString(sensorVals.toArray())); this line not gonna work with the custom class
    }

    private void startExercise(){
        //db.exerciseDao().clearSetDb();
        //db.exerciseDao().clearExerciseDb();
        //assemble instantiation vals for ExerciseData, these should be connected to the form
        String muscleGroup = editMuscle.getSelectedItem().toString();
        String exerciseName = editWorkout.getSelectedItem().toString();
        currExerciseID = db.exerciseDao().getHighestExerciseID()+1; //this is also a dummy val right now
        Log.d("start exercise data: ",muscleGroup + " " + exerciseName + String.valueOf(currExerciseID));
        //create ExerciseData object
        ExerciseData currExercise = new ExerciseData(currExerciseID, muscleGroup, exerciseName);

        //insert it into db
        db.exerciseDao().insertExerciseData(currExercise);
    }

    private void stopExercise(){
        //clear out the vals
        currSetNumber = 0;
    }

    private void saveSet(){
        // add storage method here, maybe send to graphs here as well

        //assemble instantiation vals
        int exerciseID = currExerciseID;
        int setID = db.exerciseDao().getHighestSetID() + 1;
        int weight = Integer.parseInt(editWeight.getText().toString());
        double peakAverage;  //change this to be an actual calculation

        //int reps = Integer.parseInt(editReps.getText().toString());
        int setNumber = currSetNumber;
        ArrayList<DataPoint> setDataValues = mCallback.getCurrentSetArray();

        peakDataSet = new double[setDataValues.size()];
        //prepare data for peak detection algorithm
        for(int i = 0; i < setDataValues.size(); i++){
            peakDataSet[i] = (double)setDataValues.get(i).getVal();
        }

        peakIndices = peakDetector.findPeaks(peakDataSet, 3, 0, 0, false);

        //average peak amplitude calculation
        for (int i = 0;  i < peakIndices.size(); i++){
            sum += peakDataSet[peakIndices.get(i)];
        }
        peakAverage = sum/(peakIndices.size());
        Log.d("AVERAGE PEAK:", String.valueOf(peakAverage));

        String setDataValueStr = DataPointConverters.fromArrayList(setDataValues);
        Log.d("set data values: ", setDataValueStr);

        //create SetData object
        SetData newSet = new SetData(setID, exerciseID,weight,setNumber,setDataValueStr, peakAverage);

        //insert it into db using DAO
        db.exerciseDao().insertSetData(newSet);
        //String setDataValuesStr = db.exerciseDao().getSetDataValuesStr(setID);

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(this.getId(), Chart.newInstance(setID)).commit();
        //ft.replace(this.getId(), new Chart()).commit();
        msg("Set Saved");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    private void msg(String s)
    {
        Toast.makeText(getActivity().getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> //UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "Connecting...", "Please wait!!!"); // show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    btAdapter = BluetoothAdapter.getDefaultAdapter(); // get the mobile bluetooth device
                    BluetoothDevice dispositivo = btAdapter.getRemoteDevice(address); // connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);// create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect(); //start connection
                }
            }
            catch(IOException e){
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }

            mConnectedThread = new Home.ConnectedThread(btSocket);
            mConnectedThread.start();
            mConnectedThread.write("x");

            return null;
        }

        @Override
        protected void onPostExecute(Void result){  //after the doInBackground, it checks if everything went fine
            super.onPostExecute(result);

            if (!ConnectSuccess){
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                getActivity().finish();
            }
            else{
                msg("Connected.");
                isBtConnected = true;
            }

            progress.dismiss();
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            //byte[] buffer = new byte[256];
            //int bytes;

            // Keep looping to listen for received messages
            while (true) {
                // if in the middle of a set, read received messages and send them to handler

                if (isSet){
                    try {
                        if (mmInStream.available() > 0) {
                            //bytes = mmInStream.read(buffer);            //read bytes from input buffer
                            //String readMessage = new String(buffer, 0, bytes);
                            String readMessage = readUntilChar(mmInStream, '~');
                            //System.out.println(readMessage);
                            // Send the obtained bytes to the UI Activity via handler
                            //bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                            bluetoothIn.obtainMessage(handlerState, readMessage).sendToTarget();
                        }
                    } catch (IOException e) {
                        break;
                    }
                }

            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getActivity().getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                getActivity().finish();

            }
        }

        public String readUntilChar(InputStream stream, char target) {
            StringBuilder sb = new StringBuilder();

            try {
                BufferedReader buffer=new BufferedReader(new InputStreamReader(stream));

                int r;
                while ((r = buffer.read()) != -1) {
                    char c = (char) r;

                    sb.append(c);

                    if (c == target)
                        break;
                }

                System.out.println(sb.toString());
            } catch(IOException e) {
                // Error handling
            }

            return sb.toString();
        }
    }

}
