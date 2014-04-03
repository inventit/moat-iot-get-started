/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.android.example;

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
public class ShakeEventModelMapper implements ModelMapper<ShakeEvent> {

	/**
	 * {@link RuntimeExceptionDao} for {@link ShakeEvent}
	 */
	private final RuntimeExceptionDao<ShakeEvent, String> dao;

	/**
	 * {@link ConnectionSource} for handling transactions
	 */
	private final ConnectionSource connectionSource;

	/**
	 * 
	 * @param dao
	 * @param connectionSource
	 */
	public ShakeEventModelMapper(RuntimeExceptionDao<ShakeEvent, String> dao,
			ConnectionSource connectionSource) {
		this.dao = dao;
		this.connectionSource = connectionSource;
	}

	/**
	 * @return the dao
	 */
	protected RuntimeExceptionDao<ShakeEvent, String> getDao() {
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
	public void save(final ShakeEvent entity) {
		doInTransaction(new Callable<Void>() {
			public Void call() throws Exception {
				if (StringUtils.isEmpty(entity.getUid())) {
					final String uid = UUID.randomUUID().toString();
					entity.setUid(uid);
				}
				entity.setTime(System.currentTimeMillis());
				getDao().create(entity);
				return null;
			}
		});
	}

	/**
	 * Finds and removes all records
	 */
	public List<ShakeEvent> findAndRemoveAll() {
		return doInTransaction(new Callable<List<ShakeEvent>>() {
			public List<ShakeEvent> call() throws Exception {
				final List<ShakeEvent> result = getDao().queryForAll();
				getDao().deleteBuilder().delete();
				return result;
			}
		});
	}

	/**
	 * Finds and removes all records. {@link ShakeEvent} array is returned.
	 */
	public ShakeEvent[] findAndRemoveAllAsArray() {
		final List<ShakeEvent> result = findAndRemoveAll();
		if (result.isEmpty()) {
			return new ShakeEvent[0];
		} else {
			return result.toArray(new ShakeEvent[result.size()]);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.yourinventit.dmc.api.moat.ModelMapper#update(java.lang.Object)
	 */
	public ShakeEvent update(final ShakeEvent entity) {
		return doInTransaction(new Callable<ShakeEvent>() {
			public ShakeEvent call() throws Exception {
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
	public void updateFields(ShakeEvent entity, String[] updateFields) {
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
	public ShakeEvent findByUid(String uid) {
		return getDao().queryForId(uid);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.yourinventit.dmc.api.moat.ModelMapper#add(java.lang.String)
	 */
	public ShakeEvent add(final String uid) {
		return doInTransaction(new Callable<ShakeEvent>() {
			public ShakeEvent call() throws Exception {
				final ShakeEvent event = new ShakeEvent();
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
		final QueryBuilder<ShakeEvent, String> queryBuilder = getDao()
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
