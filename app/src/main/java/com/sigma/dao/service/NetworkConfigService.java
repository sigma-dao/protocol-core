package com.sigma.dao.service;

import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.NetworkConfig;
import com.sigma.dao.repository.NetworkConfigRepository;
import com.sigma.dao.utils.JSONUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NetworkConfigService {

    @Setter
    @Getter
    private Long timestamp;

    private final NetworkConfigRepository networkConfigRepository;
    private final JSONUtils jsonUtils;

    public NetworkConfigService(NetworkConfigRepository networkConfigRepository,
                                JSONUtils jsonUtils) {
        this.networkConfigRepository = networkConfigRepository;
        this.jsonUtils = jsonUtils;
    }

    /**
     * Initializes the database with the starting network configuration
     */
    public void initializeNetworkConfig(JSONObject appState) {
        NetworkConfig networkConfig = jsonUtils.fromJson(
                appState.getJSONObject("networkConfig").toString(), NetworkConfig.class);
        this.networkConfigRepository.save(networkConfig);
    }

    /**
     * Increment the seed used to generate deterministic UUIDs
     */
    public void incrementUuidSeed() {
        networkConfigRepository.save(get().setUuidSeed(get().getUuidSeed() + 1));
    }

    /**
     * Fetches the network config
     *
     * @return the {@link NetworkConfig}
     */
    public NetworkConfig get() {
        return networkConfigRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ProtocolException(ErrorCode.E0023));
    }
}