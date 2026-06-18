package com.tfp.example.domain;

public record PaymentCard(
        String cardholderName,
        String cardNumber,
        String expiry,
        String cvc
) {}

