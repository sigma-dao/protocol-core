package com.sigma.dao.blockchain;

import com.sigma.dao.blockchain.constant.TendermintQuery;
import com.sigma.dao.blockchain.constant.TendermintTransaction;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Component
public class TendermintClient {

    public String query(TendermintQuery query) {
        return query(query, null);
    }

    public String query(TendermintQuery query, String payload) {
        return null;
    }

    public String write(TendermintTransaction transaction, String payload) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tx", transaction.toString());
        jsonObject.put("uuid", UUID.randomUUID().toString());
        jsonObject.put("payload", new JSONObject(payload));
        String tx = URLEncoder.encode(Base64.getEncoder()
                .encodeToString(jsonObject.toString().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        String url = "http://localhost:26657/broadcast_tx_commit?tx=%22" + tx + "%22";
        HttpResponse<JsonNode> response = Unirest.get(url).asJson();
        try {
            return new JSONObject(response.getBody().toString())
                    .getJSONObject("result")
                    .getJSONObject("deliver_tx")
                    .getString("log");
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            return new JSONObject()
                    .put("error", String.format("Unknown error occurred: %s", e.getMessage())).toString();
        }
    }
}