package com.github.notsetup.eventloop;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface EventLoopGroup {
    public void dispatch(SocketChannel socketChannel, HandlerProvider provider) throws IOException;
}
