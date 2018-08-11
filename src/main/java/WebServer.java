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
                    queueConnection(server.accept());
                } catch (SocketException e) {
                    // swallow exceptions
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void queueConnection(Socket newSocket) {
        Thread t = new Thread(() -> {
            try {
                Request request = readRequest(newSocket);
                Response response = this.requestRouter.respondTo(request);
                writeResponse(newSocket, response);

                newSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        t.start();
    }


    private Request readRequest(Socket newSocket) throws IOException {
        InputStreamReader isr = new InputStreamReader(newSocket.getInputStream());
        BufferedReader br = new BufferedReader(isr);

        String s = br.readLine();
        if (s == null) {
            throw new IOException("Server received a null request");
        } else {
            return getRequestFromString(s);
        }
    }

    private void writeResponse(Socket newSocket, Response response) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(newSocket.getOutputStream());
        String headers = String.join("\n", response.headers);
        osw.write("HTTP/1.1 " + response.responseCode + "\n" + headers + "\n\n");
        osw.flush();
        newSocket.getOutputStream().write(response.body);
        newSocket.getOutputStream().flush();
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

