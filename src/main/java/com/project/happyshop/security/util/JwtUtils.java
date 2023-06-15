package com.project.happyshop.security.util;


import com.project.happyshop.domain.entity.Member;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    @Autowired
    private static KeyPair keyPair;

    public static String setJwtToken(Member member) {

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "RS512");

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("email", member.getEmail());
        claims.put("provider", member.getProvider());
        claims.put("username", member.getUsername());

        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + 1000 * 60 * 60 * 1);

        return Jwts.builder()
                .setHeader(headerMap)
                .setClaims(claims)
                .signWith(keyPair.getPrivate())
                .compact();
    }
}
