/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.android.example2;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager.Listener;
import com.yourinventit.dmc.api.moat.ContextFactory;
import com.yourinventit.dmc.api.moat.Moat;

/**
 * 
 * @author dbaba@yourinventit.com
 * 
 */
public class SampleApplication extends Activity implements Listener {

	/**
	 * {@link Logger}
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SampleApplication.class);

	/**
	 * Threshold to detect if the button is kept pressed.
	 */
	static final long PRESS_THRESHOLD_MS = 1500;

	/**
	 * The uid value for the connected zigbee device object.
	 */
	static final String ZIGBEE_DEVICE_UID = "8251370d-caeb-4b5b-9510-de17515b48d4";

	/**
	 * {@link ContextFactory}
	 */
	private static ContextFactory contextFactory;

	/**
	 * {@link ZigBeeDeviceModelMapper}
	 */
	private static ZigBeeDeviceModelMapper zigBeeDeviceModelMapper;

	/**
	 * {@link Moat}
	 */
	private static Moat moat;

	/**
	 * URN prefix
	 */
	private static String urnPrefix;

	/**
	 * {@link UsbSerialDevice}
	 */
	private UsbSerialDevice usbSerialDevice;

	/**
	 * The incoming data buffer
	 */
	private ByteArrayOutputStream in;

	/**
	 * {@link ExecutorService}
	 */
	private final ScheduledExecutorService clickEventMonitorExecutor = Executors
			.newScheduledThreadPool(1);

	/**
	 * {@link ScrollView}
	 */
	private ScrollView scrollView;

	/**
	 * {@link TextView}
	 */
	private TextView textView;

	/**
	 * (static)
	 * 
	 * @return the contextFactory
	 */
	static ContextFactory getContextFactory() {
		return contextFactory;
	}

	/**
	 * (static)
	 * 
	 * @param zigBeeDeviceModelMapper
	 *            the zigBeeDeviceModelMapper to set
	 */
	static void setZigBeeDeviceModelMapper(
			ZigBeeDeviceModelMapper zigBeeDeviceModelMapper) {
		SampleApplication.zigBeeDeviceModelMapper = zigBeeDeviceModelMapper;
	}

	/**
	 * (static)
	 * 
	 * @return the zigBeeDeviceModelMapper
	 */
	static ZigBeeDeviceModelMapper getZigBeeDeviceModelMapper() {
		return zigBeeDeviceModelMapper;
	}

	/**
	 * (static)
	 * 
	 * @param moat
	 *            the moat to set
	 */
	static void setMoat(Moat moat) {
		SampleApplication.moat = moat;
	}

	/**
	 * (static)
	 * 
	 * @return the moat
	 */
	static Moat getMoat() {
		return moat;
	}

	/**
	 * (static)
	 * 
	 * @param urnPrefix
	 *            the urnPrefix to set
	 */
	static void setUrnPrefix(String urnPrefix) {
		SampleApplication.urnPrefix = urnPrefix;
	}

	/**
	 * (static)
	 * 
	 * @return the urnPrefix
	 */
	static String getUrnPrefix() {
		return urnPrefix;
	}

