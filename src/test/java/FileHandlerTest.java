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
        Response response = fileHandler.respondTo(getRequest("/ham.html"));
        assertEquals("404 NOT FOUND", response.responseCode);
        assertEquals("Content-Length: 0", response.headers[0]);
        assertEquals("Content-Type: text/plain", response.headers[1]);
        assertArrayEquals(new byte[0], response.body);
    }

    @Test
    public void ReturnstsTxtContents() throws IOException {
        String expected_content = "delicious tasty cheese";
        writeStringToFile(expected_content, "brollies.txt");
        Response response = fileHandler.respondTo(getRequest("/brollies.txt"));

        assertEquals("200 OK", response.responseCode);
        assertEquals("Content-Length: " + expected_content.length(), response.headers[0]);
        assertEquals("Content-Type: text/plain", response.headers[1]);
        assertArrayEquals(expected_content.getBytes(), response.body);
    }

    @Test
    public void ReturnsHTMLContents() throws IOException {
        String expected_content = "<html><body><h1>Delicious tasty cheese\nAnother great line of content</h1></body></html>";
        writeStringToFile(expected_content, "brollies.html");

        Response response = fileHandler.respondTo(getRequest("/brollies.html"));

        assertEquals("200 OK", response.responseCode);
        assertEquals("Content-Length: " + expected_content.length(), response.headers[0]);
        assertEquals("Content-Type: text/html", response.headers[1]);
        assertArrayEquals(expected_content.getBytes(), response.body);
    }

    private Request getRequest(String route) {
        Request request = new Request();
        request.method = "GET";
        request.route = route;
        return request;
    }

    private void writeStringToFile(String expected, String fileName) throws IOException {
        FileWriter out = new FileWriter(fileName);
        out.write(expected);
        out.close();
    }
}