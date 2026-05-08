package com.gps.gps_tracking_simulation_backend.controller;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gps.gps_tracking_simulation_backend.dto.request.DeviceCreateRequest;
import com.gps.gps_tracking_simulation_backend.dto.request.DeviceLocationCreateRequest;
import com.gps.gps_tracking_simulation_backend.dto.response.DeviceResponse;
import com.gps.gps_tracking_simulation_backend.dto.response.LocationResponse;
import com.gps.gps_tracking_simulation_backend.dto.response.PageResponse;
import com.gps.gps_tracking_simulation_backend.entity.Device;
import com.gps.gps_tracking_simulation_backend.entity.DeviceLocation;
import com.gps.gps_tracking_simulation_backend.service.DeviceLocationService;
import com.gps.gps_tracking_simulation_backend.service.DeviceService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
	
	private final DeviceService deviceService;
	private final DeviceLocationService locationService;
	

	public DeviceController(DeviceService deviceService,
							DeviceLocationService locationService) {
		this.deviceService = deviceService;
		this.locationService = locationService;
	}
	
	@PostMapping
	public ResponseEntity<DeviceResponse> createDevice(@Valid @RequestBody DeviceCreateRequest request) {
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(DeviceResponse.from(deviceService.createDevice(request)));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DeviceResponse> getDeviceById(@PathVariable Long id) {
		return ResponseEntity
				.ok(DeviceResponse.from(deviceService.getDeviceById(id)));
	}
	
	@GetMapping
	public ResponseEntity<PageResponse<DeviceResponse>> getDevices(@RequestParam(defaultValue = "0") @Min(0) int page,
															@RequestParam(defaultValue = "10") @Min(0) @Max(1000) int size) {
		Page<Device> devicePage = deviceService.getDevices(page, size);
		
		List<DeviceResponse> devices = devicePage
					.getContent()
					.stream()
					.map(DeviceResponse::from)
					.toList();
		
		PageResponse<DeviceResponse> response = new PageResponse<DeviceResponse>(devices, devicePage.getNumber(), devicePage.getSize(), devicePage.getTotalElements(), devicePage.getTotalPages());
		
		return ResponseEntity
				.ok(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<DeviceResponse> updateDevice(@PathVariable Long id, @Valid @RequestBody DeviceCreateRequest request){
		Device device = deviceService.updateDevice(id, request);
		return ResponseEntity.ok(DeviceResponse.from(device));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
		deviceService.deleteDevice(id);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/{id}/locations")
	public ResponseEntity<LocationResponse> saveLocation(@PathVariable Long id, @Valid @RequestBody DeviceLocationCreateRequest request){
		DeviceLocation location = locationService.saveLocation(id, request);
		return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(LocationResponse.from(location));
	}
	
	@GetMapping("/{id}/locations/latest")
	public ResponseEntity<LocationResponse> getLatestLocation(@PathVariable Long id){
		LocationResponse response = locationService.getLatestLocation(id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}/locations")
	public ResponseEntity<PageResponse<LocationResponse>> getLocations(
				@PathVariable Long id,
				@RequestParam(required = false) Instant from,
				@RequestParam(required = false) Instant to,
				@RequestParam(defaultValue = "0") @Min(0) @Max(100) int page,
				@RequestParam(defaultValue = "50") @Min(0) @Max(9999) int size
			) {
		Page<DeviceLocation> locationPage = locationService.getLocations(id, from, to, page, size);
		
		List<LocationResponse> locations = locationPage
											.getContent()
											.stream()
											.map(LocationResponse::from)
											.toList();
		PageResponse<LocationResponse> res = new PageResponse<LocationResponse>(locations, locationPage.getNumber(), locationPage.getSize(), locationPage.getTotalElements(), locationPage.getTotalPages());
		
		return ResponseEntity.ok(res);
	}
}
