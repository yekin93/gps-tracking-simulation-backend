package com.gps.gps_tracking_simulation_backend.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gps.gps_tracking_simulation_backend.dto.request.DeviceCreateRequest;
import com.gps.gps_tracking_simulation_backend.entity.Device;
import com.gps.gps_tracking_simulation_backend.enums.DeviceStatus;
import com.gps.gps_tracking_simulation_backend.exception.DeviceNotFoundException;
import com.gps.gps_tracking_simulation_backend.repository.DeviceRepository;
import com.gps.gps_tracking_simulation_backend.service.DeviceService;


@Service
public class DeviceServiceImpl implements DeviceService {
	
	private final DeviceRepository deviceRepository;
	
	
	public DeviceServiceImpl(DeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}

	@Override
	@Transactional
	public Device createDevice(DeviceCreateRequest request) {
		Device device = new Device();
		device.setName(request.name());
		device.setType(request.type());
		device.setStatus(DeviceStatus.ACTIVE);
		return deviceRepository.save(device);
	}

	@Override
	@Transactional(readOnly = true)
	public Device getDeviceById(Long id) {
		return deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Device> getDevices(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return deviceRepository.findAll(pageable);
	}
}
