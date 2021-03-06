package com.sigma.dao.service;

import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.model.NetworkConfig;
import com.sigma.dao.repository.NetworkConfigRepository;
import com.sigma.dao.utils.JSONUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.List;

@Ignore
public class NetworkConfigServiceTest {

    private NetworkConfigService networkConfigService;
    private NetworkConfigRepository networkConfigRepository;
    private JSONUtils jsonUtils;

    @Before
    public void setup() {
        networkConfigRepository = Mockito.mock(NetworkConfigRepository.class);
        jsonUtils = Mockito.mock(JSONUtils.class);
        networkConfigService = new NetworkConfigService(networkConfigRepository, jsonUtils);
    }

    @Test
    public void testInitializeNetworkConfig() {
        JSONObject appState = new JSONObject()
                .put("networkConfig", new JSONObject()
                        .put("minFundEnactmentDelay", 1)
                        .put("uuidSeed", 1));
        networkConfigService.initializeNetworkConfig(appState);
        Mockito.verify(networkConfigRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testIncrementSeed() {
        Mockito.when(networkConfigRepository.findAll()).thenReturn(List.of(new NetworkConfig().setUuidSeed(1L)));
        networkConfigService.incrementUuidSeed();
        Mockito.verify(networkConfigRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testIncrementSeedMissingConfig() {
        Mockito.when(networkConfigRepository.findAll()).thenReturn(List.of());
        try {
            networkConfigService.incrementUuidSeed();
            Assertions.fail();
        } catch(Exception e) {
            Assertions.assertEquals(ErrorCode.E0023, e.getMessage());
        }
    }

    @Test
    public void testSetTimestamp() {
        networkConfigService.setTimestamp(1L);
        Assertions.assertEquals(networkConfigService.getTimestamp(), 1L);
    }
}