package com.sigma.dao.service;

import com.sigma.dao.model.Fund;
import com.sigma.dao.repository.AssetRepository;
import com.sigma.dao.repository.FundRepository;
import com.sigma.dao.repository.GovernanceActionRepository;
import com.sigma.dao.utils.UUIDUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

public class FundServiceTest {

    private FundService fundService;
    private FundRepository fundRepository;
    private NetworkConfigService networkConfigService;
    private AssetRepository assetRepository;
    private UUIDUtils uuidUtils;
    private GovernanceActionRepository governanceActionRepository;

    @Before
    public void setup() {
        uuidUtils = Mockito.mock(UUIDUtils.class);
        fundRepository = Mockito.mock(FundRepository.class);
        networkConfigService = Mockito.mock(NetworkConfigService.class);
        assetRepository = Mockito.mock(AssetRepository.class);
        governanceActionRepository = Mockito.mock(GovernanceActionRepository.class);
        fundService = new FundService(fundRepository, networkConfigService, assetRepository, uuidUtils, governanceActionRepository);
    }
//
//    @Test
//    public void testCreateFundMissingDisbursementFrequency() {
//        try {
//            fundService.create(new Fund());
//            Assert.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0001);
//        }
//    }
//
//    @Test
//    public void testCreateFundMissingManagementFee() {
//        try {
//            fundService.create(new Fund().
//                    setDisbursementFrequency(1L));
//            Assert.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0002);
//        }
//    }
//
//    @Test
//    public void testCreateFundMissingMinimumSubscription() {
//        try {
//            fundService.create(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1));
//            Assert.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0003);
//        }
//    }
//
//    @Test
//    public void testCreateFundMissingPerformanceFee() {
//        try {
//            fundService.create(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1)
//                    .setMinimumSubscription(1L));
//            Assert.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0004);
//        }
//    }
//
//    @Test
//    public void testCreateFundMissingRedemptionFrequency() {
//        try {
//            fundService.create(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1)
//                    .setMinimumSubscription(1L)
//                    .setPerformanceFee(1));
//            Assert.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0005);
//        }
//    }
//
//    @Test
//    public void testCreateFundMissingSubscriptionAsset() {
//        try {
//            fundService.create(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1)
//                    .setMinimumSubscription(1L)
//                    .setPerformanceFee(1)
//                    .setRedemptionFrequency(1L));
//            Assert.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0006);
//        }
//    }
//
//    @Test
//    public void testCreateFundMissingType() {
//        try {
//            fundService.create(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1)
//                    .setMinimumSubscription(1L)
//                    .setPerformanceFee(1)
//                    .setRedemptionFrequency(1L)
//                    .setSubscriptionAsset(new Asset()));
//            Assert.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0007);
//        }
//    }
//
//    @Test
//    public void testCreateFundMissingActivationDate() {
//        try {
//            fundService.create(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1)
//                    .setMinimumSubscription(1L)
//                    .setPerformanceFee(1)
//                    .setRedemptionFrequency(1L)
//                    .setSubscriptionAsset(new Asset())
//                    .setType(FundType.OPEN_ENDED));
//            Assert.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0012);
//        }
//    }
//
//    @Test
//    public void testCreateFundInvalidActivationDate() {
//        UUID id = UUID.randomUUID();
//        long ts = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
//        Mockito.when(fundRepository.save(Mockito.any(Fund.class))).thenReturn(new Fund().setId(id));
//        Mockito.when(networkConfigService.getTimestamp()).thenReturn(ts);
//        Mockito.when(networkConfigService.get()).thenReturn(new NetworkConfig().setMinFundActivationTime(300L));
//        try {
//            fundService.create(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1)
//                    .setMinimumSubscription(1L)
//                    .setPerformanceFee(1)
//                    .setRedemptionFrequency(1L)
//                    .setSubscriptionAsset(new Asset())
//                    .setType(FundType.OPEN_ENDED)
//                    .setActivationDate(ts));
//            Assert.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0013);
//        }
//    }
//
//    @Test
//    public void testCreateFundInvalidSubscriptionAsset() {
//        UUID id = UUID.randomUUID();
//        long ts = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
//        Mockito.when(fundRepository.save(Mockito.any(Fund.class))).thenReturn(new Fund().setId(id));
//        Mockito.when(networkConfigService.getTimestamp()).thenReturn(ts);
//        Mockito.when(networkConfigService.get()).thenReturn(new NetworkConfig().setMinFundActivationTime(300L));
//        try {
//            fundService.create(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1)
//                    .setMinimumSubscription(1L)
//                    .setPerformanceFee(1)
//                    .setRedemptionFrequency(1L)
//                    .setSubscriptionAsset(new Asset().setId(UUID.randomUUID()))
//                    .setType(FundType.OPEN_ENDED)
//                    .setActivationDate(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault())
//                            .toInstant().toEpochMilli()));
//            Assert.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0021);
//        }
//    }
//
//    @Test
//    public void testCreateFundIncorrectAssetStatus() {
//        UUID id = UUID.randomUUID();
//        UUID assetId = UUID.randomUUID();
//        Mockito.when(assetRepository.findById(Mockito.any(UUID.class)))
//                .thenReturn(Optional.of(new Asset().setId(assetId).setStatus(GovernanceStatus.SUBMITTED)));
//        Mockito.when(fundRepository.save(Mockito.any(Fund.class))).thenReturn(new Fund().setId(id));
//        Mockito.when(networkConfigService.get()).thenReturn(new NetworkConfig().setMinFundActivationTime(300L));
//        try {
//            fundService.create(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1)
//                    .setMinimumSubscription(1L)
//                    .setPerformanceFee(1)
//                    .setRedemptionFrequency(1L)
//                    .setSubscriptionAsset(new Asset().setId(assetId))
//                    .setType(FundType.OPEN_ENDED)
//                    .setActivationDate(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault())
//                            .toInstant().toEpochMilli()));
//            Assert.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0024);
//        }
//    }
//
//    @Test
//    public void testCreateFund() {
//        UUID id = UUID.randomUUID();
//        UUID assetId = UUID.randomUUID();
//        Mockito.when(assetRepository.findById(Mockito.any(UUID.class)))
//                .thenReturn(Optional.of(new Asset().setId(assetId).setStatus(GovernanceStatus.APPROVED)));
//        Mockito.when(fundRepository.save(Mockito.any(Fund.class))).thenReturn(new Fund().setId(id));
//        Mockito.when(networkConfigService.get()).thenReturn(new NetworkConfig().setMinFundActivationTime(300L));
//        Fund fund = fundService.create(new Fund()
//                .setDisbursementFrequency(1L)
//                .setManagementFee(1)
//                .setMinimumSubscription(1L)
//                .setPerformanceFee(1)
//                .setRedemptionFrequency(1L)
//                .setSubscriptionAsset(new Asset().setId(assetId))
//                .setType(FundType.OPEN_ENDED)
//                .setActivationDate(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault())
//                        .toInstant().toEpochMilli()));
//        Assertions.assertEquals(id, fund.getId());
//    }

