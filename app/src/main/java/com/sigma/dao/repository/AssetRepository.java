package com.sigma.dao.repository;

import com.sigma.dao.constant.Blockchain;
import com.sigma.dao.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AssetRepository extends JpaRepository<Asset, UUID> {
    List<Asset> findByBlockchainAndContractAddress(Blockchain blockchain, String contractAddress);
}