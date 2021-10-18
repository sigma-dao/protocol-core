package com.sigma.dao.model;

import com.sigma.dao.model.constant.Blockchain;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Asset {
    private String symbol;
    private String contractAddress;
    private Blockchain blockchain;
}