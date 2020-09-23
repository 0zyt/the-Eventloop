package com.github.notsetup.eventloop;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface Eventloop extends Runnable{
    public void add(HandlerProvider handlerProvider, SocketChannel socketChannel) throws IOException;
}
