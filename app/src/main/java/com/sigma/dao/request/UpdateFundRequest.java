package com.sigma.dao.request;

import com.sigma.dao.error.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class UpdateFundRequest extends GovernanceActionRequest {
    @NotNull(message = ErrorCode.E0003)
    private Long minimumSubscription;
    @NotNull(message = ErrorCode.E0001)
    private Long disbursementFrequency;
    @NotNull(message = ErrorCode.E0002)
    private Integer managementFee;
    @NotNull(message = ErrorCode.E0004)
    private Integer performanceFee;
    @NotNull(message = ErrorCode.E0005)
    private Long redemptionFrequency;
    @NotNull(message = ErrorCode.E0008)
    private UUID id;
}
