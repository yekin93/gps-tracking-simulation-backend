package com.gps.gps_tracking_simulation_backend.service;

import java.time.Instant;

import org.springframework.data.domain.Page;

import com.gps.gps_tracking_simulation_backend.dto.request.DeviceLocationCreateRequest;
import com.gps.gps_tracking_simulation_backend.dto.response.LocationResponse;
import com.gps.gps_tracking_simulation_backend.entity.DeviceLocation;

public interface DeviceLocationService {

	DeviceLocation saveLocation(Long deviceId, DeviceLocationCreateRequest request);
	LocationResponse getLatestLocation(Long deviceId);
	Page<DeviceLocation> getLocations(Long deviceId, Instant from, Instant to, int page, int size);
}
