package com.cafeteria.pedidos.exception;

public class TurnoNoActivoException extends RuntimeException {
    public TurnoNoActivoException(String message) {
        super(message);
    }
}