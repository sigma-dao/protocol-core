package com.sigma.dao.blockchain;

import com.sigma.dao.constant.TendermintQuery;
import com.sigma.dao.constant.TendermintTransaction;
import com.sigma.dao.error.exception.ProtocolException;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Component
public class TendermintClient {

    @Value("${tendermint.base.uri}")
    private String tendermintBaseUri;

    public String query(TendermintQuery query) {
        return query(query, null);
    }

    /**
     * Sends a query to the Tendermint blockchain
     *
     * @param query the {@link TendermintQuery} type
     * @param payload JSON payload
     *
     * @return JSON response from application tier
     */
    public String query(TendermintQuery query, String payload) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", query.toString());
        jsonObject.put("uuid", UUID.randomUUID().toString());
        if(payload != null) {
            jsonObject.put("payload", new JSONObject(payload));
        }
        String data = URLEncoder.encode(Base64.getEncoder()
                .encodeToString(jsonObject.toString().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        String url = tendermintBaseUri + "/abci_query?data=%22" + data + "%22";
        HttpResponse<JsonNode> response = Unirest.get(url).asJson();
        try {
            return new JSONObject(response.getBody().toString())
                    .getJSONObject("result")
                    .getJSONObject("response")
                    .getString("log");
        } catch(Exception e) {
            log.info(response.getBody().toString());
            throw new ProtocolException(response.getBody().getObject().optString("error"));
        }
    }

    /**
     * Sends write instruction to the Tendermint blockchain
     *
     * @param transaction the {@link TendermintTransaction} type
     * @param payload JSON payload
     *
     * @return JSON response from application tier
     */
    public String write(TendermintTransaction transaction, String payload) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tx", transaction.toString());
        jsonObject.put("uuid", UUID.randomUUID().toString());
        jsonObject.put("payload", new JSONObject(payload));
        String tx = URLEncoder.encode(Base64.getEncoder()
                .encodeToString(jsonObject.toString().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        String url = tendermintBaseUri + "/broadcast_tx_commit?tx=%22" + tx + "%22";
        HttpResponse<JsonNode> response = Unirest.get(url).asJson();
        try {
            return new String(Base64.getDecoder().decode(new JSONObject(response.getBody().toString())
                    .getJSONObject("result")
                    .getJSONObject("deliver_tx")
                    .getString("data")));
        } catch(Exception e) {
            log.info(response.getBody().toString());
            throw new ProtocolException(response.getBody().getObject().optString("error"));
        }
    }
}