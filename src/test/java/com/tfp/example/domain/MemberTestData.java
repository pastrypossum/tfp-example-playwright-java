package com.tfp.example.domain;

public enum MemberTestData {
    ROBERT ( new Member(
            "Robert", "Smith",
            "robert.smith@example.com", "07700900000",
            "1990-01-15",
            "123 Main Street", "",
            "London", "SW1A 1AA", "United Kingdom")),
    RUTH ( new Member(
            "Ruth", "Smith",
            "ruth.smith@example.com", "07700900123",
            "1992-05-19",
            "345 Main Street", "",
            "London", "SW1A 1AA", "United Kingdom")),
    MINIMAL ( new Member(
            "Minimal", "Smith",
            "minimal.smith@example.com", "07700929823",
            "1991-01-01",
            "98 Main Street", "",
            "", "", "United Kingdom")),
    EMPTY ( new Member(
            "", "",
                     "", "",
                     "",
                     "", "",
                     "", "", ""));
    // May need to be select... for back test

    private final Member member;

    MemberTestData(Member member) {
        this.member = member;
    }

    public Member getInfo() {
        return member;
    }
}