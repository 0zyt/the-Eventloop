package com.github.notsetup.eventloop;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class ServerBootstrap implements Bootstrap {
    private Selector selector;
    private ServerSocketChannel server;
    private HandlerProvider handlerProvider;
    private InetSocketAddress address;
    private EventLoopGroup eventLoopGroup;

    public ServerBootstrap provider(HandlerProvider handlerProvider) {
        this.handlerProvider = handlerProvider;
        return this;
    }

    public ServerBootstrap init(int port) throws IOException {
        return init(Runtime.getRuntime().availableProcessors(),new InetSocketAddress(port), Selector.open(), ServerSocketChannel.open());
    }

    public ServerBootstrap init(int threads,InetSocketAddress address, Selector selector, ServerSocketChannel serverSocketChannel) throws IOException {
        this.address = address;
        this.selector = selector;
        this.server = serverSocketChannel;
        this.eventLoopGroup=new DefaultEventloopGroup(threads);
        log.info("Initialization successful!");
        return this;
    }

    public void start() throws IOException {
        if (handlerProvider==null){
            throw new RuntimeException("Provider is null");
        }
        server.bind(address)
                .configureBlocking(false)
                .register(selector, SelectionKey.OP_ACCEPT);
        log.info("server started on {}", address);
        while (!Thread.interrupted()) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    SocketChannel channel = server.accept();
                    log.info("Connected!");
                    eventLoopGroup.dispatch(channel,handlerProvider);
                }
                iterator.remove();
            }
        }
    }
}
