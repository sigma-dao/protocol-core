package com.sigma.dao.repository;

import com.sigma.dao.model.Asset;
import com.sigma.dao.model.Fund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FundRepository extends JpaRepository<Fund, UUID> {
    List<Fund> findBySubscriptionAsset(Asset asset);
}