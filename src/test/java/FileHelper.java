import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {
    static void writeStringToFile(String expected, String fileName) throws IOException {
        FileWriter out = new FileWriter(fileName);
        out.write(expected);
        out.close();
    }
}
