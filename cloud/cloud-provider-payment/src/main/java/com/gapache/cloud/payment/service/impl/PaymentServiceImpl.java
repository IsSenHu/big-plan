package com.gapache.cloud.payment.service.impl;

import com.gapache.cloud.payment.dao.po.PaymentPO;
import com.gapache.cloud.payment.dao.repository.PaymentRepository;
import com.gapache.cloud.payment.error.PaymentError;
import com.gapache.cloud.payment.service.PaymentService;
import com.gapache.cloud.sdk.PaymentVO;
import com.gapache.commons.model.ThrowUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author HuSen
 * create on 2020/6/3 00:01
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Long create(PaymentVO payment) {
        PaymentPO newPayment = new PaymentPO();
        newPayment.setSerial(payment.getSerial());
        paymentRepository.save(newPayment);
        return newPayment.getId();
    }

    @Override
    public PaymentVO findById(Long id) {
        Optional<PaymentPO> optional = paymentRepository.findById(id);
        ThrowUtils.throwIfTrue(!optional.isPresent(), PaymentError.NOT_FOUND);

        PaymentPO po = optional.get();
        PaymentVO vo = new PaymentVO();
        vo.setId(po.getId());
        vo.setSerial(po.getSerial());
        return vo;
    }
}
