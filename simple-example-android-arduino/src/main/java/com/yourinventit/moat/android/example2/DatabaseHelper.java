/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.android.example2;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * 
 * @author dbaba@yourinventit.com
 * 
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	/**
	 * The name of the database file
	 */
	private static final String DATABASE_NAME = "moatexample2.db";

	/**
	 * The version of the database schema
	 */
	private static final int DATABASE_VERSION = 1;

	/**
	 * {@link RuntimeExceptionDao} for {@link ZigBeeDevice}
	 */
	private RuntimeExceptionDao<ZigBeeDevice, String> zigbeeDeviceDao = null;

	/**
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * @return the zigbeeDeviceDao
	 */
	public RuntimeExceptionDao<ZigBeeDevice, String> getZigBeeDeviceDao() {
		if (this.zigbeeDeviceDao == null) {
			// Workaround for the issue: http://stackoverflow.com/a/9590042
			final RuntimeExceptionDao dao = getRuntimeExceptionDao(ZigBeeDevice.class);
			this.zigbeeDeviceDao = (RuntimeExceptionDao<ZigBeeDevice, String>) dao;
		}
		return zigbeeDeviceDao;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase,
	 *      com.j256.ormlite.support.ConnectionSource)
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, ZigBeeDevice.class);

		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
	 *      com.j256.ormlite.support.ConnectionSource, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, ZigBeeDevice.class, true);
			onCreate(db, connectionSource);

		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper#close()
	 */
	@Override
	public void close() {
		super.close();
		this.zigbeeDeviceDao = null;
	}

}
