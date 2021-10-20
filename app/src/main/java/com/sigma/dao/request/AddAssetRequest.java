package com.sigma.dao.request;

import com.sigma.dao.constant.Blockchain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AddAssetRequest extends SignedRequest {
    private Blockchain blockchain;
    private String symbol;
    private String contractAddress;
}