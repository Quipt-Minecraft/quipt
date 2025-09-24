package com.quiptmc.core.utils.net;

import com.quiptmc.core.annotations.Nullable;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * A utility class for network operations
 */
public class NetworkUtils {

    private static final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    public static HttpConfig DEFAULT = HttpConfig.DEFAULTS;

    private NetworkUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static HttpResponse<String> get(HttpConfig config, String url) {
        return request(config, url, HttpMethod.GET, null);
    }

    public static HttpResponse<String> post(HttpConfig config, String url, @Nullable JSONObject body) {
        return request(config, url, HttpMethod.POST, body);
    }

    public static HttpResponse<String> request(HttpConfig config, String url, HttpMethod method, @Nullable JSONObject body) {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url)).timeout(config.connectTimeout());
        if (config.headers() != null)
            for (HttpHeader header : config.headers())
                builder.header(header.name, header.value);
        switch (method) {
            case GET -> builder.GET();
            case POST -> builder.POST(HttpRequest.BodyPublishers.ofString(body.toString()));
            case PUT -> builder.PUT(HttpRequest.BodyPublishers.ofString(body.toString()));
            case DELETE -> builder.DELETE();
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        try {
            return http.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}