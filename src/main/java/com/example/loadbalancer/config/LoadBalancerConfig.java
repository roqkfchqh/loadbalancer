package com.example.loadbalancer.config;

import com.example.loadbalancer.common.HealthCheckService;
import com.example.loadbalancer.common.LoadBalancerStrategy;
import com.example.loadbalancer.concrete.RoundRobinStrategy;
import com.example.loadbalancer.concrete.WeightedStrategy;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LoadBalancerConfig {

    @Value("${loadbalancer.strategy}")
    private String strategyName;

    @Bean
    @ConditionalOnProperty(name = "loadbalancer.strategy", havingValue = "roundrobin")
    public LoadBalancerStrategy roundRobinStrategy() {
        return new RoundRobinStrategy();
    }

    @Bean
    @ConditionalOnProperty(name = "loadbalancer.strategy", havingValue = "weighted")
    public LoadBalancerStrategy weightedStrategy(HealthCheckService healthCheckService) {
        return new WeightedStrategy(healthCheckService);
    }
}
