package ru.shop.exception;

public class BadOrderCountException extends RuntimeException {
    public BadOrderCountException(){
        super("BAD COUNT");
    }
}
