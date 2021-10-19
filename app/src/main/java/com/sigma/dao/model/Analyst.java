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
@Table(name = "sigma_analyst")
@Accessors(chain = true)
public class Analyst {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "public_key", nullable = false)
    private String publicKey;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "rating", nullable = false)
    private Integer rating;
}