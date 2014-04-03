/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.android.example;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.os.Vibrator;

import com.yourinventit.dmc.api.moat.Command;
import com.yourinventit.dmc.api.moat.ResourceType;

/**
 * The class represents a device capable for vibration.
 * 
 * @author dbaba@yourinventit.com
 * 
 */
public class VibrationDevice {

	/**
	 * {@link Logger}
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(VibrationDevice.class);

	/**
	 * The image data resource
	 */
	@ResourceType
	private Map<String, String> image;

	/**
	 * @return the image
	 */
	public Map<String, String> getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(Map<String, String> image) {
		this.image = image;
	}

	/**
	 * Vibrates the device once for a given duration in milliseconds.
	 * 
	 * @param context
	 */
	@Command
	public void vibrate(Map<String, Object> context) {
		LOGGER.info("Start vibrate!");
		LOGGER.info("[image] => {}", image);
		LOGGER.info("[context] => {}", context);
		final Context androidContext = (Context) context.get(Context.class
				.getName());
		final Vibrator vibrator = (Vibrator) androidContext
				.getSystemService(Context.VIBRATOR_SERVICE);
		final String data = (String) context.get("data");
		long[] pattern = null;
		if (StringUtils.isNotEmpty(data)) {
			try {
				final JSONArray array = new JSONArray(data);
				pattern = new long[array.length()];
				for (int i = 0; i < array.length(); i++) {
					pattern[i] = array.getLong(i);
				}
			} catch (JSONException e) {
				vibrator.vibrate(5000);
				LOGGER.info("Vibrating for 5 seconds! Malformat pattern=>{}",
						pattern);
			}
		}
		if (pattern == null || pattern.length == 0) {
			vibrator.vibrate(3000);
			LOGGER.info("Vibrating for 3 seconds!");
		} else if (pattern.length == 1) {
			vibrator.vibrate(pattern[0]);
			LOGGER.info("Vibrating for {} milliseconds!", pattern[0]);
		} else {
			vibrator.vibrate(pattern, -1);
			LOGGER.info(
					"Vibrating with the pattern (interval ms, vibrating ms, interval ms, vibrating ms,...) => {}",
					Arrays.asList(pattern));
		}
		LOGGER.info("End vibrate!");
	}
}
