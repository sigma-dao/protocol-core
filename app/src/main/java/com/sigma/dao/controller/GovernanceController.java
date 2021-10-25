package com.sigma.dao.controller;

import com.sigma.dao.blockchain.TendermintClient;
import com.sigma.dao.constant.TendermintQuery;
import com.sigma.dao.request.PaginatedRequest;
import com.sigma.dao.utils.JSONUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/governance")
public class GovernanceController {

    private final TendermintClient tendermintClient;
    private final JSONUtils jsonUtils;

    public GovernanceController(TendermintClient tendermintClient,
                                JSONUtils jsonUtils) {
        this.tendermintClient = tendermintClient;
        this.jsonUtils = jsonUtils;
    }

    @GetMapping
    @RequestMapping("/actions")
    public ResponseEntity<String> get(@RequestParam Integer size,
                                      @RequestParam Integer page,
                                      @RequestParam String sort) {
        PaginatedRequest request = new PaginatedRequest().setPage(page).setSize(size).setSort(sort);
        return ResponseEntity.ok(tendermintClient.query(TendermintQuery.GET_GOVERNANCE_ACTIONS,
                jsonUtils.toJson(request)));
    }
}