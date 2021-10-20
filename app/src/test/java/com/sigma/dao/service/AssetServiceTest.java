package com.sigma.dao.service;

import com.sigma.dao.constant.Blockchain;
import com.sigma.dao.constant.FundStatus;
import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.model.Asset;
import com.sigma.dao.model.Fund;
import com.sigma.dao.model.GovernanceAction;
import com.sigma.dao.repository.AssetRepository;
import com.sigma.dao.repository.FundRepository;
import com.sigma.dao.repository.GovernanceActionRepository;
import com.sigma.dao.utils.UUIDUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AssetServiceTest {

    private AssetService assetService;
    private AssetRepository assetRepository;
    private FundRepository fundRepository;
    private GovernanceActionRepository governanceActionRepository;
    private UUIDUtils uuidUtils;

    @Before
    public void setup() {
        uuidUtils = Mockito.mock(UUIDUtils.class);
        fundRepository = Mockito.mock(FundRepository.class);
        assetRepository = Mockito.mock(AssetRepository.class);
        governanceActionRepository = Mockito.mock(GovernanceActionRepository.class);
        assetService = new AssetService(assetRepository, fundRepository, governanceActionRepository, uuidUtils);
    }

    @Test
    public void testAddAssetMissingBlockchain() {
        try {
            assetService.add(new Asset());
            Assertions.fail();
        } catch(Exception e) {
            Assertions.assertEquals(ErrorCode.E0017, e.getMessage());
        }
    }

    @Test
    public void testAddAssetMissingContractAddress() {
        try {
            assetService.add(new Asset()
                    .setBlockchain(Blockchain.ETHEREUM));
            Assertions.fail();
        } catch(Exception e) {
            Assertions.assertEquals(ErrorCode.E0018, e.getMessage());
        }
    }

    @Test
    public void testAddAssetMissingSymbol() {
        try {
            assetService.add(new Asset()
                    .setBlockchain(Blockchain.ETHEREUM)
                    .setContractAddress("0x0"));
            Assertions.fail();
        } catch(Exception e) {
            Assertions.assertEquals(ErrorCode.E0019, e.getMessage());
        }
    }

    @Test
    public void testAddAssetDuplicated() {
        Mockito.when(assetRepository.findByBlockchainAndContractAddress(
                Mockito.any(Blockchain.class), Mockito.any())).thenReturn(List.of(new Asset()));
        try {
            assetService.add(new Asset()
                    .setBlockchain(Blockchain.ETHEREUM)
                    .setContractAddress("0x0")
                    .setSymbol("XYZ"));
            Assertions.fail();
        } catch(Exception e) {
            Assertions.assertEquals(ErrorCode.E0022, e.getMessage());
        }
    }

    @Test
    public void testAddAsset() {
        Mockito.when(assetRepository.findByBlockchainAndContractAddress(
                Mockito.any(Blockchain.class), Mockito.any())).thenReturn(List.of());
        Mockito.when(assetRepository.save(Mockito.any(Asset.class))).thenReturn(new Asset());
        Mockito.when(governanceActionRepository.save(Mockito.any(GovernanceAction.class)))
                .thenReturn(new GovernanceAction());
        Asset asset = assetService.add(new Asset()
                .setBlockchain(Blockchain.ETHEREUM)
                .setContractAddress("0x0")
                .setSymbol("XYZ"));
        Assertions.assertNotNull(asset);
        Mockito.verify(governanceActionRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testRemoveAssetMissingID() {
        try {
            assetService.remove(new Asset());
            Assertions.fail();
        } catch(Exception e) {
            Assertions.assertEquals(ErrorCode.E0008, e.getMessage());
        }
    }

    @Test
    public void testRemoveAssetInUse() {
        Mockito.when(fundRepository.findBySubscriptionAsset(Mockito.any(Asset.class)))
                .thenReturn(List.of(new Fund().setStatus(FundStatus.ACTIVE)));
        try {
            assetService.remove(new Asset().setId(UUID.randomUUID()));
            Assertions.fail();
        } catch(Exception e) {
            Assertions.assertEquals(ErrorCode.E0020, e.getMessage());
        }
    }

    @Test
    public void testRemoveAssetNotFound() {
        Mockito.when(fundRepository.findBySubscriptionAsset(Mockito.any(Asset.class)))
                .thenReturn(List.of(new Fund().setStatus(FundStatus.TERMINATED)));
        Mockito.when(assetRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
        try {
            assetService.remove(new Asset().setId(UUID.randomUUID()));
            Assertions.fail();
        } catch(Exception e) {
            Assertions.assertEquals(ErrorCode.E0021, e.getMessage());
        }
    }

    @Test
    public void testRemoveAsset() {
        Mockito.when(fundRepository.findBySubscriptionAsset(Mockito.any(Asset.class)))
                .thenReturn(List.of(new Fund().setStatus(FundStatus.TERMINATED)));
        Mockito.when(assetRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(new Asset()));
        Mockito.when(governanceActionRepository.save(Mockito.any(GovernanceAction.class)))
                .thenReturn(new GovernanceAction());
        Asset asset = assetService.remove(new Asset().setId(UUID.randomUUID()));
        Assertions.assertNotNull(asset);
        Mockito.verify(governanceActionRepository, Mockito.times(1)).save(Mockito.any());
    }
}