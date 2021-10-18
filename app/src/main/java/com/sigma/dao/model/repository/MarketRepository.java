package com.sigma.dao.model.repository;

import com.sigma.dao.model.Market;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MarketRepository extends JpaRepository<Market, UUID> {
}
