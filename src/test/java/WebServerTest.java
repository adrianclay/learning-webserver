import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.*;

public class WebServerTest {
    WebServer ws;

    @Before
    public void TestSetup() {
        this.ws = new WebServer(8081);
    }

    @After
    public void TestTeardown() {
        this.ws.server.stop(0);
        this.ws = null;
    }

    @Test
    public void TestServerCreation() {
        assert ws.server != null;
    }

    @Test
    public void ServerListensOnPort8081() throws Exception {
        new Socket("localhost", 8081);
    }

    @Test
    public void ServerHasContext() {
        ws.server.removeContext("/cheese");
    }
}