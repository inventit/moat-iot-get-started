/*
 * LEGAL NOTICE
 *
 * Copyright (C) 2014 InventIt Inc. All rights reserved.
 *
 * This source code, product and/or document is public under licenses 
 * restricting its use, copying, distribution, and decompilation.
 * No part of this source code, product or document may be reproduced in
 * any form by any means without prior written authorization of InventIt Inc.
 * and its licensors, if any.
 *
 * InventIt Inc.
 * 9F 4-4-7 Kojimachi, Chiyoda-ku, Tokyo 102-0083
 * JAPAN
 * http://www.yourinventit.com/
 */
package com.yourinventit.moat.android.example2;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.hoho.android.usbserial.util.SerialInputOutputManager.Listener;

/**
 * 
 * @author dbaba@yourinventit.com
 * 
 */
public class UsbSerialDevice {
	/**
	 * Baud rate for ZigBee UART
	 */
	public static final int BAUD_RATE = 19200;

	/**
	 * {@link ExecutorService}
	 */
	private final ExecutorService serialInputOutputExecutor = Executors
			.newSingleThreadExecutor();

	/**
	 * {@link UsbManager}
	 */
	private final UsbManager usbManager;

	/**
	 * {@link Listener}
	 */
	private final Listener listener;

	/**
	 * {@link UsbSerialDriver}
	 */
	private UsbSerialDriver usbSerialDriver;

	/**
	 * {@link SerialInputOutputManager}
	 */
	private SerialInputOutputManager serialInputOutputManager;

	/**
	 * 
	 * @param usbManager
	 */
	public UsbSerialDevice(UsbManager usbManager, Listener listener) {
		this.usbManager = usbManager;
		this.listener = listener;
	}

	/**
	 * @return the usbSerialDriver
	 */
	public UsbSerialDriver getUsbSerialDriver() {
		return usbSerialDriver;
	}

	/**
	 * @return the serialInputOutputManager
	 */
	public SerialInputOutputManager getSerialInputOutputManager() {
		return serialInputOutputManager;
	}

	/**
	 * Inquires a USB serial driver and returns if any driver is detected.
	 * 
	 * @return true if a USB serial driver is found and is ready.
	 */
	public boolean inquireUsbSerialDriver() {
		if (this.usbSerialDriver != null) {
			return true;
		}
		UsbSerialDriver usbSerialDriver = UsbSerialProber.findFirstDevice(usbManager);
		try {
			stopSerialInputOutputManager();
			if (usbSerialDriver != null) {
				usbSerialDriver.open();
				usbSerialDriver.setParameters(BAUD_RATE,
						UsbSerialDriver.DATABITS_8, UsbSerialDriver.STOPBITS_1,
						UsbSerialDriver.PARITY_NONE);
			}
			this.usbSerialDriver = usbSerialDriver;
			startSerialInputOutputManager();
		} catch (RuntimeException exception) {
			this.usbSerialDriver = null;
			throw exception;
		} catch (IOException exception) {
			this.usbSerialDriver = null;
			startSerialInputOutputManager();
		}
		return usbSerialDriver != null;
	}

	/**
	 * setup {@link SerialInputOutputManager}
	 */
	public void startSerialInputOutputManager() {
		if (usbSerialDriver != null) {
			serialInputOutputManager = new SerialInputOutputManager(
					usbSerialDriver);
			serialInputOutputManager.setListener(listener);
			serialInputOutputExecutor.submit(serialInputOutputManager);
		}
	}

	/**
	 * {@link SerialInputOutputManager#stop()}
	 */
	public void stopSerialInputOutputManager() {
		if (serialInputOutputManager != null) {
			serialInputOutputManager.stop();
			serialInputOutputManager = null;
		}
	}

	/**
	 * Closes the current {@link UsbSerialDriver}.
	 */
	public void closeUsbSerialDriver() {
		stopSerialInputOutputManager();
		if (usbSerialDriver != null) {
			try {
				usbSerialDriver.close();
			} catch (IOException ignored) {
			} finally {
				usbSerialDriver = null;
			}
		}
	}

}
