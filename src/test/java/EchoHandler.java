import com.github.notsetup.eventloop.Eventloop;
import com.github.notsetup.eventloop.EventloopContext;
import com.github.notsetup.eventloop.Handler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

@Slf4j
public class EchoHandler implements Handler {

    private ByteBuffer buf;
    private EventloopContext ctx;
    public EchoHandler(EventloopContext ctx) {
        this.ctx=ctx;
    }

    @Override
    public void register() {
        buf=ByteBuffer.allocate(1024);
    }

    @Override
    public void read() throws IOException {
        if (ctx.getSocketChannel().read(buf)!=-1){
            log.info("read:{}",ctx.getSocketChannel());
            ctx.getSelectionKey().interestOps(SelectionKey.OP_WRITE);
            return;
        }
            ctx.getSocketChannel().close();
            log.info("read finished close:{}",ctx.getSocketChannel());
    }

    @Override
    public void write() throws IOException {
        buf.flip();
        ctx.getSocketChannel().write(buf);
        if (buf.hasRemaining()){
            buf.compact();
        }else {
            buf.clear();
        }
        log.info("write:{}",ctx.getSocketChannel());
        ctx.getSelectionKey().interestOps(SelectionKey.OP_READ);
    }

}
