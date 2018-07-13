package com.magicworldz.txt2mobi;

public class Txt2MobiRuntimeException extends RuntimeException {
    public Txt2MobiRuntimeException(Throwable e) {
        super(e);
    }

    public Txt2MobiRuntimeException(String message, Throwable e) {
        super(message, e);
    }
}
