package com.sigma.dao.response;

import com.sigma.dao.model.Asset;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GetAssetsResponse {
    private List<Asset> assets;
}