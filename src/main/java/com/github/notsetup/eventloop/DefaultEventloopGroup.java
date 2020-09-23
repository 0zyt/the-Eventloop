package com.github.notsetup.eventloop;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DefaultEventloopGroup implements EventLoopGroup{
        private final List<Eventloop> loops;
        private int position=0;

    public DefaultEventloopGroup(int threadCount) throws IOException {
        this.loops = new LinkedList<>();
        for (int i = 0; i < threadCount; i++) {
            Eventloop loop = new DefaultEventloop();
            new Thread(loop,"Eventloop-"+i).start();
            loops.add(loop);
        }
    }

    @Override
    public void dispatch(SocketChannel socketChannel, HandlerProvider provider) throws IOException {
        position= position % loops.size();
        Eventloop eventloop = loops.get(position);
        eventloop.add(provider,socketChannel);
        ++position;
    }
}
