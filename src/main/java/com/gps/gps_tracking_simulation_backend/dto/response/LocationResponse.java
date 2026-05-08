package com.gps.gps_tracking_simulation_backend.dto.response;

import java.time.Instant;

import com.gps.gps_tracking_simulation_backend.entity.DeviceLocation;

public record LocationResponse(
		Long id,
		double latitude,
		double longitude,
		Instant recordedAt
		) {
	
	
	public static LocationResponse from(DeviceLocation deviceLocation) {
		return new LocationResponse(deviceLocation.getId(),
									deviceLocation.getLatitude(),
									deviceLocation.getLongitude(),
									deviceLocation.getRecordedAt());
	}
	
}