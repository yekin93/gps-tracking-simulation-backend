package com.gps.gps_tracking_simulation_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gps.gps_tracking_simulation_backend.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {

}
