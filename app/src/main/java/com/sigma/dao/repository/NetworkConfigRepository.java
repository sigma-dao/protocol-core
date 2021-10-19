package com.sigma.dao.repository;

import com.sigma.dao.model.NetworkConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NetworkConfigRepository extends JpaRepository<NetworkConfig, Long> {
}