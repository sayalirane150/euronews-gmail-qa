package helpers;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class GmailApiHelper {
    private final String accessToken;

    public GmailApiHelper(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isEmailPresent(String subject) throws Exception {
        // Gmail search query
        String query = "subject:(" + subject + ") newer_than:1d";
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        String apiUrl = "https://gmail.googleapis.com/gmail/v1/users/me/messages?q=" + encodedQuery;

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Gmail API request failed: " + responseCode);
        }

        String response = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                .lines().reduce("", (acc, line) -> acc + line);

        JSONObject json = new JSONObject(response);

        // If "messages" array exists and has at least 1 message
        if (json.has("messages")) {
            JSONArray messages = json.getJSONArray("messages");
            return messages.length() > 0;
        }
        return false;
    }
}
