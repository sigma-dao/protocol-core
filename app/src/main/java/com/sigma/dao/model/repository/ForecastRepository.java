package com.sigma.dao.model.repository;

import com.sigma.dao.model.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ForecastRepository extends JpaRepository<Forecast, UUID> {
}