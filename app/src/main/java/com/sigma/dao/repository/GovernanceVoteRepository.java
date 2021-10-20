package com.sigma.dao.repository;

import com.sigma.dao.model.GovernanceVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GovernanceVoteRepository extends JpaRepository<GovernanceVote, UUID> {
}