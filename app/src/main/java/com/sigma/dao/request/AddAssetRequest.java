package com.sigma.dao.request;

import com.sigma.dao.constant.Blockchain;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AddAssetRequest {
    private Blockchain blockchain;
    private String symbol;
    private String contractAddress;
}