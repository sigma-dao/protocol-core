package com.sigma.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "sigma_market")
@Accessors(chain = true)
public class Market {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "ticker", nullable = false)
    private String ticker;
    @Column(name = "name", nullable = false)
    private String name;
}