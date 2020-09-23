package com.github.notsetup.eventloop;

public interface HandlerProvider {
        Handler provide(EventloopContext context);
}
