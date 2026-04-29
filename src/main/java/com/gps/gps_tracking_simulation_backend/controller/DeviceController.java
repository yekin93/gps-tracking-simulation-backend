package com.gps.gps_tracking_simulation_backend.controller;

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
import com.gps.gps_tracking_simulation_backend.dto.response.DeviceResponse;
import com.gps.gps_tracking_simulation_backend.dto.response.PageResponse;
import com.gps.gps_tracking_simulation_backend.entity.Device;
import com.gps.gps_tracking_simulation_backend.service.DeviceService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
	
	private final DeviceService deviceService;
	

	public DeviceController(DeviceService deviceService) {
		this.deviceService = deviceService;
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
}
