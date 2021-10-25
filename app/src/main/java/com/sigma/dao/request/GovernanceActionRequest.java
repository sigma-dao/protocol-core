package com.sigma.dao.request;

import com.sigma.dao.error.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public abstract class GovernanceActionRequest extends SignedRequest {
    @NotNull(message = ErrorCode.E0039)
    public Long enactmentDate;
    @NotNull(message = ErrorCode.E0033)
    public Long openingDate;
    @NotNull(message = ErrorCode.E0034)
    public Long closingDate;
}
