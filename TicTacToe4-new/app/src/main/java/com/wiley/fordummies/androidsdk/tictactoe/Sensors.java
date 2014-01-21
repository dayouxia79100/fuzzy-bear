package com.wiley.fordummies.androidsdk.tictactoe;

import java.util.Hashtable;
import java.util.List;

import com.wiley.fordummies.androidsdk.tictactoe.R;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.lang.Math;


public class Sensors extends Activity implements SensorEventListener, OnClickListener {
	/** Called when the activity is first created. */
	private SensorManager sensorManager;
	private List<Sensor> sensorList;
	private TextView listSensorsView=null;
	private Hashtable<String, float[]> lastSensorValues= new Hashtable<String, float[]>();
    
	private static final String LOGTAG="Sensors";
	private static final float TOLERANCE = (float) 10.0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensors);
		listSensorsView = (TextView) findViewById(R.id.sensorsListTextView);
		Button buttonExit = (Button) findViewById(R.id.buttonSensorsExit);
		buttonExit.setOnClickListener(this);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
	    StringBuilder sensorDescriptions = new StringBuilder();
	    int count=0;
	    for (Sensor sensor : sensorList) {
	    	
	    	String sensorName = sensor.getName();
	    	sensorDescriptions.append(count+". " + sensorName + "\n" + " " +
	    			                  " Ver :" + sensor.getVersion() +
	    			                  " Range: " + sensor.getMaximumRange() + 
	    			                  " Power: " + sensor.getPower() + 
	    			                  " Res: " + sensor.getResolution());
	    	sensorDescriptions.append("\n");
			count++;
	    }
	    listSensorsView.setText(sensorDescriptions);
	}
	
	public void onSensorChanged(SensorEvent event) {
		String sensorName = event.sensor.getName();
		String lastValueString = "No previous value";
		String sensorEventString = sensorEventToString(event);
		float percentageChange = (float)1000.0 + TOLERANCE; // Some value greater than tolerance
		float distanceOfLastValue = (float)0.0;
		float distanceOfThisValue = (float)0.0;
		float change = (float)0.0;
		
		float[] lastValue = lastSensorValues.get(sensorName);
		lastSensorValues.remove(sensorName); // Hash table is "open" and can store multiple entries for the same key
		lastSensorValues.put(sensorName, event.values.clone()); // update the value
		if (lastValue != null){
			// Compute distance of new value, change and percentage change
			StringBuilder builder= new StringBuilder ();
			distanceOfLastValue = (float)0.0;
			for (int i = 0; i < event.values.length; i++){
				try{
				distanceOfLastValue = distanceOfLastValue + (float) Math.pow (lastValue[i], 2);
				distanceOfThisValue = distanceOfThisValue + (float) Math.pow (event.values[i], 2);
				change = change + (float) Math.pow ((event.values[i]-lastValue[i]), 2);
				builder.append("   [");
		    	builder.append(i);
		    	builder.append("] = ");
		    	builder.append(lastValue[i]);
				} catch (Exception e){
					Log.e(LOGTAG, e.getMessage());
				}
			}
			lastValueString = builder.toString();
			change = (float) Math.sqrt(change);
			distanceOfLastValue = (float) Math.sqrt(distanceOfLastValue);
			distanceOfThisValue = (float) Math.sqrt(distanceOfThisValue);

			percentageChange = (float)1000.0 + TOLERANCE; // large value > tolerance
			if (distanceOfLastValue != 0.0) percentageChange = change*(float)100.0/distanceOfLastValue;
			else if (distanceOfThisValue != 0.0) percentageChange = change*(float)100.0/distanceOfThisValue;
			else percentageChange = (float) 0.0; // both distances are zero

		}
		Log.d(LOGTAG, "--- EVENT Raw Values ---\n" + sensorName + "\n" +
			      "Distance  Last= >" + distanceOfLastValue + "<\n" + 
			      "Distance  This= >" + distanceOfThisValue + "<\n" + 
		    	  "Change = >" + change + "<\n" +
	              "Percent = >" + percentageChange + "%\n" + 
	              "Last value = " + lastValueString + "<\n" +
		          sensorEventString);
		if (lastValue == null ||
			percentageChange > TOLERANCE){
		    Log.d(LOGTAG+sensorName, 
		    	  "--- Event Changed --- \n" + 
		    	  "Change = >" + change + "<\n" +
			      "Percent = >" + percentageChange + "%\n" + 
			      sensorEventString);
		}
	}
	
	private String sensorEventToString(SensorEvent event){
	    StringBuilder builder = new StringBuilder();
	    builder.append("Sensor: ");
	    builder.append(event.sensor.getName());
	    builder.append("\nAccuracy: ");
	    builder.append(event.accuracy);
	    builder.append("\nTimestamp: ");
	    builder.append(event.timestamp);
	    builder.append("\nValues:\n");
	    for (int i = 0; i < event.values.length; i++) {
	    	builder.append("   [");
	    	builder.append(i);
	    	builder.append("] = ");
	    	builder.append(event.values[i]);
	      }	
	    builder.append("\n");
	    return builder.toString();
	}
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		      // TODO Auto-generated method stub
	}

	public void onClick(View v) {

		switch(v.getId()){
		    case R.id.buttonSensorsExit:
				this.finish();
			    break;
		}
	
	}
	@Override
	protected void onResume() {
	      super.onResume();
	      // Start listening to sensor updates
	      for (Sensor sensor : sensorList) {
	    	  sensorManager.registerListener(this, sensor,
	                 SensorManager.SENSOR_DELAY_NORMAL);
	      }
	}

	@Override
	protected void onPause() {
	   Log.d(LOGTAG, "Entering onPause");
	   super.onPause();
	   // Stop updates when paused
	   sensorManager.unregisterListener(this);
	   Log.d(LOGTAG, "Leaving onPause");
	}

}
	