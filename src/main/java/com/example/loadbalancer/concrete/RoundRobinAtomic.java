package com.example.loadbalancer.concrete;

import com.example.loadbalancer.common.LoadBalancerStrategy;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinAtomic implements LoadBalancerStrategy {

    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public String getNextServer(List<String> healthyServers) {

        if (healthyServers.isEmpty()) {
            throw new IllegalStateException("No healthy servers available.");
        }

        int index = currentIndex.getAndUpdate(i -> (i + 1) % healthyServers.size());
        return healthyServers.get(index);
    }
}
