package com.sigma.dao.model;

import com.sigma.dao.model.constant.ForecastStatus;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Forecast {
    private Market market;
    private Analyst analyst;
    private Long forecastTime;
    private Long currentPrice;
    private Long targetPrice;
    private Long outcomeTime;
    private Long outcomePrice;
    private ForecastStatus status;
}
