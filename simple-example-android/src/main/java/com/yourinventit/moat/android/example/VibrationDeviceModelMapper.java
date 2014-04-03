/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */
package com.yourinventit.moat.android.example;

import com.yourinventit.dmc.api.moat.ModelMapper;
import com.yourinventit.dmc.api.moat.ModelMapper.SingletonOnMemory;

/**
 * A {@link ModelMapper} for {@link VibrationDevice}. This implementation doesn't
 * use any database persistence but store an object on memory.
 * 
 * @author dbaba@yourinventit.com
 * 
 */
public class VibrationDeviceModelMapper extends
		SingletonOnMemory<VibrationDevice> {

	/**
	 * @param singleton
	 */
	public VibrationDeviceModelMapper(VibrationDevice singleton) {
		super(singleton);
	}

}
