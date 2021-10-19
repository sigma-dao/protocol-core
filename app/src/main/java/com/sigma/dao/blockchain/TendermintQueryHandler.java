package com.sigma.dao.blockchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.dao.constant.TendermintQuery;
import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.Fund;
import com.sigma.dao.model.NetworkConfig;
import com.sigma.dao.service.FundService;
import com.sigma.dao.service.NetworkConfigService;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TendermintQueryHandler {

    private final ObjectMapper objectMapper;
    private final FundService fundService;
    private final NetworkConfigService networkConfigService;

    public TendermintQueryHandler(ObjectMapper objectMapper,
                                  FundService fundService,
                                  NetworkConfigService networkConfigService) {
        this.objectMapper = objectMapper;
        this.fundService = fundService;
        this.networkConfigService = networkConfigService;
    }

    /**
     * Processes the Tendermint query by handing off to application tier
     *
     * @param query the {@link TendermintQuery}
     * @param jsonObject the JSON data received by Tendermint
     *
     * @return the response object (in JSON format)
     *
     * @throws JsonProcessingException when cannot read / write JSON data
     */
    public String process(TendermintQuery query, JSONObject jsonObject) throws JsonProcessingException {
        JSONObject payload = jsonObject.optJSONObject("payload"); // TODO - use this later
        if(query.equals(TendermintQuery.GET_FUNDS)) {
            List<Fund> funds = fundService.get();
            return objectMapper.writeValueAsString(funds);
        } else if(query.equals(TendermintQuery.GET_NETWORK_CONFIG)) {
            NetworkConfig networkConfig = networkConfigService.get();
            return objectMapper.writeValueAsString(networkConfig);
        }
        throw new ProtocolException(ErrorCode.E0016);
    }
}