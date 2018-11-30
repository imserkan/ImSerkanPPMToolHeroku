package com.sislamoglu.ppmtool.exceptions;

public class UsernameAlreadyExistsExceptionResponse {

    private String userAlreadyExists;

    public UsernameAlreadyExistsExceptionResponse(String userAlreadyExists) {
        this.userAlreadyExists = userAlreadyExists;
    }

    public String getUserAlreadyExists() {
        return userAlreadyExists;
    }

    public void setUserAlreadyExists(String userAlreadyExists) {
        this.userAlreadyExists = userAlreadyExists;
    }
}
