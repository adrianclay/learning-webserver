import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler implements RequestRouter{
    private byte[] getResponseFromFile(String fileName) throws IOException {
        InputStream is = new FileInputStream(fileName);
        int sizeAroo = (int) Files.size(new File(fileName).toPath());
        byte[] buff = new byte[sizeAroo];
        is.read(buff);
        return buff;
    }

    private String getContentType(String fileName) throws IOException {
        Path path = FileSystems.getDefault().getPath(fileName);
        return Files.probeContentType(path);
    }

    @Override
    public Response respondTo(Request request) {
        Response response = new Response();

        try {
            String routeWithoutFirstSlash = request.route.substring(1);
            response.body = getResponseFromFile(routeWithoutFirstSlash);
            response.responseCode = "200 OK";
            response.headers = new String[]{
                    "Content-Length: " + response.body.length,
                    "Content-Type: " + getContentType(routeWithoutFirstSlash)
            };
        } catch( FileNotFoundException exception) {
            response.body = new byte[] {};
            response.responseCode = "404 NOT FOUND";
            response.headers = new String[]{
                    "Content-Length: 0",
                    "Content-Type: text/plain"
            };
        } catch (IOException exception) {
            // GOODNESS
        }
        return response;
    }
}
