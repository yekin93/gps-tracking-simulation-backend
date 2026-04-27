package com.gps.gps_tracking_simulation_backend.exception;

@SuppressWarnings("serial")
public class DeviceNotFoundException extends RuntimeException {

	
	public DeviceNotFoundException(String message) {
		super(message);
	}
}
