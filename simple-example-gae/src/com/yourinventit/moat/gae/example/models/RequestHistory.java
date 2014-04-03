/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.gae.example.models;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * 
 * @author dbaba@yourinventit.com
 * 
 */
@PersistenceCapable
public class RequestHistory extends MoatModel {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.UUIDSTRING)
	private String uid;

	@Persistent
	private String deviceId;

	@Persistent
	private String name;

	@Persistent
	private String status;

	@Persistent
	private String jobServiceId;

	@Persistent
	private int sessionId;

	@Persistent
	private String argumentsString;

	@Persistent
	private String description;

	@Persistent
	private Date createdAt;

	@Persistent
	private Date activatedAt;

	@Persistent
	private Date startedAt;

	@Persistent
	private Date endedAt;

	@Persistent
	private Date expiredAt;

	@Persistent
	private String notificationType;

	@Persistent
	private String notificationUri;

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
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the jobServiceId
	 */
	public String getJobServiceId() {
		return jobServiceId;
	}

	/**
	 * @param jobServiceId
	 *            the jobServiceId to set
	 */
	public void setJobServiceId(String jobServiceId) {
		this.jobServiceId = jobServiceId;
	}

	/**
	 * @return the sessionId
	 */
	public int getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the argumentsString
	 */
	public String getArgumentsString() {
		return argumentsString;
	}

	/**
	 * @param argumentsString
	 *            the argumentsString to set
	 */
	public void setArgumentsString(String argumentsString) {
		this.argumentsString = argumentsString;
	}

	/**
	 * 
	 * @return
	 */
	public JSONObject getArguments() {
		if (argumentsString != null && argumentsString.length() > 0) {
			try {
				return new JSONObject(argumentsString);
			} catch (JSONException e) {
				throw new IllegalArgumentException(e);
			}
		} else {
			return new JSONObject();
		}
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the activatedAt
	 */
	public Date getActivatedAt() {
		return activatedAt;
	}

	/**
	 * @param activatedAt
	 *            the activatedAt to set
	 */
	public void setActivatedAt(Date activatedAt) {
		this.activatedAt = activatedAt;
	}

	/**
	 * @return the startedAt
	 */
	public Date getStartedAt() {
		return startedAt;
	}

	/**
	 * @param startedAt
	 *            the startedAt to set
	 */
	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	/**
	 * @return the endedAt
	 */
	public Date getEndedAt() {
		return endedAt;
	}

	/**
	 * @param endedAt
	 *            the endedAt to set
	 */
	public void setEndedAt(Date endedAt) {
		this.endedAt = endedAt;
	}

	/**
	 * @return the expiredAt
	 */
	public Date getExpiredAt() {
		return expiredAt;
	}

	/**
	 * @param expiredAt
	 *            the expiredAt to set
	 */
	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	/**
	 * @return the notificationType
	 */
	public String getNotificationType() {
		return notificationType;
	}

	/**
	 * @param notificationType
	 *            the notificationType to set
	 */
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	/**
	 * @return the notificationUri
	 */
	public String getNotificationUri() {
		return notificationUri;
	}

	/**
	 * @param notificationUri
	 *            the notificationUri to set
	 */
	public void setNotificationUri(String notificationUri) {
		this.notificationUri = notificationUri;
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
	 * 
	 * @param jsonObject
	 */
	@Override
	public void updateFrom(JSONObject jsonObject) {
		setArgumentsString(jsonObject.optString("arguments"));

		setUid(jsonObject.optString("uid"));
		setDeviceId(jsonObject.optString("deviceId"));
		setName(jsonObject.optString("name"));
		setStatus(jsonObject.optString("status"));
		setJobServiceId(jsonObject.optString("jobServiceId"));
		setSessionId(jsonObject.optInt("sessionId"));
		setDescription(jsonObject.optString("description"));
		setNotificationType(jsonObject.optString("notificationType"));
		setNotificationUri(jsonObject.optString("notificationUri"));

		final DateFormat rfc2822 = getRfc2822Format();
		try {
			setCreatedAt(toDate(jsonObject.optString("createdAt"), rfc2822));
			setActivatedAt(toDate(jsonObject.optString("activatedAt"), rfc2822));
			setStartedAt(toDate(jsonObject.optString("startedAt"), rfc2822));
			setEndedAt(toDate(jsonObject.optString("endedAt"), rfc2822));
			setExpiredAt(toDate(jsonObject.optString("expiredAt"), rfc2822));
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<RequestHistory> find() {
		final PersistenceManager persistenceManager = PMF.get()
				.getPersistenceManager();
		try {
			final Query query = persistenceManager
					.newQuery(RequestHistory.class);
			query.setOrdering("endedAt descending");
			return (List<RequestHistory>) query.execute();
		} finally {
			persistenceManager.close();
		}
	}

	/**
	 * 
	 */
	public static long deleteAll() {
		final PersistenceManager persistenceManager = PMF.get()
				.getPersistenceManager();
		try {
			// remove all entities
			return persistenceManager.newQuery().deletePersistentAll();
		} finally {
			persistenceManager.close();
		}
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 */
	public static RequestHistory save(InputStream inputStream) {
		final PersistenceManager persistenceManager = PMF.get()
				.getPersistenceManager();
		try {
			final RequestHistory requestHistory = new RequestHistory();
			final JSONObject jsonObject = fromInputStream(inputStream);
			requestHistory.updateFrom(jsonObject);
			return persistenceManager.makePersistent(requestHistory);
		} finally {
			persistenceManager.close();
		}
	}
}
