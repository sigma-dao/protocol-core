package com.sigma.dao.request;

import com.sigma.dao.error.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AddAssetRequest extends GovernanceActionRequest {
    @NotBlank(message = ErrorCode.E0017)
    private String blockchain;
    @NotBlank(message = ErrorCode.E0019)
    private String symbol;
    @NotBlank(message = ErrorCode.E0018)
    private String contractAddress;
}