/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.android.example2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import com.yourinventit.dmc.api.moat.ContextFactory;
import com.yourinventit.dmc.api.moat.DoneCallback;
import com.yourinventit.dmc.api.moat.Moat;
import com.yourinventit.dmc.api.moat.android.MoatAndroidFactory;
import com.yourinventit.dmc.api.moat.android.MoatInitResult;

/**
 * 
 * @author dbaba@yourinventit.com
 * 
 */
public class MoatIoTService extends Service {

	/**
	 * {@link Logger}
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MoatIoTService.class);

	/**
	 * {@link DatabaseHelper}
	 */
	private DatabaseHelper databaseHelper;

	/**
	 * {@link Moat}
	 */
	private Moat moat;

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// This service doesn't have any IBinder instance.
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		LOGGER.info("onCreate(): Preparing the token file and the enrollment info.");
		super.onCreate();

		// Just for code readability
		final Context context = this;

		// Creating a new DatabaseHelper
		databaseHelper = new DatabaseHelper(context);
		LOGGER.info("onCreate(): DatabaseHelper has been initialized.");

		byte[] token = null;
		try {
			// Loading a security token signed twice,
			// by ServiceSync Sandbox Server and your own.
			token = toByteArray(getAssets().open("moat/signed.bin"));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

		// Initializing MOAT object asynchronously
		MoatAndroidFactory.getInstance().initMoat(token, context)
				.then(new DoneCallback<MoatInitResult, Throwable>() {
					/**
					 * This method will be invoked when the initialization is
					 * successfully terminated.
					 */
					public void onSuccess(MoatInitResult result) {
						LOGGER.info("onCreate(): OK! MOAT initialization is successful.");
						if (databaseHelper == null) {
							throw new IllegalStateException(
									"Inconsistent State. Re-start the app.");
						}
						final Moat moat = result.getMoat();
						final String urnPrefix = result.getUrnPrefix();

						// Holding the passed moat instance
						MoatIoTService.this.moat = moat;

						// AndroidContextFactory
						final ContextFactory contextFactory = SampleApplication
								.getContextFactory();

						// Registering ZigBeeDevice model
						final ZigBeeDeviceModelMapper zigBeeDeviceModelMapper = new ZigBeeDeviceModelMapper(
								databaseHelper.getZigBeeDeviceDao(),
								databaseHelper.getConnectionSource());
						moat.registerModel(ZigBeeDevice.class,
								zigBeeDeviceModelMapper, contextFactory);
						ZigBeeDevice zigBeeDevice = zigBeeDeviceModelMapper
								.findByUid(SampleApplication.ZIGBEE_DEVICE_UID);
						if (zigBeeDevice == null) {
							zigBeeDeviceModelMapper
									.add(SampleApplication.ZIGBEE_DEVICE_UID);
						}
						LOGGER.info("onCreate(): ZigBeeDevice has been registered.");

						SampleApplication.setMoat(moat);
						SampleApplication.setUrnPrefix(urnPrefix);
						SampleApplication
								.setZigBeeDeviceModelMapper(zigBeeDeviceModelMapper);
						LOGGER.info("onCreate(): OK. I'm ready.");
						Looper.prepare();
						new Handler(getMainLooper()).post(new Runnable() {
							public void run() {
								Toast.makeText(getApplicationContext(),
										"OK. Successfully connected to GW.",
										Toast.LENGTH_LONG).show();
							}
						});
					}

					/**
					 * This method will be invoked when unexpected exception
					 * occurs during initialization process.
					 * 
					 * @param cause
					 */
					public void onFailure(final Throwable cause) {
						LOGGER.error("onCreate(): ERROR!!!!!!!!!!.", cause);
						Looper.prepare();
						new Handler(getMainLooper()).post(new Runnable() {
							public void run() {
								Toast.makeText(
										getApplicationContext(),
										"Exception Occured. Failed to initMoat!:"
												+ cause.getMessage(),
										Toast.LENGTH_LONG).show();
							}
						});
					}
				});

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		LOGGER.info("onDestroy(): Terminating this instance.");
		super.onDestroy();

		// Shutdown the databaseHelper
		databaseHelper.close();
		databaseHelper = null;

		final MoatAndroidFactory factory = MoatAndroidFactory.getInstance();
		if (factory.isValid(moat)) {
			// Remove unused model descriptors
			moat.removeModel(ZigBeeDevice.class);
		}
		factory.destroyMoat(moat);
		moat = null;
		LOGGER.info("onDestroy(): Done!");
	}

	/**
	 * Reads an {@link InputStream} and returns as bytes.
	 * 
	 * @param inputStream
	 * @return
	 */
	static byte[] toByteArray(InputStream inputStream) {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final byte[] buffer = new byte[1024];
		int len = 0;
		try {
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			return outputStream.toByteArray();
		} catch (IOException exception) {
			throw new IllegalStateException(exception);
		}
	}

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
}
