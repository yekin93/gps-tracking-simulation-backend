package com.gps.gps_tracking_simulation_backend.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gps.gps_tracking_simulation_backend.dto.request.DeviceLocationCreateRequest;
import com.gps.gps_tracking_simulation_backend.dto.response.LocationResponse;
import com.gps.gps_tracking_simulation_backend.entity.Device;
import com.gps.gps_tracking_simulation_backend.entity.DeviceLocation;
import com.gps.gps_tracking_simulation_backend.exception.DeviceNotFoundException;
import com.gps.gps_tracking_simulation_backend.repository.DeviceLocationRepository;
import com.gps.gps_tracking_simulation_backend.repository.DeviceRepository;
import com.gps.gps_tracking_simulation_backend.service.DeviceLocationService;

import tools.jackson.databind.ObjectMapper;

@Service
public class DeviceLocationServiceImpl implements DeviceLocationService {
	
	private final DeviceRepository deviceRepo;
	private final DeviceLocationRepository locationRepo;
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;
	
	public DeviceLocationServiceImpl(DeviceRepository deviceRepo,
									DeviceLocationRepository locationRepo,
									RedisTemplate<String, String> redisTemplate,
									ObjectMapper objectMapper) {
		this.deviceRepo = deviceRepo;
		this.locationRepo = locationRepo;
		this.redisTemplate = redisTemplate;
		this.objectMapper = objectMapper;
	}

	@Override
	@Transactional
	public DeviceLocation saveLocation(Long deviceId, DeviceLocationCreateRequest request) {
		Device device = deviceRepo
							.findById(deviceId)
							.orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + deviceId));
		DeviceLocation location = new DeviceLocation();
		location.setLatitude(request.latitude());
		location.setLongitude(request.longitude());
		location.setDevice(device);
		DeviceLocation savedLocation = locationRepo.save(location);
		
		try {
			String key = "device:" + deviceId + ":latest_location";
			String objLocation = objectMapper.writeValueAsString(LocationResponse.from(savedLocation));
			redisTemplate.opsForValue().set(key, objLocation, 10, TimeUnit.MINUTES);
		} catch (Exception ex) {
			System.out.println("Redis latest location write failed:"+ ex.getMessage());
		}
		
		return savedLocation;
	}

	@Override
	@Transactional(readOnly = true)
	public LocationResponse getLatestLocation(Long deviceId) {
		String redisKey = "device:" + deviceId + ":latest_location";
		String cached = redisTemplate.opsForValue().get(redisKey);
		LocationResponse location = null;
		if(cached != null) {
			try {
				return objectMapper.readValue(cached, LocationResponse.class);
			} catch (Exception ex) {
				System.out.println("Error while reading from redis" + ex.getMessage());
			}
		}
		
		DeviceLocation latestLocation = locationRepo.findTopByDeviceIdOrderByRecordedAtDesc(deviceId);
		
		if(latestLocation == null) {
			throw new DeviceNotFoundException("Location not found for device id:" + deviceId);
		}
		
		location = LocationResponse.from(latestLocation);
		try {
			String redisData = objectMapper.writeValueAsString(location);
			redisTemplate.opsForValue().set(redisKey, redisData);
		} catch (Exception ex) {
			System.out.println("Error while data write to redis" + ex.getMessage());
		}
		return location;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<DeviceLocation> getLocations(Long deviceId, Instant from, Instant to, int page, int size) {
		Device device = deviceRepo
						.findById(deviceId)
						.orElseThrow(() -> new DeviceNotFoundException("Device not foun with id: " + deviceId));
		Page<DeviceLocation> deviceLocations = null;
		Pageable pageable = PageRequest.of(page, size);
		
		if(from != null && to != null) {
			deviceLocations = locationRepo.findByDeviceAndRecordedAtBetween(device, from, to, pageable);
		} else if(from != null && to == null) {
			deviceLocations = locationRepo.findByDeviceAndRecordedAtAfter(device, from, pageable);
		} else if(from == null && to != null) {
			deviceLocations = locationRepo.findByDeviceAndRecordedAtBefore(device, to, pageable);
		} else {
			deviceLocations = locationRepo.findByDevice(device, pageable);
		}
		
		return deviceLocations;
	}

}
