package com.sigma.dao.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public abstract class SignedRequest {
    private String signature;
    private String publicKey;
}