package com.sigma.dao.service;

import com.sigma.dao.constant.GovernanceActionType;
import com.sigma.dao.constant.GovernanceStatus;
import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.Asset;
import com.sigma.dao.model.Fund;
import com.sigma.dao.repository.AssetRepository;
import com.sigma.dao.repository.FundRepository;
import com.sigma.dao.request.CreateFundRequest;
import com.sigma.dao.request.PaginatedRequest;
import com.sigma.dao.request.UpdateFundRequest;
import com.sigma.dao.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FundService {

    private final FundRepository fundRepository;
    private final NetworkConfigService networkConfigService;
    private final AssetRepository assetRepository;
    private final UUIDUtils uuidUtils;
    private final GovernanceService governanceService;

    public FundService(FundRepository fundRepository,
                       NetworkConfigService networkConfigService,
                       AssetRepository assetRepository,
                       UUIDUtils uuidUtils,
                       GovernanceService governanceService) {
        this.fundRepository = fundRepository;
        this.networkConfigService = networkConfigService;
        this.assetRepository = assetRepository;
        this.uuidUtils = uuidUtils;
        this.governanceService = governanceService;
    }

    /**
     * Returns all of the {@link Fund}s
     *
     * @param request {@link PaginatedRequest}
     *
     * @return a {@link List} of {@link Fund}s
     */
    public List<Fund> get(
            final PaginatedRequest request
    ) {
        final PageRequest page = PageRequest.of(request.getPage(), request.getSize(),
                Sort.Direction.valueOf(request.getSort()), "id");
        return this.fundRepository.findAll(page).toList();
    }

    /**
     * Creates a new {@link Fund}
     *
     * @param request the {@link CreateFundRequest} instance
     *
     * @return the new {@link Fund}
     */
    public Fund create(
            final CreateFundRequest request
    ) {
        long timestamp = networkConfigService.getTimestamp();
        long diff = request.getEnactmentDate() - timestamp;
        if(diff < networkConfigService.get().getMinFundEnactmentDelay()) {
            throw new ProtocolException(ErrorCode.E0013);
        }
        Asset subscriptionAsset = assetRepository.findById(request.getSubscriptionAssetId())
                .orElseThrow(() -> new ProtocolException(ErrorCode.E0021));
        if(!subscriptionAsset.getStatus().equals(GovernanceStatus.APPROVED)) {
            throw new ProtocolException(ErrorCode.E0024);
        }
        Fund fund = new Fund()
                .setDisbursementFrequency(request.getDisbursementFrequency())
                .setManagementFee(request.getManagementFee())
                .setMinimumSubscription(request.getMinimumSubscription())
                .setPerformanceFee(request.getPerformanceFee())
                .setRedemptionFrequency(request.getRedemptionFrequency())
                .setType(request.getType())
                .setStatus(GovernanceStatus.SUBMITTED)
                .setSubscriptionAsset(subscriptionAsset)
                .setId(uuidUtils.next());
        fund = fundRepository.save(fund);
        governanceService.create(fund.getId(), GovernanceActionType.CREATE_FUND,
                request.getEnactmentDate(), request.getOpeningDate(), request.getClosingDate());
        return fund;
    }

    /**
     * Updates an existing {@link Fund}
     *
     * @param request the {@link UpdateFundRequest} instance
     *
     * @return the updated {@link Fund}
     */
    public Fund update(
            final UpdateFundRequest request
    ) {
        Fund currentFund = fundRepository.findById(request.getId())
                .orElseThrow(() -> new ProtocolException(ErrorCode.E0009));
        if(currentFund.getStatus().equals(GovernanceStatus.PENDING_UPDATE)) {
            throw new ProtocolException(ErrorCode.E0035);
        }
        if(currentFund.getStatus().equals(GovernanceStatus.REJECTED)) {
            throw new ProtocolException(ErrorCode.E0036);
        }
        currentFund.setProposedPerformanceFee(request.getPerformanceFee());
        currentFund.setProposedMinimumSubscription(request.getMinimumSubscription());
        currentFund.setProposedDisbursementFrequency(request.getDisbursementFrequency());
        currentFund.setProposedRedemptionFrequency(request.getRedemptionFrequency());
        currentFund.setProposedManagementFee(request.getManagementFee());
        currentFund.setStatus(GovernanceStatus.PENDING_UPDATE);
        governanceService.create(currentFund.getId(), GovernanceActionType.UPDATE_FUND,
                request.getEnactmentDate(), request.getOpeningDate(), request.getClosingDate());
        return fundRepository.save(currentFund);
    }
}