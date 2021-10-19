package com.sigma.dao.repository;

import com.sigma.dao.model.Fund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FundRepository extends JpaRepository<Fund, UUID> {
}