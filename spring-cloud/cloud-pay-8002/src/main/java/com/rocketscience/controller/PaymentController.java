package com.rocketscience.controller;

import com.rocketscience.entity.CommonResult;
import com.rocketscience.entity.Payment;
import com.rocketscience.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 支付控制器
 *
 * @author Eddie
 * @since
 */
@RestController
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;


    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment) {
        int res = paymentService.create(payment);
        if (res > 0) {
            return new CommonResult(200, "success;serverPort:" + serverPort);
        } else {
            return new CommonResult(500, "error");
        }
    }

    @GetMapping("/payment/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return new CommonResult<>(200, "success;serverPort:" + serverPort, payment);
    }

}
