package com.sigma.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "governance_vote")
@Accessors(chain = true)
public class GovernanceVote {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "governance_action_id", nullable = false)
    private GovernanceAction governanceAction;
    @Column(name = "timestamp", nullable = false)
    private Long timestamp;
}