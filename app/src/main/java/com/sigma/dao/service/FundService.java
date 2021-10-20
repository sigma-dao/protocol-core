package com.sigma.dao.service;

import com.sigma.dao.constant.FundStatus;
import com.sigma.dao.constant.GovernanceStatus;
import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.Asset;
import com.sigma.dao.model.Fund;
import com.sigma.dao.repository.AssetRepository;
import com.sigma.dao.repository.FundRepository;
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

    public FundService(FundRepository fundRepository,
                       NetworkConfigService networkConfigService,
                       AssetRepository assetRepository,
                       UUIDUtils uuidUtils) {
        this.fundRepository = fundRepository;
        this.networkConfigService = networkConfigService;
        this.assetRepository = assetRepository;
        this.uuidUtils = uuidUtils;
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
     * @param fund the {@link Fund} instance
     *
     * @return the new {@link Fund}
     */
    public Fund create(final Fund fund) {
        validate(fund);
        long timestamp = networkConfigService.getTimestamp();
        long diff = fund.getActivationDate() - timestamp;
        if(diff < networkConfigService.get().getMinFundActivationTime()) {
            throw new ProtocolException(ErrorCode.E0013);
        }
        Asset subscriptionAsset = assetRepository.findById(fund.getSubscriptionAsset().getId())
                .orElseThrow(() -> new ProtocolException(ErrorCode.E0021));
        if(!subscriptionAsset.getStatus().equals(GovernanceStatus.APPROVED)) {
            throw new ProtocolException(ErrorCode.E0024);
        }
        fund.setStatus(FundStatus.PROPOSED);
        fund.setSubscriptionAsset(subscriptionAsset);
        fund.setId(uuidUtils.next());
        return fundRepository.save(fund);
    }

    /**
     * Updates an existing {@link Fund}
     *
     * @param fund the {@link Fund} instance
     *
     * @return the updated {@link Fund}
     */
    public Fund update(final Fund fund) {
        if(fund.getId() == null) {
            throw new ProtocolException(ErrorCode.E0008);
        }
        Fund currentFund = fundRepository.findById(fund.getId())
                .orElseThrow(() -> new ProtocolException(ErrorCode.E0009));
        if(!currentFund.getStatus().equals(FundStatus.PROPOSED)) {
            throw new ProtocolException(ErrorCode.E0010);
        }
        if(!fund.getActivationDate().equals(currentFund.getActivationDate())) {
            throw new ProtocolException(ErrorCode.E0011);
        }
        if(!fund.getStatus().equals(currentFund.getStatus())) {
            throw new ProtocolException(ErrorCode.E0014);
        }
        fund.setSubscriptionAsset(currentFund.getSubscriptionAsset());
        validate(fund);
        return fundRepository.save(fund);
    }

    /**
     * Verify that the mandatory fields of a {@link Fund} have been populated
     *
     * @param fund the {@link Fund} instance
     */
    private void validate(final Fund fund) {
        if(fund.getDisbursementFrequency() == null) {
            throw new ProtocolException(ErrorCode.E0001);
        }
        if(fund.getManagementFee() == null) {
            throw new ProtocolException(ErrorCode.E0002);
        }
        if(fund.getMinimumSubscription() == null) {
            throw new ProtocolException(ErrorCode.E0003);
        }
        if(fund.getPerformanceFee() == null) {
            throw new ProtocolException(ErrorCode.E0004);
        }
        if(fund.getRedemptionFrequency() == null) {
            throw new ProtocolException(ErrorCode.E0005);
        }
        if(fund.getSubscriptionAsset() == null) {
            throw new ProtocolException(ErrorCode.E0006);
        }
        if(fund.getType() == null) {
            throw new ProtocolException(ErrorCode.E0007);
        }
        if(fund.getActivationDate() == null) {
            throw new ProtocolException(ErrorCode.E0012);
        }
    }
}