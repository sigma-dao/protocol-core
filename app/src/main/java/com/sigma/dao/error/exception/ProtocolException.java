package com.sigma.dao.error.exception;

/**
 * Generic wrapper for {@link RuntimeException}
 */
public class ProtocolException extends RuntimeException {

    /**
     * Constructs new exception
     *
     * @param error the error message
     */
    public ProtocolException(String error) {
        super(error);
    }
}