package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.cache.UserCache;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.User;
import com.christopher.herron.tradingsimulator.view.event.UpdateUserViewEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserCache userCache;

    @Autowired
    public UserService(ApplicationEventPublisher applicationEventPublisher, UserCache userCache) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.userCache = userCache;
    }

    public void addUser(final User user) {
        userCache.addUser(user);
    }

    public void updateUserOrderTableView(final Order order) {
        applicationEventPublisher.publishEvent(new UpdateUserViewEvent(this, order));
    }

    public int generateUserId() {
        return userCache.generateUserId();
    }

}
