package com.cennavi.order.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class OrderTestController {

    @Value("${server.port:8082}")
    private String port;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from order-service, port: " + port;
    }

    @GetMapping("/info")
    public String info() {
        return "这是订单服务，提供订单相关API";
    }
}