package com.sigma.dao.blockchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.sigma.dao.constant.TendermintTransaction;
import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.Fund;
import com.sigma.dao.service.*;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class TendermintTransactionHandler {

    private final ObjectMapper objectMapper;
    private final FundService fundService;
    private final ForecastService forecastService;
    private final MarketService marketService;
    private final NetworkConfigService networkConfigService;
    private final AssetService assetService;
    private final AnalystService analystService;

    public TendermintTransactionHandler(ObjectMapper objectMapper,
                                        FundService fundService,
                                        ForecastService forecastService,
                                        MarketService marketService,
                                        NetworkConfigService networkConfigService,
                                        AssetService assetService,
                                        AnalystService analystService) {
        this.objectMapper = objectMapper;
        this.fundService = fundService;
        this.forecastService = forecastService;
        this.marketService = marketService;
        this.networkConfigService = networkConfigService;
        this.assetService = assetService;
        this.analystService = analystService;
    }

    /**
     * Processes Tendermint write transactions by handing off to application tier
     *
     * @param transaction the {@link TendermintTransaction}
     * @param jsonObject the JSON data received by Tendermint
     *
     * @return the response object which is committed to the blockchain state
     *
     * @throws JsonProcessingException when cannot read / write JSON data
     */
    public ByteString process(TendermintTransaction transaction,
                              JSONObject jsonObject) throws JsonProcessingException {
        String payload = jsonObject.getJSONObject("payload").toString();
        if(transaction.equals(TendermintTransaction.CREATE_FUND)) {
            Fund fund = fundService.create(objectMapper.readValue(payload, Fund.class));
            return ByteString.copyFromUtf8(objectMapper.writeValueAsString(fund));
        } else if(transaction.equals(TendermintTransaction.UPDATE_FUND)) {
            Fund fund = fundService.update(objectMapper.readValue(payload, Fund.class));
            return ByteString.copyFromUtf8(objectMapper.writeValueAsString(fund));
        }
        throw new ProtocolException(ErrorCode.E0015);
    }
}