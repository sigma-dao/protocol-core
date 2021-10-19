package com.sigma.dao.model;

import com.sigma.dao.constant.Blockchain;
import com.sigma.dao.constant.GovernanceStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "sigma_asset")
@Accessors(chain = true)
public class Asset {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "symbol", nullable = false)
    private String symbol;
    @Column(name = "contractAddress", nullable = false)
    private String contractAddress;
    @Enumerated(EnumType.STRING)
    @Column(name = "blockchain", nullable = false)
    private Blockchain blockchain;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GovernanceStatus status;
}