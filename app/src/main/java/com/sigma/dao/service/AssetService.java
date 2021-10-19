package com.sigma.dao.service;

import com.sigma.dao.constant.FundStatus;
import com.sigma.dao.constant.GovernanceActionType;
import com.sigma.dao.constant.GovernanceStatus;
import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.Asset;
import com.sigma.dao.model.GovernanceAction;
import com.sigma.dao.repository.AssetRepository;
import com.sigma.dao.repository.FundRepository;
import com.sigma.dao.repository.GovernanceActionRepository;
import com.sigma.dao.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final FundRepository fundRepository;
    private final GovernanceActionRepository governanceActionRepository;
    private final UUIDUtils uuidUtils;

    public AssetService(AssetRepository assetRepository,
                        FundRepository fundRepository,
                        GovernanceActionRepository governanceActionRepository,
                        UUIDUtils uuidUtils) {
        this.assetRepository = assetRepository;
        this.fundRepository = fundRepository;
        this.governanceActionRepository = governanceActionRepository;
        this.uuidUtils = uuidUtils;
    }

    /**
     * Add a new asset (must be approved by governance voting)
     *
     * @param asset the {@link Asset} instance
     *
     * @return the new {@link Asset}
     */
    public Asset add(Asset asset) {
        if(asset.getBlockchain() == null) {
            throw new ProtocolException(ErrorCode.E0017);
        }
        if(asset.getContractAddress() == null) {
            throw new ProtocolException(ErrorCode.E0018);
        }
        if(asset.getSymbol() == null) {
            throw new ProtocolException(ErrorCode.E0019);
        }
        Optional<Asset> assetOptional = assetRepository.findByBlockchainAndContractAddress(
                asset.getBlockchain(), asset.getContractAddress()).stream().findAny();
        if(assetOptional.isPresent()) {
            throw new ProtocolException(ErrorCode.E0022);
        }
        asset.setStatus(GovernanceStatus.SUBMITTED);
        asset.setId(uuidUtils.next());
        asset = assetRepository.save(asset);
        governanceActionRepository.save(new GovernanceAction()
                .setEntityId(asset.getId())
                .setVotesFor(0)
                .setVotesAgainst(0)
                .setType(GovernanceActionType.ADD_ASSET));
        return asset;
    }

    /**
     * Remove an asset (must be approved by governance voting)
     *
     * @param asset the {@link Asset} instance
     *
     * @return the {@link Asset}
     */
    public Asset remove(Asset asset) {
        if(asset.getId() == null) {
            throw new ProtocolException(ErrorCode.E0008);
        }
        boolean assetInUse = fundRepository.findBySubscriptionAsset(asset).stream()
                .anyMatch(f -> !f.getStatus().equals(FundStatus.TERMINATED));
        if(assetInUse) {
            throw new ProtocolException(ErrorCode.E0020);
        }
        Optional<Asset> assetOptional = assetRepository.findById(asset.getId());
        if(assetOptional.isEmpty()) {
            throw new ProtocolException(ErrorCode.E0021);
        }
        governanceActionRepository.save(new GovernanceAction()
                .setEntityId(asset.getId())
                .setVotesFor(0)
                .setVotesAgainst(0)
                .setType(GovernanceActionType.REMOVE_ASSET)
                .setId(uuidUtils.next()));
        return asset;
    }
}