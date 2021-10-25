package com.sigma.dao.controller;

import com.sigma.dao.blockchain.TendermintClient;
import com.sigma.dao.constant.TendermintQuery;
import com.sigma.dao.constant.TendermintTransaction;
import com.sigma.dao.model.Fund;
import com.sigma.dao.utils.JSONUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fund")
public class FundController {

    private final TendermintClient tendermintClient;
    private final JSONUtils jsonUtils;

    public FundController(TendermintClient tendermintClient, JSONUtils jsonUtils) {
        this.tendermintClient = tendermintClient;
        this.jsonUtils = jsonUtils;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Fund fund) {
        return ResponseEntity.ok(tendermintClient.write(TendermintTransaction.CREATE_FUND, jsonUtils.toJson(fund)));
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody Fund fund) {
        return ResponseEntity.ok(tendermintClient.write(TendermintTransaction.UPDATE_FUND, jsonUtils.toJson(fund)));
    }

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok(tendermintClient.query(TendermintQuery.GET_FUNDS));
    }
}