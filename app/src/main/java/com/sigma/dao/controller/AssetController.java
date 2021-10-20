package com.sigma.dao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.dao.blockchain.TendermintClient;
import com.sigma.dao.constant.TendermintTransaction;
import com.sigma.dao.request.AddAssetRequest;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/asset")
public class AssetController {

    private final TendermintClient tendermintClient;
    private final ObjectMapper objectMapper;

    public AssetController(TendermintClient tendermintClient,
                           ObjectMapper objectMapper) {
        this.tendermintClient = tendermintClient;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<String> add(@RequestBody AddAssetRequest request) {
        return ResponseEntity.ok(tendermintClient.write(
                TendermintTransaction.ADD_ASSET, objectMapper.writeValueAsString(request)));
    }
}