    @Test
    public void testUpdateFundMissingID() {
//        try {
//            fundService.update(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1)
//                    .setMinimumSubscription(1L)
//                    .setPerformanceFee(1)
//                    .setRedemptionFrequency(1L)
//                    .setSubscriptionAsset(new Asset())
//                    .setType(FundType.OPEN_ENDED));
//            Assertions.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0008);
//        }
    }

//    @Test
//    public void testUpdateFundInvalidStatus() {
//        long activationDate = LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault())
//                .toInstant().toEpochMilli();
//        Mockito.when(networkConfigService.get()).thenReturn(new NetworkConfig().setMinFundActivationTime(300L));
//        Mockito.when(fundRepository.findById(Mockito.any(UUID.class)))
//                .thenReturn(Optional.of(new Fund().setActivationDate(activationDate).setStatus(FundStatus.ACTIVE)));
//        try {
//            fundService.update(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1)
//                    .setMinimumSubscription(1L)
//                    .setPerformanceFee(1)
//                    .setRedemptionFrequency(1L)
//                    .setSubscriptionAsset(new Asset())
//                    .setType(FundType.OPEN_ENDED)
//                    .setId(UUID.randomUUID())
//                    .setActivationDate(activationDate));
//            Assertions.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0010);
//        }
//    }
//
//    @Test
//    public void testUpdateFundInvalidID() {
//        long activationDate = LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault())
//                .toInstant().toEpochMilli();
//        Mockito.when(networkConfigService.get()).thenReturn(new NetworkConfig().setMinFundActivationTime(300L));
//        Mockito.when(fundRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
//        try {
//            fundService.update(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1)
//                    .setMinimumSubscription(1L)
//                    .setPerformanceFee(1)
//                    .setRedemptionFrequency(1L)
//                    .setSubscriptionAsset(new Asset())
//                    .setType(FundType.OPEN_ENDED)
//                    .setId(UUID.randomUUID())
//                    .setActivationDate(activationDate));
//            Assertions.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0009);
//        }
//    }
//
//    @Test
//    public void testUpdateFundDifferentActivationDate() {
//        long activationDate = LocalDateTime.now().atZone(ZoneId.systemDefault())
//                .toInstant().toEpochMilli();
//        Mockito.when(networkConfigService.get()).thenReturn(new NetworkConfig().setMinFundActivationTime(300L));
//        Mockito.when(fundRepository.findById(Mockito.any(UUID.class)))
//                .thenReturn(Optional.of(new Fund().setActivationDate(activationDate).setStatus(FundStatus.PROPOSED)));
//        Mockito.when(fundRepository.save(Mockito.any(Fund.class))).thenReturn(new Fund());
//        try {
//            fundService.update(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1)
//                    .setMinimumSubscription(1L)
//                    .setPerformanceFee(1)
//                    .setRedemptionFrequency(1L)
//                    .setSubscriptionAsset(new Asset())
//                    .setType(FundType.OPEN_ENDED)
//                    .setId(UUID.randomUUID())
//                    .setActivationDate(activationDate + 1));
//            Assertions.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0011);
//        }
//    }
//
//    @Test
//    public void testUpdateFundDifferentStatus() {
//        long activationDate = LocalDateTime.now().atZone(ZoneId.systemDefault())
//                .toInstant().toEpochMilli();
//        Mockito.when(networkConfigService.get()).thenReturn(new NetworkConfig().setMinFundActivationTime(300L));
//        Mockito.when(fundRepository.findById(Mockito.any(UUID.class)))
//                .thenReturn(Optional.of(new Fund().setActivationDate(activationDate).setStatus(FundStatus.PROPOSED)));
//        Mockito.when(fundRepository.save(Mockito.any(Fund.class))).thenReturn(new Fund());
//        try {
//            fundService.update(new Fund()
//                    .setDisbursementFrequency(1L)
//                    .setManagementFee(1)
//                    .setMinimumSubscription(1L)
//                    .setPerformanceFee(1)
//                    .setRedemptionFrequency(1L)
//                    .setSubscriptionAsset(new Asset())
//                    .setType(FundType.OPEN_ENDED)
//                    .setId(UUID.randomUUID())
//                    .setStatus(FundStatus.TERMINATED)
//                    .setActivationDate(activationDate ));
//            Assertions.fail();
//        } catch(ProtocolException e) {
//            Assertions.assertEquals(e.getMessage(), ErrorCode.E0014);
//        }
//    }
//
//    @Test
//    public void testUpdateFund() {
//        long activationDate = LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault())
//                .toInstant().toEpochMilli();
//        UUID assetId = UUID.randomUUID();
//        Mockito.when(assetRepository.findById(Mockito.any(UUID.class)))
//                .thenReturn(Optional.of(new Asset().setId(assetId)));
//        Mockito.when(networkConfigService.get()).thenReturn(new NetworkConfig().setMinFundActivationTime(300L));
//        Mockito.when(fundRepository.findById(Mockito.any(UUID.class)))
//                .thenReturn(Optional.of(new Fund().setSubscriptionAsset(new Asset().setId(assetId))
//                        .setActivationDate(activationDate).setStatus(FundStatus.PROPOSED)));
//        Mockito.when(fundRepository.save(Mockito.any(Fund.class))).thenReturn(new Fund());
//        Fund fund = fundService.update(new Fund()
//                .setDisbursementFrequency(1L)
//                .setManagementFee(1)
//                .setMinimumSubscription(1L)
//                .setPerformanceFee(1)
//                .setRedemptionFrequency(1L)
//                .setStatus(FundStatus.PROPOSED)
//                .setSubscriptionAsset(new Asset().setId(assetId))
//                .setType(FundType.OPEN_ENDED)
//                .setId(UUID.randomUUID())
//                .setActivationDate(activationDate));
//        Assertions.assertNotNull(fund);
//    }

    @Test
    public void testGetFunds() {
        UUID id = UUID.randomUUID();
        Mockito.when(fundRepository.findAll()).thenReturn(List.of(new Fund().setId(id)));
        List<Fund> funds = fundService.get();
        Assertions.assertEquals(id, funds.get(0).getId());
    }
}