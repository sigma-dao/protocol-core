package com.sigma.dao.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public abstract class GovernanceActionRequest extends SignedRequest {
    protected Long enactmentDate;
    protected Long openingDate;
    protected Long closingDate;
}
