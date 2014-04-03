/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.android.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;

import com.yourinventit.dmc.api.moat.Moat;

/**
 * An example for detecting the shake motion.
 * 
 * See http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it for
 * detail.
 * 
 * @author dbaba@yourinventit.com
 * 
 */
public class MotionSensorListener implements SensorEventListener {

	/**
	 * {@link Logger}
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MotionSensorListener.class);

	/**
	 * the threshold time in milliseconds to process events
	 */
	private static final long THRESHOLD_MILLIS = 500;

	/**
	 * The {@link Moat} instance
	 */
	private final Moat moat;

	/**
	 * MOAT URN Prefix
	 */
	private final String urnPrefix;

	/**
	 * {@link ShakeEventModelMapper}
	 */
	private final ShakeEventModelMapper shakeEventModelMapper;

	/**
	 * {@link SensorManager}
	 */
	private final SensorManager manager;

	/**
	 * {@link Sensor} (Accelerometer)
	 */
	private final Sensor accelerometer;

	/**
	 * the acceleration value without gravity influence.
	 */
	private float acceleration = 0.0f;

	/**
	 * the current acceleration value with gravity influence.
	 */
	private float currentAcceleration = SensorManager.GRAVITY_EARTH;

	/**
	 * the last detected acceleration value with gravity influence.
	 */
	private float lastAcceleration = SensorManager.GRAVITY_EARTH;

	/**
	 * the last update timestamp
	 */
	private long lastDetected = 0;

	/**
	 * the motion count
	 */
	private int motionCount = 0;

	/**
	 * Returns the MOT URN
	 * 
	 * @param jobServiceId
	 * @param version
	 * @return
	 */
	static String getMoatUrn(String urnPrefix, String jobServiceId,
			String version) {
		return urnPrefix + jobServiceId + ":" + version;
	}

	/**
	 * 
	 * @param moat
	 * @param urnPrefix
	 * @param databaseHelper
	 * @param context
	 */
	public MotionSensorListener(Moat moat, String urnPrefix,
			ShakeEventModelMapper shakeEventModelMapper, Context context) {
		this.manager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		this.accelerometer = manager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.moat = moat;
		this.urnPrefix = urnPrefix;
		this.shakeEventModelMapper = shakeEventModelMapper;
		onResume();
	}

	/**
	 * @return the shakeEventModelMapper
	 */
	protected ShakeEventModelMapper getShakeEventModelMapper() {
		return shakeEventModelMapper;
	}

	public void onPause() {
		manager.unregisterListener(this);
	}

	public void onResume() {
		manager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void invalidate() {
		manager.unregisterListener(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	public void onSensorChanged(SensorEvent event) {
		final long now = SystemClock.uptimeMillis();
		if (now - this.lastDetected < THRESHOLD_MILLIS) {
			return;
		}
		this.lastDetected = now;
		final float x = event.values[0];
		final float y = event.values[1];
		final float z = event.values[2];
		this.lastAcceleration = this.currentAcceleration;
		this.currentAcceleration = (float) Math
				.sqrt((double) (x * x + y * y + z * z));
		final float delta = this.currentAcceleration - this.lastAcceleration;
		// High-pass Filter (Low-cut Filter)
		this.acceleration = this.acceleration * 0.9f + delta;

		if (this.acceleration > 2) {
			// Shake!
			final ShakeEvent shakeEvent = new ShakeEvent();
			shakeEvent.setX(x);
			shakeEvent.setY(y);
			shakeEvent.setZ(z);
			shakeEvent.setAcceleration(acceleration);

			// save
			getShakeEventModelMapper().save(shakeEvent);

			// count
			this.motionCount++;
			if (this.motionCount > 3) {
				LOGGER.info("onSensorChanged => Sending the notification.");
				this.moat.sendNotification(
						getMoatUrn(urnPrefix, "ShakeEvent", "1.0"), null,
						getShakeEventModelMapper().findAndRemoveAllAsArray());
				LOGGER.info("The notification has been sent.");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor,
	 *      int)
	 */
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do Nothing
	}
}
