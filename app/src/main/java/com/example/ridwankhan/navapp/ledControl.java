package com.example.ridwankhan.navapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.annotation.SuppressLint;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import java.io.IOException;

import java.util.ArrayList;
import java.util.UUID;
import android.os.Handler;

import com.example.database.*;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {link ledControl.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {link ledControl#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ledControl extends Fragment {
    Button btnOn, btnOff, btnDis, start, stop, save;
    SeekBar brightness;
    TextView lumn;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter btAdapter = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // not quite sure how this string was generated

    //added for receiving data:
    TextView sensor, txtString;
    Handler bluetoothIn;
    final int handlerState = 0;                        //used to identify handler message
    private StringBuilder recDataString = new StringBuilder();
    private ConnectedThread mConnectedThread;

    ArrayList<DataPoint> sensorVals = new ArrayList<>();
    boolean isSet = false;

    final AppDatabase db = Room.databaseBuilder(
        getActivity().getApplicationContext(),
        AppDatabase.class,
        "perfectPumpDB"
    ).build();

    int currExerciseID;
    //increments after each set, returns to 0 upon new exercise
    int currSetNumber = 0;

    DataCommunication mCallback;

    public ledControl() {
        // Required empty public constructor
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_led_control);

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
                        txtString.setText("Data Received = " + dataInPrint);

                        if (recDataString.charAt(0) == '#')                             //if it starts with # we know it is what we are looking for
                        {
                            int firstEndIndex = recDataString.indexOf("+");

                            String sensor0 = recDataString.substring(1, firstEndIndex);             //get sensor value from string between indices 1 and the +
                            String time = recDataString.substring(firstEndIndex+1, endOfLineIndex); // time stamp comes after
                            sensor.setText("Sensor Value: " + sensor0);    //update the textviews with sensor values

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

        new ConnectBT().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_led_control, container, false);

        //call the widgets
        btnOn = (Button)v.findViewById(R.id.turnOn);
        btnOff = (Button)v.findViewById(R.id.turnOff);
        btnDis = (Button)v.findViewById(R.id.disconnect);
        start = (Button)v.findViewById(R.id.start);
        stop = (Button)v.findViewById(R.id.stop);
        save = (Button)v.findViewById(R.id.save);
        brightness = (SeekBar)v.findViewById(R.id.seekBar);
        lumn = (TextView)v.findViewById(R.id.lumn);
        sensor = (TextView)v.findViewById(R.id.sens);
        txtString = (TextView)v.findViewById(R.id.txtString);

        btnOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                turnOnLed();    //method to turn on
            }

        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                turnOffLed();   //method to turn off
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

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSet();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSet();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSet();
            }
        });


        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser ){
                if (fromUser==true){
                    lumn.setText(String.valueOf(progress));
                    try{
                        btSocket.getOutputStream().write(String.valueOf(progress).getBytes());
                    }
                    catch (IOException e){

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){

            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (DataCommunication) context;
            System.out.println("called");
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

    private void turnOffLed(){
        mConnectedThread.write("0");
        msg("Turn off LED");
    }

    private void turnOnLed()
    {
        mConnectedThread.write("1");
        msg("Turn on LED");
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
        //assemble instantiation vals for ExerciseData, these should be connected to the form
        String muscleGroup = "Biceps";
        String exerciseName = "Curls";
        currExerciseID = db.exerciseDao().getHighestExerciseID()+1; //this is also a dummy val right now

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
        int weight = 20;
        int setNumber = currSetNumber;
        ArrayList<DataPoint> setDataValues = mCallback.getCurrentSetArray();

        //create SetData object
        SetData newSet = new SetData(setID, exerciseID,weight,setNumber,setDataValues);

        //insert it into db using DAO
        db.exerciseDao().insertSetData(newSet);

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(this.getId(), new Chart()).commit();
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

            mConnectedThread = new ConnectedThread(btSocket);
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
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                // if in the middle of a set, read received messages and send them to handler
                if (isSet){
                    try {
                        bytes = mmInStream.read(buffer);            //read bytes from input buffer
                        String readMessage = new String(buffer, 0, bytes);
                        // Send the obtained bytes to the UI Activity via handler
                        bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
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
    }


}
