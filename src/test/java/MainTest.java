import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class MainTest {

    public static final String PAGE_CONTENT = "Delicious.\n";

    @Test
    public void EndToEndTest() throws IOException {
        Main.main(new String[]{"8020"});
        FileHelper.writeStringToFile(PAGE_CONTENT, "cheese.txt");
        String actual = makeGetRequest(new URL("http://localhost:8020/cheese.txt"));
        assertEquals(PAGE_CONTENT, actual);
    }

    private String makeGetRequest(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setReadTimeout(5 * 1500);

        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }

        return stringBuilder.toString();
    }
}
