package com.sigma.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "sigma_network_config")
@Accessors(chain = true)
public class NetworkConfig {
    @Id
    @JsonIgnore
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = 1L;
    @Column(name = "min_fund_activation_time", nullable = false)
    private Long minFundActivationTime;
    @Column(name = "uuid_seed", nullable = false)
    private Long uuidSeed;
    @Column(name = "governance_token_address", nullable = false)
    private String governanceTokenAddress;
}