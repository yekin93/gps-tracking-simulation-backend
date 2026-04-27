package com.gps.gps_tracking_simulation_backend.dto.request;

import com.gps.gps_tracking_simulation_backend.enums.DeviceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DeviceCreateRequest(
		
		@NotBlank(message = "Name must not be blank")
		@Size(max = 100, message = "Name must not exceed 100 characters")
		String name,
		
		@NotNull(message = "Device type is required")
		DeviceType type
		
		) {}