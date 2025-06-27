package handler;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginHandlerTest {

    @Test
    public void testLoginSuccess() throws Exception {
        URL url = new URL("http://localhost:62550/login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        String body = "{\"login\":\"makaka\",\"password\":\"123123\"}";
        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.getBytes());
        }

        int code = connection.getResponseCode();
        assertEquals(200, code);

        String response = new String(connection.getInputStream().readAllBytes());
        assertTrue(response.contains("token"));
    }

    @Test
    public void testLoginUnauthorized() throws Exception {
        URL url = new URL("http://localhost:62550/login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        String body = "{\"login\":\"wrong\",\"password\":\"wrong\"}";
        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.getBytes());
        }

        int code = connection.getResponseCode();
        assertEquals(401, code);
    }
}
