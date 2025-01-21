package com.example.loadbalancer.core;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoadBalancerController {
    private final LoadBalancer loadBalancer;

    public LoadBalancerController(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @GetMapping("/forward")
    public String forward(@RequestParam String request){
        try {
            return loadBalancer.forwardRequest(request);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

