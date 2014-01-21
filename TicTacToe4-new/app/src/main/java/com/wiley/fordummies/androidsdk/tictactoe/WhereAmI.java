package com.wiley.fordummies.androidsdk.tictactoe;

import java.util.List;
import android.app.Activity;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

public class WhereAmI extends Activity {
	private EditText locationEditableField = null;
	private String TAG = "WhereAmI";
	private GoogleMap mMap;
	private MapView mMapView;
	private static final String WHEREAMISTRING = "WhereAmIString";
	private String whereAmIString = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whereami);

		try {
			// Get the MapView from the XML layout and creates it
			mMapView = (MapView) findViewById(R.id.mapview);
			if (mMapView != null) {
				mMapView.onCreate(savedInstanceState);
				mMapView.onResume();
				Log.d(TAG, "mapview created");

				// Get to GoogleMap from the MapView and enable to MyLocation
				// layer
				// and button
				mMap = mMapView.getMap();
				mMap.getUiSettings().setMyLocationButtonEnabled(true);
				mMap.setMyLocationEnabled(true);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

		/*
		 * Pick the location from the EditText and zoom there on the map.
		 */
		locationEditableField = (EditText) findViewById(R.id.location);
		locationEditableField
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							v.setBackgroundColor(Color.WHITE);
							((EditText) v).setTextColor(Color.BLACK);
						}
					}
				});

		View buttonLocate = findViewById(R.id.button_locate);
		buttonLocate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// Need to call MapsInitializer before doing any
					// CameraUpdateFactory calls
					MapsInitializer.initialize(WhereAmI.this);

					String locationName = locationEditableField.getText()
							.toString();
					Geocoder gc = new Geocoder(WhereAmI.this);
					List<Address> addresses = gc.getFromLocationName(
							locationName, 1);
					LatLng latlng = new LatLng(addresses.get(0).getLatitude(),
							addresses.get(0).getLongitude());
					mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							latlng, 8));
				} catch (Exception e) {
					// do nothing
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * I didn't find these methods useful, we aren't saving/restoring any
	 * meaningful state, hence disabled them. - Swaroop 12/14/13
	 */
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (whereAmIString != null)
			outState.putString(WHEREAMISTRING, whereAmIString);
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		whereAmIString = savedInstanceState.getString(WHEREAMISTRING);
		if (whereAmIString != null) {
			// myLocationField.setText(whereAmIString);
			Toast.makeText(getApplicationContext(), whereAmIString,
					Toast.LENGTH_LONG).show();
		}
	}
}
