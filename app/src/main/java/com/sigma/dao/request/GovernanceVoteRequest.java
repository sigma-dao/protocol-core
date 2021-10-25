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
public class GovernanceVoteRequest extends SignedRequest {
    @NotNull(message = ErrorCode.E0037)
    private UUID governanceActionId;
    @NotNull(message = ErrorCode.E0038)
    private Boolean voteFor;
}