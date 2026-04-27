package com.gps.gps_tracking_simulation_backend.dto.response;

import com.gps.gps_tracking_simulation_backend.entity.Device;
import com.gps.gps_tracking_simulation_backend.enums.DeviceStatus;
import com.gps.gps_tracking_simulation_backend.enums.DeviceType;

public record DeviceResponse(
		
		Long id,
		String name,
		DeviceType type,
		DeviceStatus status
		) {
	
	
	public static DeviceResponse from(Device device) {
		if(device == null) throw new IllegalArgumentException("Device cannot bu null");
		return new DeviceResponse(device.getId(), device.getName(), device.getType(), device.getStatus());
	}
	
}