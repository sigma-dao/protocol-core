package com.sigma.dao.repository;

import com.sigma.dao.model.GovernanceAction;
import com.sigma.dao.model.GovernanceVote;
import com.sigma.dao.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GovernanceVoteRepository extends JpaRepository<GovernanceVote, UUID> {
    Optional<GovernanceVote> findByUserAndGovernanceAction(User user, GovernanceAction governanceAction);
}