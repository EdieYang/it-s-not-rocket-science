package com.rockectcience.controller;

import com.rocketscience.entity.CommonResult;
import com.rocketscience.entity.Payment;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 消费端控制器
 *
 * @author Eddie
 * @since
 */
@RestController
public class ConsumerController {

    @Resource
    private RestTemplate restTemplate;

    public static final String url = "http://CLOUD-PAY-SERVICE";

    @GetMapping("/consumer/payment")
    public CommonResult<Payment> getPayment(@RequestParam("id") String id) {
        CommonResult<Payment> res = restTemplate.getForObject(url + "/payment/" + id, CommonResult.class);
        return res;
    }

    @PostMapping("/consumer/payment/create")
    public CommonResult createPayment(Payment payment) {
        CommonResult res = restTemplate.postForObject(url + "/payment/create", payment, CommonResult.class);
        if (res.getCode() == 200) {
            return new CommonResult(200, "success");
        } else {
            return new CommonResult(400, "error");
        }
    }

}
