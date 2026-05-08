package com.gps.gps_tracking_simulation_backend.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record DeviceLocationCreateRequest(
			
		
		@NotNull(message = "Longitude is required")
		@DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
		@DecimalMax(value = "180.0", message = "Longitude must be <= 180")
		Double longitude,
		
		@NotNull(message = "Latitude is required")
		@DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
		@DecimalMax(value = "90.0", message = "Latitude must be <= 90")
		Double latitude
		) {}