	/**
	 * @return the usbSerialDevice
	 */
	UsbSerialDevice getUsbSerialDevice() {
		return usbSerialDevice;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Widgets
		setContentView(R.layout.simple);
		scrollView = (ScrollView) findViewById(R.id.scroll_view);
		textView = (TextView) findViewById(R.id.scroll_content);

		// ContextFactory
		contextFactory = new AndroidContextFactory(this);
		// Starting the MoatIoTService on this application starting up.
		startService(new Intent(this, MoatIoTService.class));

		// USB Serial Device
		usbSerialDevice = new UsbSerialDevice(
				(UsbManager) getSystemService(Context.USB_SERVICE), this);

		// Timer task
		clickEventMonitorExecutor.scheduleAtFixedRate(new Runnable() {

			/**
			 * Monitors if the button is pressed longer then PRESS_THRESHOLD_MS.
			 * 
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				if (isNotInitialized()) {
					return;
				}
				try {
					final ZigBeeDevice zigBeeDevice = getZigBeeDeviceModelMapper()
							.findByUid(ZIGBEE_DEVICE_UID);
					if (zigBeeDevice == null) {
						return;
					}
					if (zigBeeDevice.isClicked()) {
						final long now = System.currentTimeMillis();
						if (now - zigBeeDevice.getLastClicked() > PRESS_THRESHOLD_MS) {
							LOGGER.info("[clickEventMonitorExecutor] Button state changed => clicked:false");
							// The button is NOT pressed.
							zigBeeDevice.setClicked(false);
							getZigBeeDeviceModelMapper().update(zigBeeDevice);
							getMoat().sendNotification(
									MoatIoTService.getMoatUrn(getUrnPrefix(),
											"ShakeEvent", "1.0"), null,
									new Object[] { zigBeeDevice });
						}
					}
				} catch (IllegalStateException ignored) {
					// com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper.getConnectionSource
					// may throw IllegalStateException
				}
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see android.app.Activity#onResume()
	 */
	protected void onResume() {
		super.onResume();
		if (usbSerialDevice.inquireUsbSerialDriver()) {
			final UsbDevice usbDevice = usbSerialDevice.getUsbSerialDriver()
					.getDevice();
			if (textView.getText() == null || textView.getText().length() == 0) {
				textView.setText("USB Serial Device is detected => { VID:"
						+ usbDevice.getVendorId() + ", PID:"
						+ usbDevice.getProductId() + "}\n");
			}
		} else {
			textView.setText("USB Serial Device is missing.");
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	protected void onDestroy() {
		super.onDestroy();
		usbSerialDevice.closeUsbSerialDriver();
		stopService(new Intent(this, MoatIoTService.class));
	}

	/**
	 * Returns the serial input buffer.
	 * 
	 * @return the in
	 */
	ByteArrayOutputStream getIn() {
		if (in == null) {
			in = new ByteArrayOutputStream();
		}
		return in;
	}

	/**
	 * Resets the serial input buffer.
	 */
	void resetIn() {
		in = null;
	}

	/**
	 * Whether or not the serial input buffer is empty.
	 * 
	 * @return
	 */
	boolean isInEmpty() {
		return in == null || in.size() == 0;
	}

	/**
	 * Whether or not the data is a message delimiter.
	 * 
	 * @param c
	 * @return
	 */
	static boolean isDelimiter(int c) {
		return c == '\n' || c == '\r';
	}

	/**
	 * Whether or not the application is initialzied.
	 * 
	 * @return
	 */
	static boolean isNotInitialized() {
		return getZigBeeDeviceModelMapper() == null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.yourinventit.moat.android.example2.SerialInputOutputManager.Listener#onNewData(byte[])
	 */
	@Override
	public void onNewData(byte[] data) {
		final String dataString = new String(data);
		LOGGER.info("[onNewData:IN] Read bytes: {}, Text: {}", data.length,
				dataString);
		String additionalInfo = "Nothing Done.\n";
		for (int i = 0; i < data.length; i++) {
			if (isDelimiter(data[i])) {
				if (isInEmpty() || isNotInitialized()) {
					continue;
				}
				final byte[] payload = getIn().toByteArray();
				resetIn();
				final ZigBeeDevice zigBeeDevice = getZigBeeDeviceModelMapper()
						.findByUid(ZIGBEE_DEVICE_UID);
				LOGGER.info(
						"[onNewData] ZigBee Device data is missing. Message => {}",
						HexDump.dumpHexString(payload));
				if (zigBeeDevice == null) {
					continue;
				}
				final String message = new String(payload);
				LOGGER.info("[onNewData] message => {}", message);
				if (message.startsWith("TEMP:")) {
					additionalInfo += performTemperatureResponse(zigBeeDevice,
							message);
				} else if ("CLICKED:TRUE".equals(message)) {
					additionalInfo += performClicked(zigBeeDevice);
				} else if (message.endsWith(":OK\n")) {
					LOGGER.warn("[onNewData] Response => {}", message);
				} else {
					LOGGER.warn("[onNewData] Unknown message. Ignored.");
				}
			} else {
				getIn().write(data[i]);
			}
		}
		final String line = additionalInfo;
		appendText("[ZB] => " + line);
	}

	/**
	 * 
	 * @param line
	 */
	public void appendText(final String line) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView.append(line);
				scrollView.smoothScrollTo(0, textView.getBottom());
			}
		});
	}

	/**
	 * 
	 * @param zigBeeDevice
	 * @param message
	 */
	private String performTemperatureResponse(ZigBeeDevice zigBeeDevice,
			String message) {
		final float t = Float.valueOf(message.substring(5));
		zigBeeDevice.setTemperature(t);
		getZigBeeDeviceModelMapper().update(zigBeeDevice);
		getMoat().sendNotificationAsync(
				MoatIoTService.getMoatUrn(getUrnPrefix(), "ShakeEvent", "1.0"),
				null, new Object[] { zigBeeDevice });
		return "Temp(C):" + t + " => Notified to Server!\n";
	}

	/**
	 * 
	 * @param zigBeeDevice
	 */
	private String performClicked(ZigBeeDevice zigBeeDevice) {
		final boolean isClicked = zigBeeDevice.isClicked();
		zigBeeDevice.setClicked(true);
		zigBeeDevice.setLastClicked(System.currentTimeMillis());
		getZigBeeDeviceModelMapper().update(zigBeeDevice);
		if (isClicked) {
			return "Clicked => Last Clicked Time Has Been Updated.\n";
		} else {
			LOGGER.info("[performClicked] Button state changed => clicked:true");
			getMoat().sendNotificationAsync(
					MoatIoTService.getMoatUrn(getUrnPrefix(), "ShakeEvent",
							"1.0"), null, new Object[] { zigBeeDevice });
			return "Clicked => Notified to Server!\n";
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.yourinventit.moat.android.example2.SerialInputOutputManager.Listener#onRunError(java.lang.Exception)
	 */
	@Override
	public void onRunError(Exception e) {
		LOGGER.warn("Exception detected. Restart SerialInputOutputManager.", e);
		usbSerialDevice.closeUsbSerialDriver();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ignored) {
		}
		usbSerialDevice.inquireUsbSerialDriver();
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
