/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.gae.example.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yourinventit.moat.gae.example.models.RequestHistory;

/**
 * 
 * @author dbaba@yourinventit.com
 * 
 */
@SuppressWarnings("serial")
public class RequestHistoriesControllerServlet extends HttpServlet {

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final String pathInfo = req.getPathInfo();
		if ("/create".equalsIgnoreCase(pathInfo)) {
			// create
			RequestHistory.save(req.getInputStream());
			
		} else if ("/delete_all".equalsIgnoreCase(pathInfo)) {
			// delete_all
			RequestHistory.deleteAll();
			resp.sendRedirect("/dashboard/index");
		}
	}
}
