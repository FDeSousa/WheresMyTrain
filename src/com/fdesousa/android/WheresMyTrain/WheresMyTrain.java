package com.fdesousa.android.WheresMyTrain;

import java.util.List;

import com.fdesousa.android.WheresMyTrain.json.TflJsonReader;
import com.fdesousa.android.WheresMyTrain.json.DetailedPredictions.DetailedPredictionsStation;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class WheresMyTrain extends Activity {
	public static final String TAG = "WheresMyTrain";
	public static Resources RESOURCES;
	public static WheresMyTrain INSTANCE;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        RESOURCES = getResources();
        INSTANCE = this;
        
        TextView tvMain = (TextView) findViewById(R.id.tvMain);
        
        TflJsonReader mJsonR = new TflJsonReader();
        List<DetailedPredictionsStation> mList = mJsonR.getPredictionsDetailed('c', "bnk");
        
        tvMain.setText(mList.toString());
        //	Works. Prints out the class type and identifier of each entity in the List
        //	In this case, it's just 
        //	com.fdesousa.android.WheresMyTrain.json.DetailedPredictions.DetailedPredictionsStation@id
        //	But that's good enough for me for the minute
    }
    
    public static void displayToast(String message) {
    	Toast.makeText(INSTANCE.getApplicationContext(), message, Toast.LENGTH_LONG);
    }
}