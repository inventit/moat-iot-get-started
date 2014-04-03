/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.android.example2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.yourinventit.dmc.api.moat.ModelMapper;

/**
 * 
 * @author dbaba@yourinventit.com
 * 
 */
public class ZigBeeDeviceModelMapper implements ModelMapper<ZigBeeDevice> {

	/**
	 * {@link RuntimeExceptionDao} for {@link ZigBeeDevice}
	 */
	private final RuntimeExceptionDao<ZigBeeDevice, String> dao;

	/**
	 * {@link ConnectionSource} for handling transactions
	 */
	private final ConnectionSource connectionSource;

	/**
	 * 
	 * @param dao
	 * @param connectionSource
	 */
	public ZigBeeDeviceModelMapper(
			RuntimeExceptionDao<ZigBeeDevice, String> dao,
			ConnectionSource connectionSource) {
		this.dao = dao;
		this.connectionSource = connectionSource;
	}

	/**
	 * @return the dao
	 */
	protected RuntimeExceptionDao<ZigBeeDevice, String> getDao() {
		return dao;
	}

	/**
	 * @return the connectionSource
	 */
	protected ConnectionSource getConnectionSource() {
		return connectionSource;
	}

	/**
	 * 
	 * @param callable
	 * @return
	 */
	protected <T> T doInTransaction(Callable<T> callable) {
		try {
			return TransactionManager.callInTransaction(getConnectionSource(),
					callable);
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 
	 * @param entity
	 */
	public void save(final ZigBeeDevice entity) {
		doInTransaction(new Callable<Void>() {
			public Void call() throws Exception {
				if (StringUtils.isEmpty(entity.getUid())) {
					final String uid = UUID.randomUUID().toString();
					entity.setUid(uid);
				}
				entity.setLastUpdated(System.currentTimeMillis());
				getDao().create(entity);
				return null;
			}
		});
	}

	/**
	 * Finds and removes all records
	 */
	public List<ZigBeeDevice> findAndRemoveAll() {
		return doInTransaction(new Callable<List<ZigBeeDevice>>() {
			public List<ZigBeeDevice> call() throws Exception {
				final List<ZigBeeDevice> result = getDao().queryForAll();
				getDao().deleteBuilder().delete();
				return result;
			}
		});
	}

	/**
	 * Finds and removes all records. {@link ZigBeeDevice} array is returned.
	 */
	public ZigBeeDevice[] findAndRemoveAllAsArray() {
		final List<ZigBeeDevice> result = findAndRemoveAll();
		if (result.isEmpty()) {
			return new ZigBeeDevice[0];
		} else {
			return result.toArray(new ZigBeeDevice[result.size()]);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.yourinventit.dmc.api.moat.ModelMapper#update(java.lang.Object)
	 */
	public ZigBeeDevice update(final ZigBeeDevice entity) {
		return doInTransaction(new Callable<ZigBeeDevice>() {
			public ZigBeeDevice call() throws Exception {
				entity.setLastUpdated(System.currentTimeMillis());
				getDao().update(entity);
				return entity;
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.yourinventit.dmc.api.moat.ModelMapper#updateFields(java.lang.Object,
	 *      java.lang.String[])
	 */
	public void updateFields(ZigBeeDevice entity, String[] updateFields) {
		// You can write more efficient code here...
		update(entity);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.yourinventit.dmc.api.moat.ModelMapper#remove(java.lang.String)
	 */
	public void remove(final String uid) {
		doInTransaction(new Callable<Void>() {
			public Void call() throws Exception {
				getDao().deleteById(uid);
				return null;
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.yourinventit.dmc.api.moat.ModelMapper#findByUid(java.lang.String)
	 */
	public ZigBeeDevice findByUid(String uid) {
		return getDao().queryForId(uid);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.yourinventit.dmc.api.moat.ModelMapper#add(java.lang.String)
	 */
	public ZigBeeDevice add(final String uid) {
		return doInTransaction(new Callable<ZigBeeDevice>() {
			public ZigBeeDevice call() throws Exception {
				final ZigBeeDevice event = new ZigBeeDevice();
				event.setUid(uid);
				getDao().create(event);
				return event;
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.yourinventit.dmc.api.moat.ModelMapper#findAllUids()
	 */
	public List<String> findAllUids() {
		final QueryBuilder<ZigBeeDevice, String> queryBuilder = getDao()
				.queryBuilder();
		try {
			final GenericRawResults<String[]> results = getDao().queryRaw(
					queryBuilder.selectRaw("`uid`").prepareStatementString());
			final List<String> list = new ArrayList<String>();
			for (Iterator<String[]> iterator = results.iterator(); iterator
					.hasNext();) {
				list.add(iterator.next()[0]);
			}
			return list;
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.yourinventit.dmc.api.moat.ModelMapper#count()
	 */
	public long count() {
		return getDao().countOf();
	}

}
