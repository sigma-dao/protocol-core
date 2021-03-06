package com.sigma.dao.controller;

import com.sigma.dao.blockchain.TendermintClient;
import com.sigma.dao.constant.TendermintQuery;
import com.sigma.dao.constant.TendermintTransaction;
import com.sigma.dao.request.AddAssetRequest;
import com.sigma.dao.request.PaginatedRequest;
import com.sigma.dao.request.RemoveAssetRequest;
import com.sigma.dao.utils.JSONUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/asset")
public class AssetController {

    private final TendermintClient tendermintClient;
    private final JSONUtils jsonUtils;

    public AssetController(TendermintClient tendermintClient,
                           JSONUtils jsonUtils) {
        this.tendermintClient = tendermintClient;
        this.jsonUtils = jsonUtils;
    }

    @GetMapping
    public ResponseEntity<String> get(@RequestParam Integer size,
                                      @RequestParam Integer page,
                                      @RequestParam String sort) {
        PaginatedRequest request = new PaginatedRequest().setPage(page).setSize(size).setSort(sort);
        return ResponseEntity.ok(tendermintClient.query(TendermintQuery.GET_ASSETS, jsonUtils.toJson(request)));
    }

    @PostMapping
    public ResponseEntity<String> add(@Valid @RequestBody AddAssetRequest request) {
        return ResponseEntity.ok(tendermintClient.write(TendermintTransaction.ADD_ASSET, jsonUtils.toJson(request)));
    }

    @DeleteMapping
    public ResponseEntity<String> remove(@Valid @RequestBody RemoveAssetRequest request) {
        return ResponseEntity.ok(tendermintClient.write(TendermintTransaction.REMOVE_ASSET, jsonUtils.toJson(request)));
    }
}
