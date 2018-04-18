package com.example.util;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.ridwankhan.navapp.R;


public class ListViewHolder extends LinearLayout {
    Context mContext;
    ListData mLog;

    public ListViewHolder(Context context) {
        super(context);
        mContext = context;
        setup();
    }

    public ListViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setup();
    }

    private void setup() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card_model, this);
    }

    public void setLog(ListData log) {

        mLog = log;

        TextView tvDate = (TextView) findViewById(R.id.date_detail);
        tvDate.setText(mLog.getDate() + "");

        TextView tvScore = (TextView) findViewById(R.id.score_detail);
        tvScore.setText(mLog.getScore() + "");

        TextView tvMuscle = (TextView) findViewById(R.id.muscle_detail);
        tvMuscle.setText(mLog.getMuscle() + "");

        TextView tvExercise = (TextView) findViewById(R.id.exercise_detail);
        tvExercise.setText(mLog.getExercise() + "");

        TextView tvSet = (TextView) findViewById(R.id.set_detail);
        tvSet.setText(mLog.getSet() + "");

        TextView tvWeight = (TextView) findViewById(R.id.weight_detail);
        tvWeight.setText(mLog.getWeight() + "");

        TextView tvActivation = (TextView) findViewById(R.id.activation_detail);
        tvActivation.setText(mLog.getActivation() + "");
    }
}
