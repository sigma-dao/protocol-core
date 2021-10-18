package com.sigma.dao.controller;

import com.sigma.dao.model.NetworkConfig;
import com.sigma.dao.service.NetworkConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/network")
public class NetworkController {

    private final NetworkConfigService networkConfigService;

    public NetworkController(NetworkConfigService networkConfigService) {
        this.networkConfigService = networkConfigService;
    }

    @GetMapping("/config")
    public ResponseEntity<NetworkConfig> getConfig() {
        return ResponseEntity.ok(networkConfigService.get());
    }
}