package com.atguigu.springcloud.service;

import org.springframework.stereotype.Component;

@Component
public class PaymentFallbackService implements PaymentHystrixService {
    @Override
    public String paymentInfo_ok(Integer id) {
        return "full back paymentInfo_ok,伤心";
    }

    @Override
    public String paymentInfo_timeout(Integer id) {
        return "full back paymentInfo_timeout,伤心";
    }
}
