package com.sigma.dao.service;

import com.sigma.dao.constant.Blockchain;
import com.sigma.dao.constant.GovernanceActionType;
import com.sigma.dao.constant.GovernanceStatus;
import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.Asset;
import com.sigma.dao.repository.AssetRepository;
import com.sigma.dao.repository.FundRepository;
import com.sigma.dao.request.AddAssetRequest;
import com.sigma.dao.request.PaginatedRequest;
import com.sigma.dao.request.RemoveAssetRequest;
import com.sigma.dao.response.GetAssetsResponse;
import com.sigma.dao.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
     * Returns a list of assets
     *
     * @param request {@link PaginatedRequest}
     *
     * @return {@link GetAssetsResponse}
     */
    public GetAssetsResponse get(
            final PaginatedRequest request
    ) {
        final PageRequest page = PageRequest.of(request.getPage(), request.getSize(),
                Sort.Direction.valueOf(request.getSort()), "symbol");
        return new GetAssetsResponse().setAssets(assetRepository.findAll(page).toList());
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
        Optional<Asset> assetOptional = assetRepository.findByBlockchainAndContractAddress(
                Blockchain.valueOf(request.getBlockchain()), request.getContractAddress()).stream().findAny();
        if(assetOptional.isPresent()) {
            throw new ProtocolException(ErrorCode.E0022);
        }
        Asset asset = new Asset()
                .setBlockchain(Blockchain.valueOf(request.getBlockchain()))
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
        Asset asset = assetRepository.findById(request.getId())
                .orElseThrow(() -> new ProtocolException(ErrorCode.E0021));
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