package com.sigma.dao.request;

import com.sigma.dao.constant.FundType;
import com.sigma.dao.constant.GovernanceStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CreateFundRequest extends GovernanceActionRequest {
    private UUID subscriptionAssetId;
    private Long minimumSubscription;
    private Long disbursementFrequency;
    private Integer managementFee;
    private Integer performanceFee;
    private FundType type;
    private GovernanceStatus status;
    private Long redemptionFrequency;
    private Long terminationDate;
}