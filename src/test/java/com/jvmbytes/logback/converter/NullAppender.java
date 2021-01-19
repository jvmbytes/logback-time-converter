package com.jvmbytes.logback.converter;

import ch.qos.logback.core.OutputStreamAppender;

/**
 * @author wongoo
 */
public class NullAppender<E> extends OutputStreamAppender<E> {

    @Override
    public void start() {
        setOutputStream(new NullOutputStream());
        super.start();
    }

}
