package com.sislamoglu.ppmtool.exceptions;

public class StorageExceptionResponse {

    private String storageNotFound;

    public StorageExceptionResponse(String storageNotFound) {
        this.storageNotFound = storageNotFound;
    }

    public String getStorageNotFound() {
        return storageNotFound;
    }

    public void setStorageNotFound(String storageNotFound) {
        this.storageNotFound = storageNotFound;
    }
}
