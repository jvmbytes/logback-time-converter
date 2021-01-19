package com.jvmbytes.logback.converter;

import java.io.OutputStream;

/**
 * @author wongoo
 */
public class NullOutputStream extends OutputStream {

    @Override
    public void write(byte[] b) {
        // drop
    }

    @Override
    public void write(byte[] b, int off, int len) {
        // drop
    }

    @Override
    public void write(int b) {
        // drop
    }
}
