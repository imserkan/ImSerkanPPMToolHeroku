package com.sislamoglu.ppmtool.exceptions;

public class UserProfileNotFoundExceptionResponse {

    private String userProfileNotFound;

    public UserProfileNotFoundExceptionResponse(String userProfileNotFound) {
        this.userProfileNotFound = userProfileNotFound;
    }

    public String getUserProfileNotFound() {
        return userProfileNotFound;
    }

    public void setUserProfileNotFound(String userProfileNotFound) {
        this.userProfileNotFound = userProfileNotFound;
    }
}
