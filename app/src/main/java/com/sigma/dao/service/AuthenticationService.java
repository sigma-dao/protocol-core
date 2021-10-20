package com.sigma.dao.service;

import com.sigma.dao.request.SignedRequest;
import com.sigma.dao.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PublicKey;

@Slf4j
@Service
public class AuthenticationService {

    private final JSONUtils jsonUtils;

    public AuthenticationService(JSONUtils jsonUtils) {
        this.jsonUtils = jsonUtils;
    }

    /**
     * Verify signed data
     *
     * @param request the {@link SignedRequest}
     *
     * @return true if valid
     */
    public boolean validSignature(
            final SignedRequest request
    ) {
        String signature = request.getSignature();
        String publicKey = request.getPublicKey();
        request.setSignature(null);
        String msg = jsonUtils.toJson(request);
        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
        try {
            EdDSAPublicKeySpec pubKey = new EdDSAPublicKeySpec(Hex.decodeHex(publicKey), spec);
            EdDSAEngine sgr = new EdDSAEngine(MessageDigest.getInstance(spec.getHashAlgorithm()));
            PublicKey vKey = new EdDSAPublicKey(pubKey);
            sgr.initVerify(vKey);
            return sgr.verifyOneShot(msg.getBytes(StandardCharsets.UTF_8), Hex.decodeHex(signature));
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}