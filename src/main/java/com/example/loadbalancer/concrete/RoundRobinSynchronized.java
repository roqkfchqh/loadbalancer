package com.example.loadbalancer.concrete;

import com.example.loadbalancer.common.LoadBalancerStrategy;
import java.util.List;

public class RoundRobinSynchronized implements LoadBalancerStrategy {

    private int currentIndex = 0;

    @Override
    public synchronized String getNextServer(List<String> healthyServers) {
        if (healthyServers.isEmpty()) {
            throw new IllegalStateException("No healthy servers available.");
        }
        String server = healthyServers.get(currentIndex);
        currentIndex = (currentIndex + 1) % healthyServers.size();
        return server;
    }
}
