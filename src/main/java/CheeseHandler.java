import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class CheeseHandler implements HttpHandler{
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = getResponseFromFile();
        httpExchange.sendResponseHeaders(200, response.length());
        writeResponse(response, httpExchange.getResponseBody());
    }

    private String getResponseFromFile(){
        StringBuilder sb = new StringBuilder();

        try {
            InputStream is = new FileInputStream("cheese.txt");
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            sb.append(line).append("\n");
        }
        catch (IOException e)
        {
        }
        return sb.toString();

    }

    private void writeResponse(String response, OutputStream exchangeStream){
        try {
            exchangeStream.write(response.getBytes());
            exchangeStream.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
