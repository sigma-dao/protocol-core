package com.sigma.dao.request;

import com.sigma.dao.error.ErrorCode;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public abstract class SignedRequest {
    @NotNull(message = ErrorCode.E0040)
    private String signature;
    @NotNull(message = ErrorCode.E0041)
    private String publicKey;
}