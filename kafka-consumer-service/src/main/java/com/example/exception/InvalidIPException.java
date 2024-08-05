package com.example.exception;

public class InvalidIPException extends RuntimeException{

    public InvalidIPException(String ip){
        super("Invalid IP Exception with with "+ip);
    }
}
