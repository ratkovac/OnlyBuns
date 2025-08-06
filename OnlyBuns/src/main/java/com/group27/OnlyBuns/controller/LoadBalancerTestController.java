package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.loadbalancer.LoadBalancer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadBalancerTestController {

    private final LoadBalancer loadBalancer;

    public LoadBalancerTestController(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @GetMapping("/test-loadbalancer")
    public String testLoadBalancing() {
        return loadBalancer.getPostsFromBalancedInstance();
    }
}


