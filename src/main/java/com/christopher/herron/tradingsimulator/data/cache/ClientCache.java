package com.christopher.herron.tradingsimulator.data.cache;

import com.christopher.herron.tradingsimulator.common.enumerators.OrderStatusEnum;
import com.christopher.herron.tradingsimulator.domain.Client;
import com.christopher.herron.tradingsimulator.domain.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ClientCache {

    public Map<String, Client> clientIdToClient = new LinkedHashMap<>();

    public Collection<Order> getClientOrders(String clientId, OrderStatusEnum orderStatus) {
        return clientIdToClient.get(clientId).getClientOrders(orderStatus);
    }

    public Client addClient(Client client) {
        return clientIdToClient.putIfAbsent(client.getClientId(), client);
    }

    public void addClientOrder(Order order) {
        clientIdToClient.computeIfAbsent(order.getClientId(), value -> new Client(order.getClientId())).addOrder(order);
    }

    public void updateClientOrderStatus(String clientId, long orderId, OrderStatusEnum currentOrderStatus, OrderStatusEnum newOrderStatus) {
        clientIdToClient.get(clientId).updateClientOrderStatus(orderId, currentOrderStatus, newOrderStatus);
    }
}
