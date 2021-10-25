package com.sigma.dao.service;

import com.sigma.dao.ethereum.ERC20;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Optional;

@Slf4j
@Service
public class EthereumService {

    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(5_000_000);

    @Value("${eth.private.key}")
    private String privateKey;
    @Value("${eth.node}")
    private String ethNode;

    private Web3j web3j;
    private Credentials credentials;

    @PostConstruct
    private void init() {
        web3j = Web3j.build(new HttpService(ethNode));
        credentials = Credentials.create(privateKey);
    }

    private ERC20 getTokenContract(String tokenContractAddress) throws Exception {
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        return ERC20.load(tokenContractAddress, web3j, credentials,
                new StaticGasProvider(gasPrice, GAS_LIMIT));
    }

    public String getAddress() {
        return credentials.getAddress();
    }

    public BigInteger getBalanceOf(String tokenContractAddress, String address) {
        try {
            return getTokenContract(tokenContractAddress).balanceOf(address).send();
        } catch(Exception e) {
            return BigInteger.ZERO;
        }
    }

    public BigInteger getTotalSupply(String tokenContractAddress) {
        try {
            return getTokenContract(tokenContractAddress).totalSupply().send();
        } catch(Exception e) {
            return BigInteger.ZERO;
        }
    }

    public Optional<String> getSymbol(String tokenContractAddress) {
        try {
            return Optional.of(getTokenContract(tokenContractAddress).symbol().send());
        } catch(Exception e) {
            return Optional.empty();
        }
    }
}