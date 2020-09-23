package com.github.notsetup.eventloop;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class DefaultEventloop implements Eventloop {
    private Selector selector;

    public DefaultEventloop() throws IOException {
        this.selector = Selector.open();
    }

    @Override
    public void add(HandlerProvider handlerProvider, SocketChannel socketChannel) throws IOException {
        SelectionKey key = socketChannel
                .configureBlocking(false)
                .register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        EventloopContext ctx = new EventloopContext(socketChannel, key, Thread.currentThread(), selector);
        Handler handler = handlerProvider.provide(ctx);
        handler.register();
        key.attach(handler);
        log.info("register successful!");
        selector.wakeup();
    }

    @Override
    public void run() {
        log.info("Eventloop start.");
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    Handler handler = (Handler) key.attachment();
                    if (key.isWritable()){
                        log.info("{}",handler);
                        handler.write();
                    }
                    if (key.isReadable()){
                        log.info("{}",handler);
                        handler.read();
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            log.error("Error:{}",e);
            throw new RuntimeException(e);
        }
    }
}
