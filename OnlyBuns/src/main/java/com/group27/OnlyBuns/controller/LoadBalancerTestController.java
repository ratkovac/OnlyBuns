package com.group27.OnlyBuns.controller;

import com.group27.OnlyBuns.loadbalancer.LoadBalancer;
import com.group27.OnlyBuns.model.Post;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class LoadBalancerTestController {

    private final LoadBalancer loadBalancer;

    public LoadBalancerTestController(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @GetMapping("/test-loadbalancer/all-users")
    public String testGetAllUsers() {
        return "Odgovor sa instance: " + loadBalancer.forwardRequest("/users");
    }

    @GetMapping("/test-loadbalancer/all-posts")
    public String testGetAllPosts() {
        return "Odgovor sa instance: " + loadBalancer.forwardRequest("/posts");
    }

    @GetMapping("/test-loadbalancer/posts-count")
    public String testGetPostsCount() {
        return "Odgovor sa instance: " + loadBalancer.forwardRequest("/posts/count");
    }

    @PostMapping("/test-loadbalancer/create-post")
    public String testCreatePost(@RequestBody Post newPost) {
        return "Odgovor sa instance: " + loadBalancer.forwardPostRequest("/posts/createPost", newPost);
    }

}