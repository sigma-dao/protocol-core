package com.sigma.dao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.NetworkConfig;
import com.sigma.dao.repository.NetworkConfigRepository;
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

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final NetworkConfigRepository networkConfigRepository;

    public NetworkConfigService(NetworkConfigRepository networkConfigRepository) {
        this.networkConfigRepository = networkConfigRepository;
    }

    /**
     * Initializes the database with the starting network configuration
     */
    public void initializeNetworkConfig(JSONObject appState) throws JsonProcessingException {
        NetworkConfig networkConfig = objectMapper.readValue(
                appState.getJSONObject("networkConfig").toString(), NetworkConfig.class);
        this.networkConfigRepository.save(networkConfig);
    }

    public void proposeNetworkConfigChange() {

    }

    public void voteOnNetworkConfigChange() {

    }

    /**
     * Fetches the network config
     *
     * @return the {@link NetworkConfig}
     */
    public NetworkConfig get() {
        return networkConfigRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ProtocolException("The network config is missing."));
    }
}