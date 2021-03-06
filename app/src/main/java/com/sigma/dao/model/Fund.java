package com.sigma.dao.model;

import com.sigma.dao.constant.FundType;
import com.sigma.dao.constant.GovernanceStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "sigma_fund")
@Accessors(chain = true)
public class Fund {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_asset_id", nullable = false)
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
    private GovernanceStatus status;
    @Column(name = "redemption_frequency", nullable = false)
    private Long redemptionFrequency;
    @Column(name = "termination_date")
    private Long terminationDate;
    @Column(name = "proposed_minimum_subscription")
    private Long proposedMinimumSubscription;
    @Column(name = "proposed_disbursement_frequency")
    private Long proposedDisbursementFrequency;
    @Column(name = "proposed_management_fee")
    private Integer proposedManagementFee;
    @Column(name = "proposed_performance_fee")
    private Integer proposedPerformanceFee;
    @Column(name = "proposed_redemption_frequency")
    private Long proposedRedemptionFrequency;
}