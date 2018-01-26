import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class WebServer {
    private ServerSocket server;
    private int port;
    private RequestRouter requestRouter;

    public WebServer(int port, RequestRouter requestRouter)
    {
        this.port = port;
        this.requestRouter = requestRouter;
    }

    public void Start() throws IOException {
        server = new ServerSocket(port);
        Thread t = new Thread(() -> {
            while(true) {
                try {
                    Socket newSocket = server.accept();
                    InputStreamReader isr = new InputStreamReader(newSocket.getInputStream());
                    BufferedReader br = new BufferedReader(isr);

                    String input = br.readLine();


                    Response r = this.requestRouter.respondTo(getRequestFromString(input));

                    OutputStreamWriter osw = new OutputStreamWriter(newSocket.getOutputStream());
                    String headers = String.join("\n", r.headers);
                    osw.write("HTTP/1.1 " + r.responseCode + "\n" + headers + "\n\n" + r.body + "\n");
                    osw.flush();
                    newSocket.close();

                }
                catch (SocketException e) {
                    // swallow exceptions
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
    }

    private Request getRequestFromString(String input) {
        Request result = new Request();

        String[] strings = input.split(" ");
        result.method = strings[0];
        result.route  = strings[1];

        return result;
    }


    public void Stop() throws IOException {
        server.close();
    }
}

