package com.sigma.dao.controller;

import com.sigma.dao.SigmaDAO;
import com.sigma.dao.constant.Blockchain;
import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.request.AddAssetRequest;
import com.sigma.dao.request.SignedRequest;
import com.sigma.dao.response.ErrorResponse;
import com.sigma.dao.service.NetworkConfigService;
import com.sigma.dao.utils.JSONUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@SpringBootTest(classes = SigmaDAO.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AssetControllerTest {

    @Autowired
    private NetworkConfigService networkConfigService;
    @Autowired
    private JSONUtils jsonUtils;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        JSONObject appState = new JSONObject().put("networkConfig",
                new JSONObject()
                        .put("governanceTokenAddress", "0x0")
                        .put("minFundEnactmentDelay", 1)
                        .put("maxFundEnactmentDelay", 1)
                        .put("minAssetEnactmentDelay", 1)
                        .put("maxAssetEnactmentDelay", 1)
                        .put("uuidSeed", 1));
        networkConfigService.initializeNetworkConfig(appState);
        networkConfigService.setTimestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    }

    private void testAddAssetMissingField(SignedRequest request, String error) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/asset")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonUtils.toJson(request)))
                .andReturn().getResponse();
        Assertions.assertEquals(response.getStatus(), 500);
        ErrorResponse errorResponse = jsonUtils.fromJson(response.getContentAsString(), ErrorResponse.class);
        Assertions.assertEquals(errorResponse.getError(), error);
    }

    @Test
    public void testAddAssetMissingBlockchain() throws Exception {
        SignedRequest request = new AddAssetRequest()
                .setSymbol("BTC")
                .setContractAddress("0x0")
                .setClosingDate(1L)
                .setOpeningDate(1L)
                .setEnactmentDate(1L)
                .setPublicKey("0x0")
                .setSignature("0x0");
        testAddAssetMissingField(request, ErrorCode.E0017);
    }

    @Test
    public void testAddAssetMissingContractAddress() throws Exception {
        SignedRequest request = new AddAssetRequest()
                .setSymbol("BTC")
                .setBlockchain(Blockchain.ETHEREUM.toString())
                .setClosingDate(1L)
                .setOpeningDate(1L)
                .setEnactmentDate(1L)
                .setPublicKey("0x0")
                .setSignature("0x0");
        testAddAssetMissingField(request, ErrorCode.E0018);
    }

    @Test
    public void testAddAssetMissingSymbol() throws Exception {
        SignedRequest request = new AddAssetRequest()
                .setContractAddress("0x0")
                .setBlockchain(Blockchain.ETHEREUM.toString())
                .setClosingDate(1L)
                .setOpeningDate(1L)
                .setEnactmentDate(1L)
                .setPublicKey("0x0")
                .setSignature("0x0");
        testAddAssetMissingField(request, ErrorCode.E0019);
    }

    @Test
    public void testAddAssetMissingClosingDate() throws Exception {
        SignedRequest request = new AddAssetRequest()
                .setContractAddress("0x0")
                .setBlockchain(Blockchain.ETHEREUM.toString())
                .setSymbol("BTC")
                .setOpeningDate(1L)
                .setEnactmentDate(1L)
                .setPublicKey("0x0")
                .setSignature("0x0");
        testAddAssetMissingField(request, ErrorCode.E0034);
    }

    @Test
    public void testAddAssetMissingOpeningDate() throws Exception {
        SignedRequest request = new AddAssetRequest()
                .setContractAddress("0x0")
                .setBlockchain(Blockchain.ETHEREUM.toString())
                .setSymbol("BTC")
                .setClosingDate(1L)
                .setEnactmentDate(1L)
                .setPublicKey("0x0")
                .setSignature("0x0");
        testAddAssetMissingField(request, ErrorCode.E0033);
    }

    @Test
    public void testAddAssetMissingEnactmentDate() throws Exception {
        SignedRequest request = new AddAssetRequest()
                .setContractAddress("0x0")
                .setBlockchain(Blockchain.ETHEREUM.toString())
                .setSymbol("BTC")
                .setClosingDate(1L)
                .setOpeningDate(1L)
                .setPublicKey("0x0")
                .setSignature("0x0");
        testAddAssetMissingField(request, ErrorCode.E0039);
    }

    @Test
    public void testAddAssetMissingPublicKey() throws Exception {
        SignedRequest request = new AddAssetRequest()
                .setContractAddress("0x0")
                .setBlockchain(Blockchain.ETHEREUM.toString())
                .setSymbol("BTC")
                .setClosingDate(1L)
                .setOpeningDate(1L)
                .setEnactmentDate(1L)
                .setSignature("0x0");
        testAddAssetMissingField(request, ErrorCode.E0041);
    }

    @Test
    public void testAddAssetMissingSignature() throws Exception {
        SignedRequest request = new AddAssetRequest()
                .setContractAddress("0x0")
                .setBlockchain(Blockchain.ETHEREUM.toString())
                .setSymbol("BTC")
                .setClosingDate(1L)
                .setOpeningDate(1L)
                .setEnactmentDate(1L)
                .setPublicKey("0x0");
        testAddAssetMissingField(request, ErrorCode.E0040);
    }

    @Test
    public void testAddAsset() throws Exception {
        SignedRequest request = new AddAssetRequest()
                .setContractAddress("0x0")
                .setBlockchain(Blockchain.ETHEREUM.toString())
                .setSymbol("BTC")
                .setClosingDate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + 2000)
                .setOpeningDate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + 1000)
                .setEnactmentDate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + 3000)
                .setPublicKey("0x0")
                .setSignature("0x0");
        MockHttpServletResponse response = mockMvc.perform(post("/asset")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonUtils.toJson(request)))
                .andReturn().getResponse();
        Assertions.assertEquals(response.getStatus(), 200);
        // TODO - check response
    }

