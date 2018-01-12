import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private ServerSocket server;
    private int port;

    public WebServer(int port)
    {
        this.port = port;
    }

    public void Start() throws IOException {
        server = new ServerSocket(port);
        Thread t = new Thread(() -> {
            try {
                Socket newSocket = server.accept();

                OutputStreamWriter osw = new OutputStreamWriter(newSocket.getOutputStream());
                osw.write("test\n");
                osw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    public void Stop() throws IOException {
        server.close();
    }

}
