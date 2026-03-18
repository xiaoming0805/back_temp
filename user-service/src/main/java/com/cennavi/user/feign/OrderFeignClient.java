package com.cennavi.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "order-service")  // 调用的服务名
public interface OrderFeignClient {

    @GetMapping("/order/test/hello")  // 注意要加上context-path
    String getOrderHello();

    @GetMapping("/order/test/info")
    String getOrderInfo();
}
