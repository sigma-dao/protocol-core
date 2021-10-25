package com.sigma.dao.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigma.dao.config.AppConfig;
import com.sigma.dao.request.GovernanceVoteRequest;
import com.sigma.dao.request.SignedRequest;
import com.sigma.dao.utils.JSONUtils;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.util.UUID;

@Ignore
public class AuthenticationServiceTest {

    private static final String PRIVATE_KEY = "1498b5467a63dffa2dc9d9e069caf075d16fc33fdd4c3b01bfadae6433767d93";
    private static final String PUBLIC_KEY = "b7a3c12dc0c8c748ab07525b701122b88bd78f600c76342d27f25e5f92444cde";

    private static final ObjectMapper objectMapper = new AppConfig().objectMapper();

    private AuthenticationService authenticationService;

    @Before
    public void setup() {
        authenticationService = new AuthenticationService(new JSONUtils(objectMapper));
    }

    @Test
    public void testVerifySignature() throws Exception {
        UUID id = UUID.randomUUID();
        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
        EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(Hex.decodeHex(PRIVATE_KEY), spec);
        EdDSAEngine sgr = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
        PrivateKey sKey = new EdDSAPrivateKey(privKey);
        sgr.initSign(sKey);
        SignedRequest vote = new GovernanceVoteRequest()
                .setGovernanceActionId(id)
                .setVoteFor(true)
                .setPublicKey(PUBLIC_KEY);
        byte[] sig = sgr.signOneShot(objectMapper.writeValueAsString(vote).getBytes(StandardCharsets.UTF_8));
        vote.setSignature(Hex.encodeHexString(sig));
        boolean result = authenticationService.validSignature(vote);
        Assertions.assertTrue(result);
    }

    @Test
    public void testVerifySignatureWithError() throws Exception {
        UUID id = UUID.randomUUID();
        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
        EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(Hex.decodeHex(PRIVATE_KEY), spec);
        EdDSAEngine sgr = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
        PrivateKey sKey = new EdDSAPrivateKey(privKey);
        sgr.initSign(sKey);
        SignedRequest vote = new GovernanceVoteRequest()
                .setGovernanceActionId(id)
                .setVoteFor(true)
                .setPublicKey(PUBLIC_KEY.replace("444cde", ""));
        byte[] sig = sgr.signOneShot(objectMapper.writeValueAsString(vote).getBytes(StandardCharsets.UTF_8));
        vote.setSignature(Hex.encodeHexString(sig));
        boolean result = authenticationService.validSignature(vote);
        Assertions.assertFalse(result);
    }
}