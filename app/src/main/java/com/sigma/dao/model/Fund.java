package com.sigma.dao.model;

import com.sigma.dao.model.constant.FundStatus;
import com.sigma.dao.model.constant.FundType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "sigma_fund")
@Accessors(chain = true)
public class Fund {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_asset_id", nullable = true)
    private Asset subscriptionAsset;
    @Column(name = "minimum_subscription", nullable = false)
    private Long minimumSubscription;
    @Column(name = "disbursement_frequency", nullable = false)
    private Long disbursementFrequency;
    @Column(name = "management_fee", nullable = false)
    private Integer managementFee;
    @Column(name = "performance_fee", nullable = false)
    private Integer performanceFee;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private FundType type;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FundStatus status;
    @Column(name = "activation_date", nullable = false)
    private Long activationDate;
    @Column(name = "redemption_frequency", nullable = false)
    private Long redemptionFrequency;
    @Column(name = "termination_date")
    private Long terminationDate;
}