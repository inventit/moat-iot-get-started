/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.gae.example.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yourinventit.moat.gae.example.models.RequestHistory;
import com.yourinventit.moat.gae.example.models.ShakeEvent;
import com.yourinventit.moat.gae.example.models.SysDevice;
import com.yourinventit.moat.gae.example.models.SysDmjob;
import com.yourinventit.moat.gae.example.models.VibrationDevice;
import com.yourinventit.moat.gae.example.models.ZigBeeDevice;

/**
 * 
 * @author dbaba@yourinventit.com
 * 
 */
@SuppressWarnings("serial")
public class DashboardControllerServlet extends HttpServlet {

	static final String VIEW_PATH = "/dashboard.jsp";

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final String pathInfo = req.getPathInfo();
		if ("/delete_device".equalsIgnoreCase(pathInfo)) {
			// delete_device
			SysDevice.delete(req.getParameter("device_uid"));
		} else if ("/vibrate".equalsIgnoreCase(pathInfo)) {
			// vibrate
			VibrationDevice.stub(req.getParameter("name")).vibrateAsync();
		} else if ("/cancel".equalsIgnoreCase(pathInfo)) {
			// cancel
			SysDmjob.delete(req.getParameter("uid"));
		} else if ("/delete_all_shake_events".equalsIgnoreCase(pathInfo)) {
			// delete_all_shake_events
			ShakeEvent.delete(req.getParameter("uids"),
					req.getParameter("device_uid"));
		} else if ("/show_text_on_lcd".equalsIgnoreCase(pathInfo)) {
			// show_text_on_lcd
			ZigBeeDevice
					.stub(req.getParameter("name"), req.getParameter("uid"))
					.showTextOnLcdAsync(req.getParameter("lcd_text"));
		} else if ("/inquire_temp".equalsIgnoreCase(pathInfo)) {
			// inquire_temp
			ZigBeeDevice
					.stub(req.getParameter("name"), req.getParameter("uid"))
					.inquireTemperatureAsync();
		}
		// index
		final List<SysDevice> devices = SysDevice.find(0, -1);
		req.setAttribute("devices", devices);
		final Map<String, List<ZigBeeDevice>> zbDevices = new HashMap<String, List<ZigBeeDevice>>();
		final Map<String, List<ShakeEvent>> shakeEvents = new HashMap<String, List<ShakeEvent>>();
		for (SysDevice device : devices) {
			shakeEvents.put(device.getName(),
					ShakeEvent.find(device.getUid(), 0, -1));
			zbDevices.put(device.getName(),
					ZigBeeDevice.find(device.getUid(), 0, -1));
		}
		req.setAttribute("shake_events", shakeEvents);
		req.setAttribute("zb_devices", zbDevices);
		req.setAttribute("job_list", SysDmjob.find(0, -1));
		req.setAttribute("job_histories", RequestHistory.find());

		req.getRequestDispatcher(VIEW_PATH).forward(req, resp);
	}
}
