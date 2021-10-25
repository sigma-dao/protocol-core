package com.sigma.dao.utils;

import com.sigma.dao.service.NetworkConfigService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class UUIDUtils {

    private final NetworkConfigService networkConfigService;

    public UUIDUtils(NetworkConfigService networkConfigService) {
        this.networkConfigService = networkConfigService;
    }

    /**
     * Get the next deterministic UUID
     *
     * @return the {@link UUID}
     */
    public UUID next() {
        UUID uuid = UUID.nameUUIDFromBytes(networkConfigService.get().getUuidSeed().toString()
                .getBytes(StandardCharsets.UTF_8));
        networkConfigService.incrementUuidSeed();
        return uuid;
    }
}