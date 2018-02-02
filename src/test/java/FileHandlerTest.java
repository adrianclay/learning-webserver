import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class FileHandlerTest {
    FileHandler fileHandler;

    @Before
    public void TestSetup(){
        fileHandler = new FileHandler();
    }

    @Test
    public void Returns404Response() throws IOException{
        Request request = new Request();
        request.method = "GET";
        request.route = "/ham";
        Response response = fileHandler.respondTo(request);
        assertEquals("404 NOT FOUND", response.responseCode);
        assertEquals("Content-Length: 0", response.headers[0]);
        assertEquals("Content-Type: text/plain", response.headers[1]);
        assertArrayEquals(new byte[0], response.body);

    }

    @Test
    public void ReturnsFileContents() throws IOException {
        String expected_content = "delicious tasty cheese";
        writeStringToCheeseFile(expected_content);

        Request request = new Request();
        request.method = "GET";
        request.route = "/brollies.txt";

        Response response = fileHandler.respondTo(request);

        assertEquals("200 OK", response.responseCode);
        assertEquals("Content-Length: " + expected_content.length(), response.headers[0]);
        assertEquals("Content-Type: text/plain", response.headers[1]);
        assertArrayEquals(expected_content.getBytes(), response.body);
    }

    @Test
    public void ReturnsMultiLineFileContents() throws IOException {
        String expected_content = "Delicious tasty cheese\nAnother great line of content";
        writeStringToCheeseFile(expected_content);

        Request request = new Request();
        request.method = "GET";
        request.route = "/brollies.txt";

        Response response = fileHandler.respondTo(request);

        assertEquals("200 OK", response.responseCode);
        assertEquals("Content-Length: " + expected_content.length(), response.headers[0]);
        assertEquals("Content-Type: text/plain", response.headers[1]);
        assertArrayEquals(expected_content.getBytes(), response.body);
    }

    @Test
    public void ReturnsMultiLineHTMLContents() throws IOException {
        String expected_content = "<html><head>Chuckles</head><body><h1>Delicious tasty cheese\nAnother great line of content</h1></body></html>";
        writeStringtoCheeseHTMLFile(expected_content);

        Request request = new Request();
        request.method = "GET";
        request.route = "/brollies.html";

        Response response = fileHandler.respondTo(request);

        assertEquals("200 OK", response.responseCode);
        assertEquals("Content-Length: " + expected_content.length(), response.headers[0]);
        assertEquals("Content-Type: text/html", response.headers[1]);
        assertArrayEquals(expected_content.getBytes(), response.body);
    }

    private void writeStringToCheeseFile(String expected) throws IOException {
        FileWriter out = new FileWriter("brollies.txt");
        out.write(expected);
        out.close();
    }

    private void writeStringtoCheeseHTMLFile(String content) throws IOException{
        FileWriter out = new FileWriter("brollies.html");
        out.write(content);
        out.close();
    }
}