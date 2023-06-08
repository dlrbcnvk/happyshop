package com.project.happyshop.security.repository;

import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.security.model.ProviderUser;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTemporarySocialRepository {

    private Map<String, SocialUser> users = new ConcurrentHashMap<>();

    public SocialUser findBySocialId(String socialId) {
        if (users.containsKey(socialId)) {
            return users.get(socialId);
        }
        return null;
    }

    public void register(ProviderUser providerUser) {
        if (users.containsKey(providerUser.getSocialId())) {
            return;
        }
        SocialUser socialUser = new SocialUser(
                providerUser.getSocialId(),
                providerUser.getSocialProvider(),
                providerUser.getEmail());
        users.put(providerUser.getSocialId(), socialUser);
    }

    public void delete(String socialId) {
        users.remove(socialId);
    }

    public class SocialUser {
        private String socialId;
        private SocialProvider socialProvider;
        private String email;

        public SocialUser(String socialId, SocialProvider socialProvider, String email) {
            this.socialId = socialId;
            this.socialProvider = socialProvider;
            this.email = email;
        }
    }
}
