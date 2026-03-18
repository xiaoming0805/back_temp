package com.cennavi.user.controller;

import com.cennavi.user.feign.OrderFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign")
public class FeignTestController {

    @Autowired
    private OrderFeignClient orderFeignClient;

    @GetMapping("/test")
    public String testFeign() {
        int statusCode = 200;
        String result = switch (statusCode) {
            case 200 -> "OK";
            case 404 -> "Not Found";
            case 500 -> {
                yield "Server Error";
            }
            default -> "Unknown";
        };
        String html = """
             <html>
                 <body>
                 <h1>Welcome, %s!</h1>
              </body>
              </html>
             """.formatted(result);
        String orderHello = orderFeignClient.getOrderHello();
        String orderInfo = orderFeignClient.getOrderInfo();

        return String.format(
                "user-service 成功调用 order-service！\n%s\n%s",
                orderHello, orderInfo
        );
    }
}