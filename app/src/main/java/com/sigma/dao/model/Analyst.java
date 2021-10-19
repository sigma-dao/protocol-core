package com.sigma.dao.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sigma_analyst")
@Accessors(chain = true)
public class Analyst extends NetworkEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(name = "public_key", nullable = false)
    private String publicKey;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "rating", nullable = false)
    private Integer rating;
}