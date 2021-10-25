package com.sigma.dao.blockchain;

import com.sigma.dao.constant.TendermintQuery;
import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.Fund;
import com.sigma.dao.model.NetworkConfig;
import com.sigma.dao.request.PaginatedRequest;
import com.sigma.dao.response.GetAssetsResponse;
import com.sigma.dao.service.AssetService;
import com.sigma.dao.service.FundService;
import com.sigma.dao.service.NetworkConfigService;
import com.sigma.dao.utils.JSONUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TendermintQueryHandler {

    private final FundService fundService;
    private final NetworkConfigService networkConfigService;
    private final AssetService assetService;
    private final JSONUtils jsonUtils;

    public TendermintQueryHandler(FundService fundService,
                                  NetworkConfigService networkConfigService,
                                  AssetService assetService,
                                  JSONUtils jsonUtils) {
        this.fundService = fundService;
        this.networkConfigService = networkConfigService;
        this.assetService = assetService;
        this.jsonUtils = jsonUtils;
    }

    /**
     * Processes the Tendermint query by handing off to application tier
     *
     * @param query the {@link TendermintQuery}
     * @param jsonObject the JSON data received by Tendermint
     *
     * @return the response object (in JSON format)
     */
    public String process(TendermintQuery query, JSONObject jsonObject) {
        JSONObject payload = jsonObject.optJSONObject("payload");
        if(query.equals(TendermintQuery.GET_FUNDS)) {
            List<Fund> funds = fundService.get(jsonUtils.fromJson(payload.toString(), PaginatedRequest.class));
            return jsonUtils.toJson(funds);
        } else if(query.equals(TendermintQuery.GET_NETWORK_CONFIG)) {
            NetworkConfig networkConfig = networkConfigService.get();
            return jsonUtils.toJson(networkConfig);
        } else if(query.equals(TendermintQuery.GET_ASSETS)) {
            GetAssetsResponse response = assetService.get(jsonUtils.fromJson(payload.toString(), PaginatedRequest.class));
            return jsonUtils.toJson(response);
        }
        throw new ProtocolException(ErrorCode.E0016);
    }
}