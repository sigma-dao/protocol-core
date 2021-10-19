package com.sigma.dao.repository;

import com.sigma.dao.model.GovernanceAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GovernanceActionRepository extends JpaRepository<GovernanceAction, UUID> {
}