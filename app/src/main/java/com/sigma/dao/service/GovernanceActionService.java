package com.sigma.dao.service;

import com.sigma.dao.error.ErrorCode;
import com.sigma.dao.error.exception.ProtocolException;
import com.sigma.dao.model.GovernanceAction;
import com.sigma.dao.model.GovernanceVote;
import com.sigma.dao.model.User;
import com.sigma.dao.repository.GovernanceActionRepository;
import com.sigma.dao.repository.GovernanceVoteRepository;
import com.sigma.dao.repository.UserRepository;
import com.sigma.dao.request.GovernanceVoteRequest;
import com.sigma.dao.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GovernanceActionService {

    private final GovernanceActionRepository governanceActionRepository;
    private final UserRepository userRepository;
    private final GovernanceVoteRepository governanceVoteRepository;
    private final NetworkConfigService networkConfigService;
    private final UUIDUtils uuidUtils;

    public GovernanceActionService(GovernanceActionRepository governanceActionRepository,
                                   UserRepository userRepository,
                                   GovernanceVoteRepository governanceVoteRepository,
                                   NetworkConfigService networkConfigService,
                                   UUIDUtils uuidUtils) {
        this.governanceActionRepository = governanceActionRepository;
        this.userRepository = userRepository;
        this.governanceVoteRepository = governanceVoteRepository;
        this.networkConfigService = networkConfigService;
        this.uuidUtils = uuidUtils;
    }

    /**
     * Get all {@link GovernanceAction}s
     *
     * @return {@link List} of {@link GovernanceAction}s
     */
    public List<GovernanceAction> get() {
        return governanceActionRepository.findAll();
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
        if(request.getGovernanceActionId() == null) {
            throw new ProtocolException(ErrorCode.E0008);
        }
        GovernanceAction governanceAction = governanceActionRepository.findById(request.getGovernanceActionId())
                .orElseThrow(() -> new ProtocolException(ErrorCode.E0025));
        User user = userRepository.findByPublicKey(request.getPublicKey())
                .orElseThrow(() -> new ProtocolException(ErrorCode.E0026));
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
        return governanceActionRepository.save(governanceAction);
    }
}