package helpers;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class GmailTokenHelper {

    private static final String CLIENT_ID = "YOUR_CLIENT_ID";
    private static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
    private static final String REFRESH_TOKEN = "YOUR_REFRESH_TOKEN";

    public static String getAccessToken() throws Exception {
        String urlParameters = "client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8") +
                "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8") +
                "&refresh_token=" + URLEncoder.encode(REFRESH_TOKEN, "UTF-8") +
                "&grant_type=refresh_token";

        URL url = new URL("https://oauth2.googleapis.com/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(urlParameters.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed to get access token: " + responseCode);
        }

        String response = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                .lines().reduce("", (acc, line) -> acc + line);

        JSONObject json = new JSONObject(response);
        return json.getString("access_token");
    }
}
