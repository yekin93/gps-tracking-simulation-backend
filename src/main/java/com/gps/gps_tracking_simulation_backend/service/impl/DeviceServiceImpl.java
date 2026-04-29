package com.gps.gps_tracking_simulation_backend.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gps.gps_tracking_simulation_backend.dto.request.DeviceCreateRequest;
import com.gps.gps_tracking_simulation_backend.dto.response.DeviceResponse;
import com.gps.gps_tracking_simulation_backend.entity.Device;
import com.gps.gps_tracking_simulation_backend.enums.DeviceStatus;
import com.gps.gps_tracking_simulation_backend.exception.DeviceNotFoundException;
import com.gps.gps_tracking_simulation_backend.repository.DeviceRepository;
import com.gps.gps_tracking_simulation_backend.service.DeviceService;

import tools.jackson.databind.ObjectMapper;


@Service
public class DeviceServiceImpl implements DeviceService {
	
	private final DeviceRepository deviceRepository;
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;
	
	
	public DeviceServiceImpl(DeviceRepository deviceRepository,
								RedisTemplate<String, String> redisTemplate,
								ObjectMapper objectMapper) {
		this.deviceRepository = deviceRepository;
		this.redisTemplate = redisTemplate;
		this.objectMapper = objectMapper;
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
		String redisKey = "device:" + id;
		String cached = redisTemplate.opsForValue().get(redisKey);
		
		if(cached != null) {
			try {
				DeviceResponse deviceResponse = objectMapper.readValue(cached, DeviceResponse.class);
				
				Device device = new Device();
				device.setId(deviceResponse.id());
				device.setName(deviceResponse.name());
				device.setStatus(deviceResponse.status());
				device.setType(deviceResponse.type());
				return device;
			} catch (Exception ex) {
				throw new RuntimeException("Error while reading from Redis", ex);
			}
		}
		
		Device device = deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
		
		try {
			String json = objectMapper.writeValueAsString(DeviceResponse.from(device));
			redisTemplate.opsForValue().set(redisKey, json, 10, TimeUnit.MINUTES);
		} catch (Exception ex) {
			System.out.print("Redis write failed: " + ex);
		}
		
		return device;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Device> getDevices(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return deviceRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public Device updateDevice(Long id, DeviceCreateRequest request) {
		Device device = deviceRepository
							.findById(id)
							.orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
		
		device.setName(request.name());
		device.setType(request.type());
		
		Device updatedDevice = deviceRepository.save(device);
		try {
			String key = "device:" + id;
			redisTemplate.delete(key);
		} catch (Exception ex) {
			System.out.println("Redis delete failed: " + ex.getMessage());
		}
		return updatedDevice;
	}

	@Override
	@Transactional
	public void deleteDevice(Long id) {
		Device device = deviceRepository
							.findById(id)
							.orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
		deviceRepository.delete(device);
		try {
			String key = "device:" + id;
			redisTemplate.delete(key);
		} catch (Exception ex) {
			System.out.println("Redis delete failed: " + ex.getMessage());
		}
	}
}
