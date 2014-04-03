/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.gae.example.models;

import java.util.List;

import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * 
 * @author dbaba@yourinventit.com
 * 
 */
public class ShakeEvent extends MoatModel {

	private String uid;

	private float x;

	private float y;

	private float z;

	private float acceleration;

	private long time;

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
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * @return the acceleration
	 */
	public float getAcceleration() {
		return acceleration;
	}

	/**
	 * @param acceleration
	 *            the acceleration to set
	 */
	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
		this.time = time;
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
		setTime(jsonObject.optLong("time"));
		setX((float) jsonObject.optDouble("x"));
		setY((float) jsonObject.optDouble("y"));
		setZ((float) jsonObject.optDouble("z"));
		setAcceleration((float) jsonObject.optDouble("acceleration"));
	}

	/**
	 * 
	 * @param deviceUid
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static List<ShakeEvent> find(String deviceUid, int offset, int limit) {
		return find(ShakeEvent.class, deviceUid, offset, limit);
	}

	/**
	 * 
	 * @param uids
	 * @param deviceUid
	 */
	public static void delete(String uids, String deviceUid) {
		delete(ShakeEvent.class, uids, deviceUid);
	}

}
