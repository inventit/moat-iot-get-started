/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.gae.example.models;

import java.util.Date;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.yourinventit.moat.gae.example.Constants;

/**
 * 
 * @author dbaba@yourinventit.com
 * 
 */
public class ZigBeeDevice extends MoatModel {

	private final String deviceName;

	/**
	 * The unique identifier
	 */
	private String uid;

	/**
	 * LCD Text
	 */
	private String lcdText;

	/**
	 * Whether or not the button is clicked.
	 */
	private boolean clicked;

	/**
	 * When the last clicked event arrives.
	 */
	private long lastClicked;

	/**
	 * Celcius Temperature
	 */
	private float temperature;

	/**
	 * Last update timestamp in milliseconds
	 */
	private long lastUpdated;

	/**
	 * 
	 */
	public ZigBeeDevice() {
		this(null, null);
	}

	/**
	 * 
	 * @param deviceName
	 * @param uid
	 */
	public ZigBeeDevice(String deviceName, String uid) {
		this.deviceName = deviceName;
		setUid(uid);
	}

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
	 * {@inheritDoc}
	 * 
	 * @see com.yourinventit.moat.gae.example.models.MoatModel#asJson()
	 */
	@Override
	public String asJson() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.yourinventit.moat.gae.example.models.MoatModel#updateFrom(com.google.appengine.labs.repackaged.org.json.JSONObject)
	 */
	@Override
	public void updateFrom(JSONObject jsonObject) {
		setUid(jsonObject.optString("uid"));
		setClicked(jsonObject.optBoolean("clicked"));
		setLastClicked(jsonObject.optLong("lastClicked"));
		setLastUpdated(jsonObject.optLong("lastUpdated"));
		setLcdText(jsonObject.optString("lcdText"));
		setTemperature((float) jsonObject.optDouble("temperature"));
	}

	/**
	 * 
	 * @param text
	 *            to be displayed
	 * @return
	 */
	public SysDmjob showTextOnLcdAsync(String text) {
		// showTextOnLcd
		final SysDmjob entity = new SysDmjob();
		entity.setJobServiceId(Constants.getInstance().getURNShowTextOnLcd());
		entity.setName(deviceName);
		entity.setActivatedAt(new Date());
		entity.setExpiredAt(new Date(
				System.currentTimeMillis() + 15 * 60 * 1000));
		final JSONObject arguments = new JSONObject();
		try {
			arguments.put("uid", uid);
			arguments.put("text", text);
		} catch (JSONException e) {
			throw new IllegalStateException(e);
		}
		entity.setArguments(arguments);
		return SysDmjob.save(entity);
	}

	/**
	 * 
	 * @return
	 */
	public SysDmjob inquireTemperatureAsync() {
		// inquireTemperature
		final SysDmjob entity = new SysDmjob();
		entity.setJobServiceId(Constants.getInstance()
				.getURNInquireTemperature());
		entity.setName(deviceName);
		entity.setActivatedAt(new Date());
		entity.setExpiredAt(new Date(
				System.currentTimeMillis() + 15 * 60 * 1000));
		final JSONObject arguments = new JSONObject();
		try {
			arguments.put("uid", uid);
		} catch (JSONException e) {
			throw new IllegalStateException(e);
		}
		entity.setArguments(arguments);
		return SysDmjob.save(entity);
	}

	/**
	 * 
	 * @param deviceUid
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static List<ZigBeeDevice> find(String deviceUid, int offset,
			int limit) {
		return find(ZigBeeDevice.class, deviceUid, offset, limit);
	}

	/**
	 * 
	 * @param deviceName
	 * @param uid
	 * @return
	 */
	public static ZigBeeDevice stub(String deviceName, String uid) {
		return new ZigBeeDevice(deviceName, uid);
	}

}
