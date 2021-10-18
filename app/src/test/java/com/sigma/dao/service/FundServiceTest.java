package com.sigma.dao.service;

import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.Asset;
import com.sigma.dao.model.Fund;
import com.sigma.dao.model.constant.FundType;
import com.sigma.dao.model.repository.FundRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class FundServiceTest {

    private FundService fundService;
    private FundRepository fundRepository;

    @Before
    public void setup() {
        fundRepository = Mockito.mock(FundRepository.class);
        fundService = new FundService(fundRepository);
    }

    @Test
    public void testCreateFundMissingDisbursementFrequency() {
        try {
            fundService.create(new Fund());
            Assert.fail();
        } catch(ProtocolException e) {
            Assertions.assertEquals(e.getMessage(), ErrorCode.E0001);
        }
    }

    @Test
    public void testCreateFundMissingManagementFee() {
        try {
            fundService.create(new Fund().
                    setDisbursementFrequency(1L));
            Assert.fail();
        } catch(ProtocolException e) {
            Assertions.assertEquals(e.getMessage(), ErrorCode.E0002);
        }
    }

    @Test
    public void testCreateFundMissingMinimumSubscription() {
        try {
            fundService.create(new Fund()
                    .setDisbursementFrequency(1L)
                    .setManagementFee(1));
            Assert.fail();
        } catch(ProtocolException e) {
            Assertions.assertEquals(e.getMessage(), ErrorCode.E0003);
        }
    }

    @Test
    public void testCreateFundMissingPerformanceFee() {
        try {
            fundService.create(new Fund()
                    .setDisbursementFrequency(1L)
                    .setManagementFee(1)
                    .setMinimumSubscription(1L));
            Assert.fail();
        } catch(ProtocolException e) {
            Assertions.assertEquals(e.getMessage(), ErrorCode.E0004);
        }
    }

    @Test
    public void testCreateFundMissingRedemptionFrequency() {
        try {
            fundService.create(new Fund()
                    .setDisbursementFrequency(1L)
                    .setManagementFee(1)
                    .setMinimumSubscription(1L)
                    .setPerformanceFee(1));
            Assert.fail();
        } catch(ProtocolException e) {
            Assertions.assertEquals(e.getMessage(), ErrorCode.E0005);
        }
    }

    @Test
    public void testCreateFundMissingSubscriptionAsset() {
        try {
            fundService.create(new Fund()
                    .setDisbursementFrequency(1L)
                    .setManagementFee(1)
                    .setMinimumSubscription(1L)
                    .setPerformanceFee(1)
                    .setRedemptionFrequency(1L));
            Assert.fail();
        } catch(ProtocolException e) {
            Assertions.assertEquals(e.getMessage(), ErrorCode.E0006);
        }
    }

    @Test
    public void testCreateFundMissingType() {
        try {
            fundService.create(new Fund()
                    .setDisbursementFrequency(1L)
                    .setManagementFee(1)
                    .setMinimumSubscription(1L)
                    .setPerformanceFee(1)
                    .setRedemptionFrequency(1L)
                    .setSubscriptionAsset(new Asset()));
            Assert.fail();
        } catch(ProtocolException e) {
            Assertions.assertEquals(e.getMessage(), ErrorCode.E0007);
        }
    }

    @Test
    public void testCreateFund() {
        UUID id = UUID.randomUUID();
        Mockito.when(fundRepository.save(Mockito.any(Fund.class))).thenReturn(new Fund().setId(id));
        Fund fund = fundService.create(new Fund()
                .setDisbursementFrequency(1L)
                .setManagementFee(1)
                .setMinimumSubscription(1L)
                .setPerformanceFee(1)
                .setRedemptionFrequency(1L)
                .setSubscriptionAsset(new Asset())
                .setType(FundType.OPEN_ENDED));
        Assertions.assertEquals(id, fund.getId());
    }

    @Test
    public void testUpdateFundMissingID() {
        try {
            fundService.update(new Fund()
                    .setDisbursementFrequency(1L)
                    .setManagementFee(1)
                    .setMinimumSubscription(1L)
                    .setPerformanceFee(1)
                    .setRedemptionFrequency(1L)
                    .setSubscriptionAsset(new Asset())
                    .setType(FundType.OPEN_ENDED));
            Assertions.fail();
        } catch(ProtocolException e) {
            Assertions.assertEquals(e.getMessage(), ErrorCode.E0008);
        }
    }

    @Test
    public void testUpdateFund() {
        Mockito.when(fundRepository.save(Mockito.any(Fund.class))).thenReturn(new Fund());
        Fund fund = fundService.update(new Fund()
                .setDisbursementFrequency(1L)
                .setManagementFee(1)
                .setMinimumSubscription(1L)
                .setPerformanceFee(1)
                .setRedemptionFrequency(1L)
                .setSubscriptionAsset(new Asset())
                .setType(FundType.OPEN_ENDED)
                .setId(UUID.randomUUID()));
        Assertions.assertNotNull(fund);
    }
}