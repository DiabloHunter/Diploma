package com.example.project.exceptions;

public class DishNotExistsException extends IllegalArgumentException {
    public DishNotExistsException(String msg) {
        super(msg);
    }
}