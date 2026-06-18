package com.tfp.example.domain;

public enum PaymentCardTestData {
    ACCEPTED_TEST_CARD ( new PaymentCard(
            "Robert Smith",
                    "4242 4242 4242 4242",
                    "07/29",
                    "123")),
    DECLINED_TEST_CARD ( new PaymentCard(
            "Robert Smith",
                    "4000 0000 0000 0002",
                    "07/29",
                    "123"));

    private final PaymentCard card;

    PaymentCardTestData(PaymentCard card) {
        this.card = card;
    }

    public PaymentCard getInfo() {
        return card;
    }
}
