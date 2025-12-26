package com.exception;



public class InsufficientBalanceException extends Exception {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
//hata kendi hatamızı tanımlıyoruz 