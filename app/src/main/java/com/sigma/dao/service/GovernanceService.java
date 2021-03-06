package com.sigma.dao.service;

import com.sigma.dao.constant.GovernanceActionType;
import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.GovernanceAction;
import com.sigma.dao.model.GovernanceVote;
import com.sigma.dao.model.User;
import com.sigma.dao.repository.GovernanceActionRepository;
import com.sigma.dao.repository.GovernanceVoteRepository;
import com.sigma.dao.repository.UserRepository;
import com.sigma.dao.request.GovernanceVoteRequest;
import com.sigma.dao.request.PaginatedRequest;
import com.sigma.dao.response.GetGovernanceActionsResponse;
import com.sigma.dao.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class GovernanceService {

    private final GovernanceActionRepository governanceActionRepository;
    private final UserRepository userRepository;
    private final GovernanceVoteRepository governanceVoteRepository;
    private final NetworkConfigService networkConfigService;
    private final AuthenticationService authenticationService;
    private final UUIDUtils uuidUtils;

    public GovernanceService(GovernanceActionRepository governanceActionRepository,
                             UserRepository userRepository,
                             GovernanceVoteRepository governanceVoteRepository,
                             NetworkConfigService networkConfigService,
                             AuthenticationService authenticationService,
                             UUIDUtils uuidUtils) {
        this.governanceActionRepository = governanceActionRepository;
        this.userRepository = userRepository;
        this.governanceVoteRepository = governanceVoteRepository;
        this.networkConfigService = networkConfigService;
        this.authenticationService = authenticationService;
        this.uuidUtils = uuidUtils;
    }

    /**
     * Get all governance actions
     *
     * @param request {@link PaginatedRequest}
     *
     * @return {@link GetGovernanceActionsResponse}
     */
    public GetGovernanceActionsResponse getActions(
            final PaginatedRequest request
    ) {
        final PageRequest page = PageRequest.of(request.getPage(), request.getSize(),
                Sort.Direction.valueOf(request.getSort()), "entityId");
        return new GetGovernanceActionsResponse().setGovernanceActions(
                governanceActionRepository.findAll(page).toList());
    }

    /**
     * Vote on a {@link GovernanceAction}
     *
     * @param request {@link GovernanceVoteRequest}
     *
     * @return the updated {@link GovernanceAction}
     */
    public GovernanceAction vote(
            final GovernanceVoteRequest request
    ) {
        if(!authenticationService.validSignature(request)) {
            throw new ProtocolException(ErrorCode.E0027);
        }
        if(request.getGovernanceActionId() == null) {
            throw new ProtocolException(ErrorCode.E0008);
        }
        GovernanceAction governanceAction = governanceActionRepository.findById(request.getGovernanceActionId())
                .orElseThrow(() -> new ProtocolException(ErrorCode.E0025));
        User user = userRepository.findByPublicKey(request.getPublicKey())
                .orElseThrow(() -> new ProtocolException(ErrorCode.E0026));
        Optional<GovernanceVote> voteCheck = governanceVoteRepository
                .findByUserAndGovernanceAction(user, governanceAction);
        if(voteCheck.isPresent()) {
            throw new ProtocolException(ErrorCode.E0042);
        }
        if(request.getVoteFor()) {
            governanceAction.setVotesFor(governanceAction.getVotesFor() + user.getStake());
        } else {
            governanceAction.setVotesAgainst(governanceAction.getVotesAgainst() + user.getStake());
        }
        governanceVoteRepository.save(new GovernanceVote()
                .setGovernanceAction(governanceAction)
                .setUser(user)
                .setTimestamp(networkConfigService.getTimestamp())
                .setId(uuidUtils.next()));
        resolveGovernanceAction(governanceAction);
        return governanceActionRepository.save(governanceAction);
    }

    /**
     * Resolve a {@link GovernanceAction} when the voting threshold has passed
     *
     * @param governanceAction the {@link GovernanceAction} instance
     */
    private void resolveGovernanceAction(
            final GovernanceAction governanceAction
    ) {
        // TODO - resolve the governance action if it passes the required threshold
        if(governanceAction.getType().equals(GovernanceActionType.CREATE_FUND)) {

        } else if(governanceAction.getType().equals(GovernanceActionType.ADD_ASSET)) {

        } else if(governanceAction.getType().equals(GovernanceActionType.REMOVE_ASSET)) {

        } else if(governanceAction.getType().equals(GovernanceActionType.UPDATE_FUND)) {

        }
    }

    /**
     * Create a new {@link GovernanceAction}
     *
     * @param entityId the related entity's UUID
     * @param type the {@link GovernanceActionType}
     * @param enactmentDate the enactment timestamp
     * @param openingDate the opening timestamp
     * @param closingDate the closing timestamp
     */
    public void create(
            final UUID entityId,
            final GovernanceActionType type,
            final Long enactmentDate,
            final Long openingDate,
            final Long closingDate
    ) {
        long ts = networkConfigService.getTimestamp();
        if(enactmentDate < ts) {
            throw new ProtocolException(ErrorCode.E0028);
        }
        if(openingDate < ts) {
            throw new ProtocolException(ErrorCode.E0029);
        }
        if(closingDate < ts) {
            throw new ProtocolException(ErrorCode.E0030);
        }
        if(closingDate < openingDate) {
            throw new ProtocolException(ErrorCode.E0031);
        }
        if(enactmentDate < openingDate) {
            throw new ProtocolException(ErrorCode.E0032);
        }
        governanceActionRepository.save(new GovernanceAction()
                .setId(uuidUtils.next())
                .setEntityId(entityId)
                .setVotesFor(0)
                .setVotesAgainst(0)
                .setEnactmentDate(enactmentDate)
                .setOpeningDate(openingDate)
                .setClosingDate(closingDate)
                .setType(type));
    }
}