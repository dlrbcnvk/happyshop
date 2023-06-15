package com.project.happyshop.security.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryJwtTokenRepository {

    private Map<String, String> tokenInfos = new ConcurrentHashMap<>();

    public void setToken(String id, String token) {
        tokenInfos.put(id, token);
    }

    public String getToken(String id) {
        if (!findById(id)) {
            return null;
        }
        return tokenInfos.get(id);
    }

    public boolean findById(String id) {
        return tokenInfos.containsKey(id);
    }
}
