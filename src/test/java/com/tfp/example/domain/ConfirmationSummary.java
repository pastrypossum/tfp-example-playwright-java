package com.tfp.example.domain;

public record ConfirmationSummary(
        String reference,
        String club,
        String name,
        String email,
        String phone,
        String membershipType,
        String fee,
        String card,
        String paymentStatus
) {}

