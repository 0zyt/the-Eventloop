package com.github.notsetup.eventloop;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class DefaultEventloop implements Eventloop {
    private Selector selector;

    public DefaultEventloop() throws IOException {
        this.selector = Selector.open();
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

                    iterator.remove();
                }
            }
        } catch (IOException e) {
            log.error("Error:{}",e);
            throw new RuntimeException(e);
        }
    }
}
