package com.example.loadbalancer.config;

import com.example.loadbalancer.common.HealthCheckService;
import com.example.loadbalancer.common.LoadBalancerStrategy;
import com.example.loadbalancer.concrete.RoundRobinAtomic;
import com.example.loadbalancer.concrete.RoundRobinConcurrentMap;
import com.example.loadbalancer.concrete.RoundRobinSynchronized;
import com.example.loadbalancer.concrete.RoundRobinThreadLocal;
import com.example.loadbalancer.concrete.WeightedStrategy;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LoadBalancerConfig {

    @Bean
    @ConditionalOnProperty(name = "loadbalancer.strategy", havingValue = "atomic_round")
    public LoadBalancerStrategy roundRobinAtomicStrategy() {
        return new RoundRobinAtomic();
    }

    @Bean
    @ConditionalOnProperty(name = "loadbalancer.strategy", havingValue = "synchro_round")
    public LoadBalancerStrategy roundRobinSynchronizedStrategy() {
        return new RoundRobinSynchronized();
    }

    @Bean
    @ConditionalOnProperty(name = "loadbalancer.strategy", havingValue = "thread_round")
    public LoadBalancerStrategy roundRobinThreadLocalStrategy() {
        return new RoundRobinThreadLocal();
    }

    @Bean
    @ConditionalOnProperty(name = "loadbalancer.strategy", havingValue = "concurrent_round")
    public LoadBalancerStrategy roundRobinConcurrentMapStrategy() {
        return new RoundRobinConcurrentMap();
    }

    @Bean
    @ConditionalOnProperty(name = "loadbalancer.strategy", havingValue = "weighted")
    public LoadBalancerStrategy weightedStrategy(HealthCheckService healthCheckService) {
        return new WeightedStrategy(healthCheckService);
    }
}
