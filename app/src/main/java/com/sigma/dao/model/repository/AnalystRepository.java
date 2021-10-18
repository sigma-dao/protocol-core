package com.sigma.dao.model.repository;

import com.sigma.dao.model.Analyst;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnalystRepository extends JpaRepository<Analyst, UUID> {
}