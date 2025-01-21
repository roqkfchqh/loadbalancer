package com.example.loadbalancer.rest;

import com.example.loadbalancer.common.LoadBalancer;
import com.example.loadbalancer.common.RequestProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoadBalancerController {

    private final RequestProcessor requestProcessor;

    public LoadBalancerController(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    @GetMapping("/forward")
    public String forward(@RequestParam String request) {
        return requestProcessor.processRequest(request);
    }
}

