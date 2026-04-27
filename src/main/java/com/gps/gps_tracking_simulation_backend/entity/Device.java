package com.gps.gps_tracking_simulation_backend.entity;

import java.time.Instant;

import com.gps.gps_tracking_simulation_backend.enums.DeviceStatus;
import com.gps.gps_tracking_simulation_backend.enums.DeviceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "devices")
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DeviceType type;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DeviceStatus status;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
	
	
	@PrePersist
	private void prePersist() {
		this.createdAt = Instant.now();
		this.updatedAt = this.createdAt;
	}
	
	@PreUpdate
	private void preUpdate() {
		this.updatedAt = Instant.now();
	}
	
}
