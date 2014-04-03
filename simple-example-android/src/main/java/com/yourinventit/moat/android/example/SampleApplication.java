/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.android.example;

import static com.yourinventit.moat.android.example.MoatIoTService.getMotionSensorListener;
import static com.yourinventit.moat.android.example.MoatIoTService.isMoationSensorListenerAssigned;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 
 * @author dbaba@yourinventit.com
 * 
 */
public class SampleApplication extends Activity {

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple);
		// Starting the MoatIoTService on this application starting up.
		startService(new Intent(this, MoatIoTService.class));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (isMoationSensorListenerAssigned()) {
			getMotionSensorListener().onResume();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (isMoationSensorListenerAssigned()) {
			getMotionSensorListener().onPause();
		}
	}

	/**
	 * When the image is tapped.
	 * 
	 * @param view
	 */
	public void tapToEnd(View view) {
		stopService(new Intent(this, MoatIoTService.class));
		finish();
	}
}
