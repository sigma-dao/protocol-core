package com.sigma.dao.model;

import com.sigma.dao.constant.GovernanceActionType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "sigma_governance_action")
@Accessors(chain = true)
public class GovernanceAction {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private GovernanceActionType type;
    @Column(name = "entity_id", nullable = false)
    private UUID entityId;
    @Column(name = "votes_against", nullable = false)
    private Integer votesFor;
    @Column(name = "votes_for", nullable = false)
    private Integer votesAgainst;
}