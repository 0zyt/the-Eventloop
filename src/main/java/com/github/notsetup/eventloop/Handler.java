package com.github.notsetup.eventloop;

import java.io.IOException;

public interface Handler {

    default void register(){

    }

    default void read() throws IOException {

    }

    default void write() throws IOException {

    }

    default void close(){

    }
}
