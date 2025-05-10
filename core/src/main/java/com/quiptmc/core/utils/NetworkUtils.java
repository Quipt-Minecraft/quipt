package com.quiptmc.core.utils;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Base64;

/**
 * A utility class for network operations
 */
public class NetworkUtils {
    
    private static Logger logger = LoggerFactory.getLogger("NetworkUtils");

    private NetworkUtils() {
        throw new IllegalStateException("Utility class");
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
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
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
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }
            return conn.getInputStream();
        } catch (Exception ex) {
           logger.info("An error occurred while downloading file from url: " + url);
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
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
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
}