package com.sigma.dao.controller;

import com.sigma.dao.blockchain.TendermintClient;
import com.sigma.dao.constant.TendermintQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/network")
public class NetworkController {

    private final TendermintClient tendermintClient;

    public NetworkController(TendermintClient tendermintClient) {
        this.tendermintClient = tendermintClient;
    }

    @GetMapping("/config")
    public ResponseEntity<String> getConfig() {
        return ResponseEntity.ok(tendermintClient.query(TendermintQuery.GET_NETWORK_CONFIG));
    }
}