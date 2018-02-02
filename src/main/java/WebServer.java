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

                    Request request = readRequest(newSocket);
                    Response response = this.requestRouter.respondTo(request);
                    writeResponse(newSocket, response);

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

    private Request readRequest(Socket newSocket) throws IOException {
        InputStreamReader isr = new InputStreamReader(newSocket.getInputStream());
        BufferedReader br = new BufferedReader(isr);

        return getRequestFromString(br.readLine());
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

