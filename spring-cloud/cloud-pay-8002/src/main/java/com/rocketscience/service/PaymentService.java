package com.rocketscience.service;

import com.rocketscience.entity.Payment;
import org.apache.ibatis.annotations.Param;

/**
 * 支付服务接口
 *
 * @author Eddie
 * @since
 */
public interface PaymentService {

    int create(Payment payment);

    Payment getPaymentById(@Param("id") Long id);

}
