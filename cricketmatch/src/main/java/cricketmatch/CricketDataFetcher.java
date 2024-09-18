package cricketmatch;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CricketDataFetcher {

    public static void main(String[] args) {
        try {
            String apiUrl = "https://api.cuvora.com/car/partner/cricket-data";
            String apiKey = "test-creds@2320";
            
            // to Call the API and get the JSON response
            String jsonResponse = getApiData(apiUrl, apiKey);
            
            // to Parse the JSON data and compute results
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray matches = jsonObject.getJSONArray("data"); 
            
            int highestScore = 0;
            String highestScoringTeam = "";
            int matchesWith300PlusScore = 0;

            for (int i = 0; i < matches.length(); i++) {
                JSONObject match = matches.getJSONObject(i);

                // Extract necessary fields
                String t1 = match.getString("t1");
                String t2 = match.getString("t2");
                int t1Score = parseScore(match.getString("t1s"));
                int t2Score = parseScore(match.getString("t2s"));

                // to Calculate highest score in one innings
                if (t1Score > highestScore) {
                    highestScore = t1Score;
                    highestScoringTeam = t1;
                }
                if (t2Score > highestScore) {
                    highestScore = t2Score;
                    highestScoringTeam = t2;
                }

                // to Calculate number of matches with total score above 300
                if ((t1Score + t2Score) > 300) {
                    matchesWith300PlusScore++;
                }
            }

            // to Print the computed results
            System.out.println("Highest Score: " + highestScore + " and Team Name is: " + highestScoringTeam);
            System.out.println("Number Of Matches with total 300 Plus Score: " + matchesWith300PlusScore);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper function to make API call and get data
    private static String getApiData(String apiUrl, String apiKey) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("apiKey", apiKey);

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) { // HTTP OK
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return content.toString();
        } else {
            throw new RuntimeException("Failed to fetch data from API: HTTP error code : " + responseCode);
        }
    }

    // Helper function to parse score strings
    private static int parseScore(String score) {
        if (score.contains("/")) {
            return Integer.parseInt(score.split("/")[0]);
        } else if (!score.isEmpty()) {
            return Integer.parseInt(score);
        }
        return 0; 
    }
}
