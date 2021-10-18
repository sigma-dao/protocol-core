package com.sigma.dao.service;

import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.Fund;
import com.sigma.dao.model.repository.FundRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FundService {

    private final FundRepository fundRepository;

    public FundService(FundRepository fundRepository) {
        this.fundRepository = fundRepository;
    }

    /**
     * Creates a new {@link Fund}
     *
     * @param fund the {@link Fund} instance
     *
     * @return the new {@link Fund}
     */
    public Fund create(final Fund fund) {
        return save(fund);
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
        return save(fund);
    }

    /**
     * Save a {@link Fund} to the database
     *
     * @param fund the {@link Fund} instance
     *
     * @return the updated {@link Fund}
     */
    private Fund save(final Fund fund) {
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
    }
}