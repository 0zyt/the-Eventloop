import com.github.notsetup.eventloop.ServerBootstrap;

import java.io.IOException;

public class EchoServer {
    public static void main(String[] args) {
        try {
            new ServerBootstrap()
                    .provider(EchoHandler::new)
                    .init(8080)
                    .start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
