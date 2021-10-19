package com.sigma.dao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.dao.blockchain.TendermintClient;
import com.sigma.dao.constant.TendermintQuery;
import com.sigma.dao.constant.TendermintTransaction;
import com.sigma.dao.model.Fund;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fund")
public class FundController {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final TendermintClient tendermintClient;

    public FundController(TendermintClient tendermintClient) {
        this.tendermintClient = tendermintClient;
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<String> create(@RequestBody Fund fund) {
        return ResponseEntity.ok(tendermintClient.write(
                TendermintTransaction.CREATE_FUND, objectMapper.writeValueAsString(fund)));
    }

    @SneakyThrows
    @PutMapping
    public ResponseEntity<String> update(@RequestBody Fund fund) {
        return ResponseEntity.ok(tendermintClient.write(
                TendermintTransaction.UPDATE_FUND, objectMapper.writeValueAsString(fund)));
    }

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok(tendermintClient.query(TendermintQuery.GET_FUNDS));
    }
}