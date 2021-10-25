package com.sigma.dao.service;

import com.sigma.dao.constant.GovernanceActionType;
import com.sigma.dao.constant.GovernanceStatus;
import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.Asset;
import com.sigma.dao.model.Fund;
import com.sigma.dao.model.GovernanceAction;
import com.sigma.dao.repository.AssetRepository;
import com.sigma.dao.repository.FundRepository;
import com.sigma.dao.repository.GovernanceActionRepository;
import com.sigma.dao.request.CreateFundRequest;
import com.sigma.dao.request.UpdateFundRequest;
import com.sigma.dao.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FundService {

    private final FundRepository fundRepository;
    private final NetworkConfigService networkConfigService;
    private final AssetRepository assetRepository;
    private final UUIDUtils uuidUtils;
    private final GovernanceActionRepository governanceActionRepository;

    public FundService(FundRepository fundRepository,
                       NetworkConfigService networkConfigService,
                       AssetRepository assetRepository,
                       UUIDUtils uuidUtils,
                       GovernanceActionRepository governanceActionRepository) {
        this.fundRepository = fundRepository;
        this.networkConfigService = networkConfigService;
        this.assetRepository = assetRepository;
        this.uuidUtils = uuidUtils;
        this.governanceActionRepository = governanceActionRepository;
    }

    /**
     * Returns all of the {@link Fund}s
     *
     * @return a {@link List} of {@link Fund}s
     */
    public List<Fund> get() {
        return this.fundRepository.findAll();
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
        if(request.getDisbursementFrequency() == null) {
            throw new ProtocolException(ErrorCode.E0001);
        }
        if(request.getManagementFee() == null) {
            throw new ProtocolException(ErrorCode.E0002);
        }
        if(request.getMinimumSubscription() == null) {
            throw new ProtocolException(ErrorCode.E0003);
        }
        if(request.getPerformanceFee() == null) {
            throw new ProtocolException(ErrorCode.E0004);
        }
        if(request.getRedemptionFrequency() == null) {
            throw new ProtocolException(ErrorCode.E0005);
        }
        if(request.getSubscriptionAssetId() == null) {
            throw new ProtocolException(ErrorCode.E0006);
        }
        if(request.getType() == null) {
            throw new ProtocolException(ErrorCode.E0007);
        }
        if(request.getEnactmentDate() == null) {
            throw new ProtocolException(ErrorCode.E0012);
        }
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
        // TODO - fund
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
        // TODO - need to add open and close date
        governanceActionRepository.save(new GovernanceAction()
                .setEntityId(fund.getId())
                .setVotesFor(0)
                .setVotesAgainst(0)
                .setEnactmentDate(request.getEnactmentDate())
                .setType(GovernanceActionType.CREATE_FUND));
        return fund;
    }

    /**
     * Updates an existing {@link Fund}
     *
     * @param request the {@link UpdateFundRequest} instance
     *
     * @return the updated {@link Fund}
     */
    public Fund update(final UpdateFundRequest request) {
//        if(fund.getId() == null) {
//            throw new ProtocolException(ErrorCode.E0008);
//        }
//        Fund currentFund = fundRepository.findById(fund.getId())
//                .orElseThrow(() -> new ProtocolException(ErrorCode.E0009));
//        if(!currentFund.getStatus().equals(FundStatus.PROPOSED)) {
//            throw new ProtocolException(ErrorCode.E0010);
//        }
//        if(!fund.getActivationDate().equals(currentFund.getActivationDate())) {
//            throw new ProtocolException(ErrorCode.E0011);
//        }
//        if(!fund.getStatus().equals(currentFund.getStatus())) {
//            throw new ProtocolException(ErrorCode.E0014);
//        }
//        // TODO - a governance action should be created (same as with asset)
//        fund.setSubscriptionAsset(currentFund.getSubscriptionAsset());
//        validate(fund);
//        return fundRepository.save(fund);
        return null;
    }
}