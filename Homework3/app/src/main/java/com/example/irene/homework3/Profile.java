package com.example.irene.homework3;

public class Profile {
    private String cardNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String login;
    private String region;

    public Profile(String cardNumber, String firstName, String lastName, String email, String login, String region) {
        this.cardNumber = cardNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.login = login;
        this.region = region;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getRegion() {
        return region;
    }
}
