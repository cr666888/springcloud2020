package com.atguigu.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    public String paymentInfo_ok(Integer id){
        return "线程池:"+Thread.currentThread().getName()+"paymentInfo_ok,id:"+id+"哈哈";
    }

    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandle",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
    })
    public String paymentInfo_TimeOut(Integer id){
        int time = 2;
        try {
            TimeUnit.SECONDS.sleep(time);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "线程池:"+Thread.currentThread().getName()+"paymentInfo_TimeOut,id:"+id+"哈哈";
    }

    public String paymentInfo_TimeOutHandle(Integer id){
        return "线程池:"+Thread.currentThread().getName()+"系统繁忙稍后再试,id:"+id+"哈哈,耗时";
    }

    //---------------------服务熔断------------------------
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),
    })
    public String paymentCircuitBreaker(@PathVariable("id")Integer id){
        if(id < 0){
            throw new RuntimeException("id不能为负数");
        }
        String number = IdUtil.simpleUUID();
        return Thread.currentThread().getName()+"调用成功,流水号"+number;
    }

    public String paymentCircuitBreaker_fallback(@PathVariable("id")Integer id){
        return "id不能为负数,请稍后再试,id:"+id;
    }

}
