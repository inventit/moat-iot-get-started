/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.android.example2;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.yourinventit.dmc.api.moat.Command;

/**
 * Represents a zigbee connected constrained device such as Arduino UNO.
 * 
 * In spite of the name of the class, ZigBee, this app is also able to work with
 * Arduino directly with a USB cable as the USB serial driver is capable for
 * connecting it.
 * 
 * This model expects a device having a LCD and knocking buttons as well as a
 * ZigBee module.
 * 
 * PAN ID, Channel, Baud Rate and Destination Address to a remote Arduino device
 * (END DEVICE) are not included in this model as they are set with Digi's X-CTU
 * or mortsenso Network Manager IRON (http://www.moltosenso.com/).
 * 
 * Set the XBees as 'AT Command Mode' and their baud rate to 19200.
 * 
 * @author dbaba@yourinventit.com
 * 
 */
@DatabaseTable
public class ZigBeeDevice {

	/**
	 * {@link Logger}
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZigBeeDevice.class);

	/**
	 * The unique identifier
	 */
	@DatabaseField(id = true)
	private String uid;

	/**
	 * LCD Text
	 */
	@DatabaseField
	private String lcdText;

	/**
	 * Whether or not the button is clicked.
	 */
	@DatabaseField
	private boolean clicked;

	/**
	 * When the last clicked event arrives.
	 */
	@DatabaseField
	private long lastClicked;

	/**
	 * Celcius Temperature
	 */
	@DatabaseField
	private float temperature;

	/**
	 * Last update timestamp in milliseconds
	 */
	@DatabaseField
	private long lastUpdated;

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the lcdText
	 */
	public String getLcdText() {
		return lcdText;
	}

	/**
	 * @param lcdText
	 *            the lcdText to set
	 */
	public void setLcdText(String lcdText) {
		this.lcdText = lcdText;
	}

	/**
	 * @return the clicked
	 */
	public boolean isClicked() {
		return clicked;
	}

	/**
	 * @param clicked
	 *            the clicked to set
	 */
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}

	/**
	 * @return the lastClicked
	 */
	public long getLastClicked() {
		return lastClicked;
	}

	/**
	 * @param lastClicked
	 *            the lastClicked to set
	 */
	public void setLastClicked(long lastClicked) {
		this.lastClicked = lastClicked;
	}

	/**
	 * @return the temperature
	 */
	public float getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature
	 *            the temperature to set
	 */
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the lastUpdated
	 */
	public long getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * @param lastUpdated
	 *            the lastUpdated to set
	 */
	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * Sending showing text on LCD command to Arduino.
	 * 
	 * Format: <code>LCD:{AlphaNumeric+Symbols}\n</code>
	 * 
	 * @param context
	 */
	@Command
	public void showTextOnLcd(Map<String, Object> context) {
		final String paramText = (String) context.get("data");
		setLcdText(paramText);
		LOGGER.debug("[{}:showTextOnLcd] => [{}]", uid, paramText);
		final UsbSerialDevice usbSerialDevice = (UsbSerialDevice) context
				.get(UsbSerialDevice.class.getName());
		final String payload = "LCD:" + getLcdText() + "\n";
		final SerialInputOutputManager serialInputOutputManager = usbSerialDevice
				.getSerialInputOutputManager();
		serialInputOutputManager.writeAsync(payload.getBytes());
		final ZigBeeDeviceModelMapper mapper = (ZigBeeDeviceModelMapper) context
				.get(ZigBeeDeviceModelMapper.class.getName());
		mapper.update(this);
		((SampleApplication) context.get(SampleApplication.class.getName()))
				.appendText("[SRV]=>" + payload);
	}

	/**
	 * Inquires the current temperature to Arduino. The response will arrive
	 * asynchronously.
	 * 
	 * @param context
	 * @return
	 */
	@Command
	public void inquireTemperature(Map<String, Object> context) {
		LOGGER.debug("[{}:inquireTemperature] => [{}]", uid);
		final UsbSerialDevice usbSerialDevice = (UsbSerialDevice) context
				.get(UsbSerialDevice.class.getName());
		final String payload = "TEMP?\n";
		final SerialInputOutputManager serialInputOutputManager = usbSerialDevice
				.getSerialInputOutputManager();
		serialInputOutputManager.writeAsync(payload.getBytes());
		((SampleApplication) context.get(SampleApplication.class.getName()))
				.appendText("[SRV]=>" + payload);
	}
}
