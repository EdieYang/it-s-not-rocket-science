package com.rocketscience.service.impl;

import com.rocketscience.dao.PaymentDao;
import com.rocketscience.entity.Payment;
import com.rocketscience.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 支付实现类
 *
 * @author Eddie
 * @since
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    @Resource
    private PaymentDao paymentDao;

    @Override
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }
}
