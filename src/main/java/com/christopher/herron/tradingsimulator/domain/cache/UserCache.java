package com.christopher.herron.tradingsimulator.domain.cache;

import com.christopher.herron.tradingsimulator.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserCache {

    public Map<String, User> userIdToUser = new ConcurrentHashMap<>();

    public User getUser(final String userId) {
        return userIdToUser.get(userId);
    }

    public int generateUserId() {
        return userIdToUser.keySet().size() + 1;
    }
}
