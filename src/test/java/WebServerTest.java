import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WebServerTest {
    private static final int PORT = 8090;
    private WebServer ws;
    private MockRequestRouter requestRouter;

    @Before
    public void TestSetup() throws IOException {
        requestRouter = new MockRequestRouter();
        this.ws = new WebServer(PORT, requestRouter);
        this.ws.Start();
    }

    @After
    public void TestTeardown() throws IOException {
        this.ws.Stop();
    }

    @Test
    public void ServerListensToConnections() throws Exception {
        assertTrue(connectToServer().isConnected());
    }

    @Test()
    public void ServerRespondsToConnection() throws IOException {
        Socket s = connectToServer();
        String input = "GET /hello/ HTTP/1.1\n";
        int input_length = "/hello/".length(); //will ignore newline

        OutputStreamWriter ows = new OutputStreamWriter(s.getOutputStream());
        ows.write(input);
        ows.flush();

        InputStreamReader isr = new InputStreamReader(s.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        assertEquals(br.readLine(), "HTTP/1.1 200 OK");
        assertEquals(br.readLine(), "Content-Length: " + input_length);
        assertEquals(br.readLine(), "Content-Type: text/plain");
        assertEquals(br.readLine(), "");
        assertEquals(br.readLine(), "/hello/");
        s.close();
    }

    @Test()
    public void ServerRespondsToTwoConnections() throws Exception {
        ServerRespondsToConnection();
        ServerRespondsToConnection();
    }

    @Test(expected = ConnectException.class)
    public void ServerStopsRespondingToConnectionsAfterStop() throws Exception {
        this.ws.Stop();
        connectToServer();
    }

    @Test()
    public void ServerRespondsToNotFoundRequest() throws Exception {
        requestRouter.returnsNotFound = true;

        Socket s = connectToServer();
        String input = "GET /bad/ HTTP/1.1\n";

        OutputStreamWriter ows = new OutputStreamWriter(s.getOutputStream());
        ows.write(input);
        ows.flush();

        InputStreamReader isr = new InputStreamReader(s.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        assertEquals(br.readLine(), "HTTP/1.1 404 NOT FOUND");
        assertEquals(br.readLine(), "Content-Length: 0");
        assertEquals(br.readLine(), "Content-Type: text/plain");
        assertEquals(br.readLine(), "");
        s.close();
    }

    @Test()
    public void ServerHandlesEmptyRequestGracefully() throws Exception {
        Socket s = connectToServer();
        s.close();

        ServerRespondsToConnection();
    }

    @Test()
    public void ServerHandlesConcurrentRequests() throws Exception {
        Socket a = connectToServer();
        ServerRespondsToConnection();
        a.close();
    }

    private Socket connectToServer() throws IOException {
        return new Socket("localhost", PORT);
    }
}