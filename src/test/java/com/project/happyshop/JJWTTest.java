package com.project.happyshop;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class JJWTTest {

    private KeyPair keyPair;

    @Test
    void jwsTestSuccess() throws NoSuchAlgorithmException, JOSEException {

        keyPair = getRSAKeyPair();

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "RS512");

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", "id from request");
        claims.put("name", "name from request");

        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + 1000 * 60 * 60 * 1);

        String jwt = Jwts.builder()
                .setHeader(headerMap)
                .setClaims(claims)
                .signWith(keyPair.getPrivate())
                .compact();
        log.info("jwt={}", jwt);

        Boolean checkJwtResult = checkJws(jwt);
        log.info("checkJwtResult={}", checkJwtResult);
    }

    @Test
    void jwsTestFailure() throws NoSuchAlgorithmException, JOSEException {

        keyPair = getRSAKeyPair();

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JJWT");
        headerMap.put("alg", "RS512");

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", "id from request");
        claims.put("name", "name from request");

        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + 1000 * 60 * 60 * 1);

        String jwt = Jwts.builder()
                .setHeader(headerMap)
                .setClaims(claims)
                .signWith(keyPair.getPrivate())
                .compact();
        log.info("jwt={}", jwt);

        Boolean checkJwtResult = checkInvalidJws(jwt);
        log.info("checkJwtResult={}", checkJwtResult);
    }

    @Test
    void generateUUID() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append(UUID.randomUUID());
        }
        log.info(sb.toString());
    }

    private Boolean checkInvalidJws(String jwt) throws NoSuchAlgorithmException, JOSEException {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getRSAKeyPair().getPublic())
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return true;
    }

    private Boolean checkJws(String jwt) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        log.info("id={}", claims.get("id"));
        log.info("name={}", claims.get("name"));
        return true;
    }

    private KeyPair getRSAKeyPair() throws NoSuchAlgorithmException, JOSEException {
        KeyPairGenerator rsaKeyPairGenerator = KeyPairGenerator.getInstance("RSA");
        rsaKeyPairGenerator.initialize(2048);

        KeyPair keyPair = rsaKeyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        StringBuilder keyIdSb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            keyIdSb.append(UUID.randomUUID());
        }
        String keyId = keyIdSb.toString();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS512)
                .keyID(keyId)
                .build();

        return rsaKey.toKeyPair();
    }
}
