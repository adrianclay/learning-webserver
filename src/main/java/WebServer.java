import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {
    HttpServer server;

    public WebServer(int port)
    {
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress("localhost", port), 180);
            server.createContext("/cheese", new CheeseHandler());
            server.start();
        }
        catch (IOException e)
        {

        }
    }
}
