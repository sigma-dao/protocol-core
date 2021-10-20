package com.sigma.dao.request;

import lombok.Data;

@Data
public abstract class SignedRequest {
    private String signature;
    private String publicKey;
}