package com.sigma.dao.model;

import com.sigma.dao.model.constant.ForecastStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sigma_forecast")
@Accessors(chain = true)
public class Forecast extends NetworkEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id", nullable = false)
    private Market market;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analyst_id", nullable = false)
    private Analyst analyst;
    @Column(name = "forecast_time", nullable = false)
    private Long forecastTime;
    @Column(name = "current_price", nullable = false)
    private Long currentPrice;
    @Column(name = "target_price", nullable = false)
    private Long targetPrice;
    @Column(name = "outcome_time", nullable = false)
    private Long outcomeTime;
    @Column(name = "outcome_price")
    private Long outcomePrice;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ForecastStatus status = ForecastStatus.PENDING;
}
