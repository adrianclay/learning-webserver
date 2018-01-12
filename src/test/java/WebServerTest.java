import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WebServerTest {
    private static final int PORT = 8090;
    private WebServer ws;

    @Before
    public void TestSetup() throws IOException {
        this.ws = new WebServer(PORT);
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
    public void ServerRespondsToConnection() throws Exception {
        Socket s = connectToServer();

        InputStreamReader isr = new InputStreamReader(s.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        assertEquals(br.readLine(), "test");
        s.close();
    }

    private Socket connectToServer() throws IOException {
        return new Socket("localhost", PORT);
    }
}