//    @Test
//    public void testAddAssetDuplicated() {
//        Mockito.when(assetRepository.findByBlockchainAndContractAddress(
//                Mockito.any(Blockchain.class), Mockito.any())).thenReturn(List.of(new Asset()));
//        try {
//            assetService.add(new AddAssetRequest()
//                    .setBlockchain(Blockchain.ETHEREUM)
//                    .setContractAddress("0x0")
//                    .setSymbol("XYZ"));
//            Assertions.fail();
//        } catch(Exception e) {
//            Assertions.assertEquals(ErrorCode.E0022, e.getMessage());
//        }
//    }

//
//    @Test
//    public void testRemoveAssetMissingID() {
//        try {
//            assetService.remove(new Asset());
//            Assertions.fail();
//        } catch(Exception e) {
//            Assertions.assertEquals(ErrorCode.E0008, e.getMessage());
//        }
//    }
//
//    @Test
//    public void testRemoveAssetInUse() {
//        Mockito.when(fundRepository.findBySubscriptionAsset(Mockito.any(Asset.class)))
//                .thenReturn(List.of(new Fund().setStatus(FundStatus.ACTIVE)));
//        try {
//            assetService.remove(new Asset().setId(UUID.randomUUID()));
//            Assertions.fail();
//        } catch(Exception e) {
//            Assertions.assertEquals(ErrorCode.E0020, e.getMessage());
//        }
//    }
//
//    @Test
//    public void testRemoveAssetNotFound() {
//        Mockito.when(fundRepository.findBySubscriptionAsset(Mockito.any(Asset.class)))
//                .thenReturn(List.of(new Fund().setStatus(FundStatus.TERMINATED)));
//        Mockito.when(assetRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
//        try {
//            assetService.remove(new Asset().setId(UUID.randomUUID()));
//            Assertions.fail();
//        } catch(Exception e) {
//            Assertions.assertEquals(ErrorCode.E0021, e.getMessage());
//        }
//    }
//
//    @Test
//    public void testRemoveAsset() {
//        Mockito.when(fundRepository.findBySubscriptionAsset(Mockito.any(Asset.class)))
//                .thenReturn(List.of(new Fund().setStatus(FundStatus.TERMINATED)));
//        Mockito.when(assetRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(new Asset()));
//        Mockito.when(governanceActionRepository.save(Mockito.any(GovernanceAction.class)))
//                .thenReturn(new GovernanceAction());
//        Asset asset = assetService.remove(new Asset().setId(UUID.randomUUID()));
//        Assertions.assertNotNull(asset);
//        Mockito.verify(governanceActionRepository, Mockito.times(1)).save(Mockito.any());
//    }
}