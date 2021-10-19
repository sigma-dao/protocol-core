package com.sigma.dao.repository;

import com.sigma.dao.model.NetworkConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NetworkConfigRepository extends JpaRepository<NetworkConfig, UUID> {
}