package com.sigma.dao.controller.mock;

import com.google.protobuf.ByteString;
import com.sigma.dao.blockchain.TendermintTransactionHandler;
import com.sigma.dao.constant.TendermintTransaction;
import org.json.JSONObject;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@Profile("test")
public class GrpcMockController {

    private final TendermintTransactionHandler tendermintTransactionHandler;

    public GrpcMockController(TendermintTransactionHandler tendermintTransactionHandler) {
        this.tendermintTransactionHandler = tendermintTransactionHandler;
    }

    @RequestMapping("/broadcast_tx_commit")
    public ResponseEntity<String> submitTx(@RequestParam String tx) throws Exception {
        JSONObject jsonObject = new JSONObject(new String(Base64.getDecoder()
                .decode(tx.replace("\"", ""))));
        TendermintTransaction transaction = TendermintTransaction.valueOf(jsonObject.getString("tx"));
        ByteString data = tendermintTransactionHandler.process(transaction, jsonObject);
        JSONObject jsonResponse = new JSONObject().put("result", new JSONObject().put("deliver_tx",
                new JSONObject().put("data", Base64.getEncoder().encodeToString(
                        new JSONObject(data.toStringUtf8()).toString().getBytes(StandardCharsets.UTF_8)))));
        return ResponseEntity.ok(jsonResponse.toString());
    }
}