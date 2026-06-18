package com.tfp.example.domain;

public record Member(
        String firstName,
        String lastName,
        String email,
        String phone,
        String dateOfBirth,
        String addressLine1,
        String addressLine2,
        String city,
        String postcode,
        String country
) {
    public String fullName() {
        return firstName + " " + lastName;
    }
}

