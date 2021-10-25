package com.sigma.dao.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaginatedRequest {
    private Integer size;
    private Integer page;
    private String sort;
}