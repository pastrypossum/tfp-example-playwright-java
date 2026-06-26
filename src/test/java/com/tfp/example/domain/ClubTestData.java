package com.tfp.example.domain;

public enum ClubTestData {
    RIVERSIDE (new Club(
            "Riverside Rovers FC", "London, United Kingdom", "£45.00", "£6.50")),
    PENNIE(new Club(
            "Pennine Harriers AC", "Manchester, United Kingdom", "£35.00", "£5.00")),
    POLISPORTIVA(new Club(
            "Polisportiva Lago di Como", "Como, Italy", "€50.00", "€7.00")),
    ATLETICA(new Club (
            "Atletica Verona","Verona, Italy","€40.00","€6.00"));

    private final Club club;

    ClubTestData(Club club) {
        this.club = club;
    }

    public Club getInfo() {
        return club;
    }

}