package com.example.loadbalancer.rest;

import com.example.loadbalancer.common.RequestProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class LoadBalancerController {

    private final RequestProcessor requestProcessor;

    public LoadBalancerController(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    @GetMapping("/forward")
    public Mono<String> forward(@RequestParam String request) {
        return requestProcessor.processRequest(request);
    }
}

