package com.github.notsetup.eventloop;

public interface Handler {

    default void register(){

    }

    default void read(){

    }

    default void write(){

    }

    default void close(){

    }
}
