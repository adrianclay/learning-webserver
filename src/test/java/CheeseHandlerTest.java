import com.sun.net.httpserver.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;

import static org.junit.Assert.*;

public class CheeseHandlerTest {
    CheeseHandler cheese;
    MockHttpExchange me;

    @Before
    public void TestSetup(){
        cheese = new CheeseHandler();
        me = new MockHttpExchange();
    }

    @Test
    public void TestResponseHeader() throws IOException{
        cheese.handle(me);
        assertEquals(200, me.getResponseCode());
    }

    @Test
    public void TestWriteResponse() throws IOException {
        String expected = "Delicious.\n";
        writeStringToCheeseFile(expected);

        cheese.handle(me);
        assertEquals(expected, me.byteArrayOutputStream.toString());
        assertEquals(expected.length(), me.responseLength);
    }

    private void writeStringToCheeseFile(String expected) throws IOException {
        FileWriter out = new FileWriter("cheese.txt");
        out.write(expected);
        out.close();
    }

    class MockHttpExchange extends HttpExchange{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int responseCode;
        long responseLength;

        @Override
        public Headers getRequestHeaders() {
            return null;
        }

        @Override
        public Headers getResponseHeaders() {
            return null;
        }

        @Override
        public URI getRequestURI() {
            return null;
        }

        @Override
        public String getRequestMethod() {
            return null;
        }

        @Override
        public HttpContext getHttpContext() {
            return null;
        }

        @Override
        public void close() {

        }

        @Override
        public InputStream getRequestBody() {
            return null;
        }

        @Override
        public OutputStream getResponseBody() {
            return byteArrayOutputStream;
        }

        @Override
        public void sendResponseHeaders(int i, long l) throws IOException {
            this.responseCode = i;
            this.responseLength = l;
        }

        @Override
        public InetSocketAddress getRemoteAddress() {
            return null;
        }

        @Override
        public int getResponseCode() {
            return this.responseCode;
        }

        @Override
        public InetSocketAddress getLocalAddress() {
            return null;
        }

        @Override
        public String getProtocol() {
            return null;
        }

        @Override
        public Object getAttribute(String s) {
            return null;
        }

        @Override
        public void setAttribute(String s, Object o) {

        }

        @Override
        public void setStreams(InputStream inputStream, OutputStream outputStream) {

        }

        @Override
        public HttpPrincipal getPrincipal() {
            return null;
        }
    }
}