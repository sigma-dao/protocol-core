package com.sigma.dao.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RemoveAssetRequest extends GovernanceActionRequest {
    private UUID id;
}