package com.personalfinancemanager.exception;

public class FileSaveException extends RuntimeException {

    public FileSaveException() {
        super();
    }

    public FileSaveException(String message) {
        super(message);
    }
}
