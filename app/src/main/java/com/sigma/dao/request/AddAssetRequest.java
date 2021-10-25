package com.sigma.dao.request;

import com.sigma.dao.constant.Blockchain;
import com.sigma.dao.error.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AddAssetRequest extends GovernanceActionRequest {
    @NotNull(message = ErrorCode.E0017)
    private Blockchain blockchain;
    @NotNull(message = ErrorCode.E0019)
    private String symbol;
    @NotNull(message = ErrorCode.E0018)
    private String contractAddress;
}