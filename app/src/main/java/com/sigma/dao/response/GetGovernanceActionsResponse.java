package com.sigma.dao.response;

import com.sigma.dao.model.GovernanceAction;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GetGovernanceActionsResponse {
    private List<GovernanceAction> governanceActions;
}