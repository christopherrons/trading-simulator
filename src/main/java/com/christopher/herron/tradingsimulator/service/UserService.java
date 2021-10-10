package com.christopher.herron.tradingsimulator.service;

import com.christopher.herron.tradingsimulator.domain.cache.UserCache;
import com.christopher.herron.tradingsimulator.domain.model.Order;
import com.christopher.herron.tradingsimulator.domain.model.Trade;
import com.christopher.herron.tradingsimulator.domain.model.User;
import com.christopher.herron.tradingsimulator.view.event.UpdateUserViewEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserCache userCache;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    public UserService(ApplicationEventPublisher applicationEventPublisher, UserCache userCache) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.userCache = userCache;
    }

    public void addUser(final User user) {
        userCache.addUser(user);
    }

    public void updateUserOrderTableView(final Order order) {
        executorService.execute(new Runnable() {
            public void run() {
                applicationEventPublisher.publishEvent(new UpdateUserViewEvent(this, order));
            }
        });
    }

    public void updateUserOrderTableView(final List<Pair<Order, Trade>> userOrderTradePairs) {

        executorService.execute(new Runnable() {
            public void run() {
                for (Pair<Order, Trade> userOrderTradePair : userOrderTradePairs) {
                    applicationEventPublisher.publishEvent(new UpdateUserViewEvent(this, userOrderTradePair.getLeft(), userOrderTradePair.getRight()));
                }
            }
        });
    }

    public int generateUserId() {
        return userCache.generateUserId();
    }

}
