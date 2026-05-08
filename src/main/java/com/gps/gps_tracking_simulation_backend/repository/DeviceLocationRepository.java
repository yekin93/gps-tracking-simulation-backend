package com.gps.gps_tracking_simulation_backend.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gps.gps_tracking_simulation_backend.dto.response.LocationResponse;
import com.gps.gps_tracking_simulation_backend.entity.Device;
import com.gps.gps_tracking_simulation_backend.entity.DeviceLocation;

public interface DeviceLocationRepository extends JpaRepository<DeviceLocation, Long> {

	Page<DeviceLocation> findByDevice(Device device, Pageable pageable);
	Page<DeviceLocation> findByDeviceAndRecordedAtBetween(Device device, Instant from, Instant to, Pageable pageable);
	Page<DeviceLocation> findByDeviceAndRecordedAtAfter(Device device, Instant from, Pageable pageable);
	Page<DeviceLocation> findByDeviceAndRecordedAtBefore(Device device, Instant to, Pageable pageable);
	DeviceLocation findTopByDeviceIdOrderByRecordedAtDesc(Long deviceId);
}
