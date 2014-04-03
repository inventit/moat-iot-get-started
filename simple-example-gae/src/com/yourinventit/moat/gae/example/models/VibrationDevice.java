/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.gae.example.models;

import java.util.Date;
import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.yourinventit.moat.gae.example.Constants;

/**
 * 
 * @author dbaba@yourinventit.com
 * 
 */
public class VibrationDevice extends MoatModel {

	private final String deviceName;

	private String uid;

	private JSONObject image = new JSONObject();

	/**
	 * 
	 */
	public VibrationDevice() {
		this(null);
	}

	/**
	 * 
	 * @param deviceName
	 */
	public VibrationDevice(String deviceName) {
		this.deviceName = deviceName;
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
	 * @return the image
	 */
	public JSONObject getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(JSONObject image) {
		this.image = image;
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
		setImage(jsonObject.optJSONObject("image"));
	}

	/**
	 * 
	 * @return
	 */
	public SysDmjob vibrateAsync() {
		// vibrate
		final SysDmjob entity = new SysDmjob();
		entity.setJobServiceId(Constants.getInstance().getURNVibrateDevice());
		entity.setName(deviceName);
		entity.setActivatedAt(new Date());
		entity.setExpiredAt(new Date(
				System.currentTimeMillis() + 15 * 60 * 1000));
		//
		// See http://developer.android.com/reference/android/os/Vibrator.html#vibrate(long[], int)
		// The first value indicates the number of milliseconds to wait before turning the vibrator on.
		// The next value indicates the number of milliseconds for which to keep the vibrator on before turning it off.
		// Subsequent values alternate between durations in milliseconds to turn the vibrator off or to turn the vibrator on.
		//
		// S-O-S * 2
		final JSONObject arguments = new JSONObject();
		try {
			arguments.put("options", new JSONArray("[0, 0, 0"
					// vibrate then wait
					+ ",200, 200, 200, 200, 200" + ",500"
					+ ",500, 200, 500, 200, 500" + ",500"
					+ ",200, 200, 200, 200, 200" + ",1000"
					+ ",200, 200, 200, 200, 200" + ",500"
					+ ",500, 200, 500, 200, 500" + ",500"
					+ ",200, 200, 200, 200, 200" + "]"));
		} catch (JSONException e) {
			throw new IllegalStateException(e);
		}
		entity.setArguments(arguments);
		return SysDmjob.save(entity);
	}

	/**
	 * 
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static List<VibrationDevice> find(int offset, int limit) {
		return find(VibrationDevice.class, offset, limit);
	}

	/**
	 * 
	 * @param deviceName
	 * @return
	 */
	public static VibrationDevice stub(String deviceName) {
		return new VibrationDevice(deviceName);
	}
}
