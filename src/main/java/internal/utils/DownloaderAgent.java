package internal.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloaderAgent {
    private static final Logger LOGGER = Logger.getLogger(DownloaderAgent.class.getName());

    private DownloaderAgent() {
    }

    private static String getFromLocal(String filename) throws IOException {
        File f = new File(filename);
        if (f.exists()) { /* show alert */
            byte[] encoded = Files.readAllBytes(Paths.get(filename));
            return new String(encoded);
        }

        return null;
    }

    public static String readJsonFromGitHub(String page, int tokenIndex, String cachePath, String filename) throws IOException {
        if (! Files.exists(Paths.get(cachePath))) {
            //create new directory to save cached data
            Files.createDirectories(Path.of(cachePath));
        }
        // String building ..
        String res = getFromLocal(cachePath + URLEncoder.encode(filename, StandardCharsets.UTF_8));
        if (res != null) {
            return res;
        }

        int threshold = 0;
        int count = -1;
        // We need a connection to retrieve information from page
        URL url;
        boolean exitCondition = false;
        StringBuilder builder = new StringBuilder();
        do {
            try {
                url = new URL(page);
                URLConnection conn = url.openConnection();
                String token = new String(Base64.getDecoder().decode(JSONConfig.getEncodedTocken(tokenIndex)), StandardCharsets.UTF_8);
                token = token.replace("\n", "");
                conn.setRequestProperty("Authorization", "token " + token);
                // String building ..
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    builder.append(inputLine);
                    builder.append(System.lineSeparator());
                }
                bufferedReader.close();
                exitCondition = true;

            } catch (IOException | NullPointerException e) {
                if (threshold > 10) {
                    LOGGER.log(Level.SEVERE, e.getMessage());
                    System.exit(1);
                }
                tokenIndex = count++;
                if (count > 2) {
                    threshold++;
                    count = 0;
                }
            }
        } while (!exitCondition);

        if (!builder.toString().equals("[]\n")) {
            PrintWriter out = new PrintWriter(cachePath + URLEncoder.encode(filename, StandardCharsets.UTF_8));
            out.println(builder);
            out.close();
        }

        return builder.toString().trim();

    }


    public static String readJsonFromJira(String url, String cachePath, String filename) throws IOException {
        if (! Files.exists(Paths.get(cachePath))) {
            //create new directory to save cached data
            Files.createDirectories(Path.of(cachePath));
        }
        String res = getFromLocal(cachePath + URLEncoder.encode(filename, StandardCharsets.UTF_8));
        if (res != null) {
            return res;
        }
        // String building ..
        StringBuilder builder = new StringBuilder();
        InputStream is = new URL(url).openStream();
        InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                builder.append(inputLine);
                builder.append(System.lineSeparator());
            }
        }

        if (!builder.toString().equals("[]\n")) {
            PrintWriter out = new PrintWriter(cachePath + URLEncoder.encode(filename, StandardCharsets.UTF_8));
            out.println(builder);
            out.close();
        }
        // return a string containing the page content
        return builder.toString().trim();
    }

}