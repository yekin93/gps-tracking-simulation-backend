package com.gps.gps_tracking_simulation_backend.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Table(name = "device_locations")
@Entity
public class DeviceLocation {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private double longitude;
	
	@Column(nullable = false)
	private double latitude;
	
	@Column(name = "recorded_at", nullable = false, updatable = false)
	private Instant recordedAt;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id", nullable = false)
	private Device device;
	
	
	@PrePersist
	private void prePersist() {
		this.createdAt = Instant.now();
		if(this.recordedAt == null) {
			this.recordedAt = this.createdAt;
		}
	}
}
