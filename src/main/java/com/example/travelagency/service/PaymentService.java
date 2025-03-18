package com.example.travelagency.service;

import com.example.travelagency.entity.Payment;
import com.example.travelagency.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Integer id) {
        return paymentRepository.findById(id);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Integer id, Payment paymentDetails) {
        paymentDetails.setId(id);
        return paymentRepository.save(paymentDetails);
    }

    public void deletePayment(Integer id) {
        paymentRepository.deleteById(id);
    }
}