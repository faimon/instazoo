package ru.faimon.instazoo.payload.response;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {
    private final String userName;
    private final String password;

    public InvalidLoginResponse() {
        this.userName = "Invalid Username";
        this.password = "Invalid Password";
    }
}
