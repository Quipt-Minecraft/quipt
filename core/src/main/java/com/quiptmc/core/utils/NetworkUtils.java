package com.quiptmc.core.utils;

import com.quiptmc.core.exceptions.QuiptNetworkException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;

/**
 * A utility class for network operations
 */
public class NetworkUtils {

    private static final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    public static Get GET = Get.DEFAULTS;
    private static Logger logger = LoggerFactory.getLogger("NetworkUtils");

    private NetworkUtils() {
        throw new IllegalStateException("Utility class");
    }


    public static HttpResponse<String> get(Get config, String url) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(config.connectTimeout)
                .GET();

        if (config.header != null && !config.header.isBlank()) {
            builder.header("If-None-Match", config.header);
        }

        try {
            return http.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Downloads a file from a URL
     *
     * @param url  The URL to download from
     * @param auth The authentication to use
     * @return The InputStream of the file
     */
    public static InputStream downloadFile(String url, String... auth) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URI(url).toURL().openConnection();
            conn.setDoOutput(true);
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestMethod("GET");

            if (auth != null && auth.length == 2) {
                String userCredentials = auth[0].trim() + ":" + auth[1].trim();
                String basicAuth = "Basic " + Base64.getEncoder().encodeToString(userCredentials.getBytes());
                conn.setRequestProperty("Authorization", basicAuth);
            }
            if (auth != null && auth.length == 1) {
                String token = auth[0].trim();
                System.out.println("Using access token.");
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }
            System.out.println("Response code: " + conn.getResponseCode());
            if (conn.getResponseCode() == 200)
                return conn.getInputStream();
            throw new QuiptNetworkException(conn.getResponseCode(), streamToString(conn.getErrorStream()));
        } catch (Exception ex) {
            logger.info("An error occurred while downloading file from url: " + url);
            ex.printStackTrace();
        }
        return InputStream.nullInputStream();
    }

    /**
     * Saves an InputStream to a FileOutputStream
     *
     * @param in  The InputStream to save
     * @param out The FileOutputStream to save to
     */
    public static void saveStream(InputStream in, FileOutputStream out) {
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException ex) {
            logger.info("An error occurred while saving file");
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                logger.info("An error occurred while closing streams");
            }
        }
    }

    public static String streamToString(InputStream inputStream) {
        try {

            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append(System.lineSeparator());
                }
            }
            return stringBuilder.toString();
        } catch (Exception ex) {
            logger.info("An error occurred while converting InputStream to String");
            ex.printStackTrace();
        }
        return null;
    }

    public static String request(String url, String... auth) {
        return streamToString(downloadFile(url, auth));
    }

    public static String post(String url, JSONObject data, String... auth) {
        try {
            URL myUrl = new URI(url).toURL();
            HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
            conn.setDoOutput(true);
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(3000);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestMethod("POST");

            if (auth != null && auth.length >= 2) {
                String userCredentials = auth[0].trim() + ":" + auth[1].trim();
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
                conn.setRequestProperty("Authorization", basicAuth);
            }
            conn.getOutputStream().write(data.toString().getBytes());
            return streamToString(conn.getInputStream());
        } catch (Exception ex) {
            logger.info("An error occurred while downloading file from (post) url: {}", url);
            ex.printStackTrace();
        }
        return null;
    }

    public record Get(Duration connectTimeout, int readTimeout, boolean useCaches, boolean allowUserInteraction,
                      String contentType, String acceptCharset, String header) {

        public static final Get DEFAULTS = new Get(
                        Duration.ofSeconds(10),
                        3000,
                        false,
                        false,
                        "application/json",
                        "UTF-8",
                        null);


        public static Get config(Duration connectTimeout, int readTimeout, boolean useCaches, boolean allowUserInteraction, String contentType, String acceptCharset, String header) {
            return new Get(connectTimeout, readTimeout, useCaches, allowUserInteraction, contentType, acceptCharset, header);
        }

        public static Get defaults(String header) {
            return new Get(
                    DEFAULTS.connectTimeout,
                    DEFAULTS.readTimeout,
                    DEFAULTS.useCaches,
                    DEFAULTS.allowUserInteraction,
                    DEFAULTS.contentType,
                    DEFAULTS.acceptCharset,
                    header
            );
        }

    }
}