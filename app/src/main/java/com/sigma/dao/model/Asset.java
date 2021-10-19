package com.sigma.dao.model;

import com.sigma.dao.constant.Blockchain;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "sigma_asset")
@Accessors(chain = true)
public class Asset {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(name = "symbol", nullable = false)
    private String symbol;
    @Column(name = "contractAddress", nullable = false)
    private String contractAddress;
    @Enumerated(EnumType.STRING)
    @Column(name = "blockchain", nullable = false)
    private Blockchain blockchain;
}