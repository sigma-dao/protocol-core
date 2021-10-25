package com.sigma.dao.service;

import com.sigma.dao.constant.GovernanceActionType;
import com.sigma.dao.constant.GovernanceStatus;
import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.Asset;
import com.sigma.dao.repository.AssetRepository;
import com.sigma.dao.repository.FundRepository;
import com.sigma.dao.request.AddAssetRequest;
import com.sigma.dao.request.RemoveAssetRequest;
import com.sigma.dao.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final FundRepository fundRepository;
    private final GovernanceService governanceService;
    private final UUIDUtils uuidUtils;

    public AssetService(AssetRepository assetRepository,
                        FundRepository fundRepository,
                        GovernanceService governanceService,
                        UUIDUtils uuidUtils) {
        this.assetRepository = assetRepository;
        this.fundRepository = fundRepository;
        this.governanceService = governanceService;
        this.uuidUtils = uuidUtils;
    }

    /**
     * Add a new asset (must be approved by governance voting)
     *
     * @param request {@link AddAssetRequest}
     *
     * @return the new {@link Asset}
     */
    public Asset add(
            final AddAssetRequest request
    ) {
        if(request.getBlockchain() == null) {
            throw new ProtocolException(ErrorCode.E0017);
        }
        if(request.getContractAddress() == null) {
            throw new ProtocolException(ErrorCode.E0018);
        }
        if(request.getSymbol() == null) {
            throw new ProtocolException(ErrorCode.E0019);
        }
        Optional<Asset> assetOptional = assetRepository.findByBlockchainAndContractAddress(
                request.getBlockchain(), request.getContractAddress()).stream().findAny();
        if(assetOptional.isPresent()) {
            throw new ProtocolException(ErrorCode.E0022);
        }
        Asset asset = new Asset()
                .setBlockchain(request.getBlockchain())
                .setSymbol(request.getSymbol())
                .setContractAddress(request.getContractAddress())
                .setStatus(GovernanceStatus.SUBMITTED)
                .setId(uuidUtils.next());
        asset = assetRepository.save(asset);
        governanceService.create(asset.getId(), GovernanceActionType.ADD_ASSET,
                request.getEnactmentDate(), request.getOpeningDate(), request.getClosingDate());
        return asset;
    }

    /**
     * Remove an asset (must be approved by governance voting)
     *
     * @param request the {@link RemoveAssetRequest} instance
     *
     * @return the {@link Asset}
     */
    public Asset remove(
            final RemoveAssetRequest request
    ) {
        if(request.getId() == null) {
            throw new ProtocolException(ErrorCode.E0008);
        }
        Asset asset = assetRepository.findById(request.getId()).orElseThrow(() -> new ProtocolException(ErrorCode.E0021));
        boolean assetInUse = fundRepository.findBySubscriptionAsset(asset).stream()
                .anyMatch(f -> !f.getStatus().equals(GovernanceStatus.REJECTED));
        if(assetInUse) {
            throw new ProtocolException(ErrorCode.E0020);
        }
        governanceService.create(asset.getId(), GovernanceActionType.REMOVE_ASSET,
                request.getEnactmentDate(), request.getOpeningDate(), request.getClosingDate());
        return asset;
    }
}