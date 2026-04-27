package com.gps.gps_tracking_simulation_backend.dto.response;

import java.util.List;

public record PageResponse<T>(
		List<T> content,
		int page,
		int size,
		long totalElements,
		int totalPages
		){
	
}