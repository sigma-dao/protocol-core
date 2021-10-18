package com.sigma.dao.controller;

import com.sigma.dao.model.Fund;
import com.sigma.dao.service.FundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fund")
public class FundController {

    private final FundService fundService;

    public FundController(FundService fundService) {
        this.fundService = fundService;
    }

    @PostMapping
    public ResponseEntity<Fund> create(@RequestBody Fund fund) {
        return ResponseEntity.ok(fundService.create(fund));
    }

    @PutMapping
    public ResponseEntity<Fund> update(@RequestBody Fund fund) {
        return ResponseEntity.ok(fundService.update(fund));
    }

    @GetMapping
    public ResponseEntity<List<Fund>> get() {
        return ResponseEntity.ok(fundService.get());
    }
}