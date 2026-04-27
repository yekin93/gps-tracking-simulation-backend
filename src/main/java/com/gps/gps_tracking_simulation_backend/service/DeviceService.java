package com.gps.gps_tracking_simulation_backend.service;

import org.springframework.data.domain.Page;

import com.gps.gps_tracking_simulation_backend.dto.request.DeviceCreateRequest;
import com.gps.gps_tracking_simulation_backend.entity.Device;

public interface DeviceService {

	Device createDevice(DeviceCreateRequest request);
	Device getDeviceById(Long id);
	Page<Device> getDevices(int page, int size);
}
