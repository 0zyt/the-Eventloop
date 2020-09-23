package com.github.notsetup.eventloop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

@Getter
@AllArgsConstructor
@ToString
public class EventloopContext {
    private final SocketChannel socketChannel;
    private final SelectionKey selectionKey;
    private final Thread thread;
    private final Selector selector;

    public void close() throws IOException {
        this.socketChannel.close();
        this.thread.interrupt();
    }
}
