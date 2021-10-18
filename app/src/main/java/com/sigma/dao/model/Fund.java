package com.sigma.dao.model;

import com.sigma.dao.model.constant.FundType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Fund {
    private Asset subscriptionAsset;
    private Long minimumSubscription;
    private Long disbursementFrequency;
    private Integer managementFee;
    private Integer performanceFee;
    private FundType type;
    private Long redemptionFrequency;
    private Long terminationDate;
}