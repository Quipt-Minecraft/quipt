package com.quiptmc.core.exceptions;

public class QuiptNetworkException extends RuntimeException {
    private final int code;
    public QuiptNetworkException(int code, String message) {
        super(message);
        this.code = code;
    }
    public int responseCode(){
        return code;
    }

    @Override
    public void printStackTrace() {
        System.err.println("QuiptNetworkException (" + code + "): " + getMessage());
        super.printStackTrace();
    }
}
