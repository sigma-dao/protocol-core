package com.sigma.dao.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Analyst {
    private String publicKey;
    private String username;
    private Integer rating;
}