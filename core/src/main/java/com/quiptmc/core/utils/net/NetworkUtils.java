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
 * Networking helpers built on top of Java 11+ {@link java.net.http.HttpClient}.
 * <p>
 * This utility centralizes common HTTP operations (GET/POST/etc.), applies an
 * optional {@link HttpConfig} (headers, timeouts, etc.), and exposes generic
 * variants that let callers decide how the response body should be handled
 * via {@link java.net.http.HttpResponse.BodyHandler}. For example, callers can
 * retrieve a String, byte array, or stream depending on the handler passed in.
 * <p>
 * Notable behavior:
 * - POST/PUT calls are null-safe. If the provided {@code JSONObject body} is {@code null}, an empty request body is sent.
 * - All calls respect the per-request timeout configured in {@link HttpConfig#connectTimeout()}.
 */
public class NetworkUtils {

    /**
     * Shared HTTP client instance with a default connect timeout of 10 seconds.
     * The per-request timeout still comes from {@link HttpConfig#connectTimeout()}.
     */
    private static final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    /**
     * Default configuration used by convenience methods or as a baseline.
     * You can shadow/override its values by creating a new config from it
     * (e.g., {@code NetworkUtils.DEFAULT.config(...)}).
     */
    public static HttpConfig DEFAULT = HttpConfig.DEFAULTS;

    private NetworkUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Perform a GET request and decode the response body as a String using the
     * default {@link HttpResponse.BodyHandlers#ofString()} handler.
     *
     * @param config request configuration (headers, timeout, etc.)
     * @param url    absolute URL to fetch
     * @return the completed HTTP response with a String body
     * @throws RuntimeException if the request fails or is interrupted
     */
    public static HttpResponse<String> get(HttpConfig config, String url) {
        return get(config, url, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Perform a GET request and decode the response body using a custom
     * {@link HttpResponse.BodyHandler}. This is useful for binary data
     * (e.g., {@code BodyHandlers.ofByteArray()} or {@code ofInputStream()})
     * or for custom parsing strategies.
     *
     * @param config               request configuration (headers, timeout, etc.)
     * @param url                  absolute URL to fetch
     * @param responseBodyHandler  handler that controls how the response body is produced
     * @param <T>                  the type produced by the handler
     * @return the completed HTTP response with a body of type {@code T}
     * @throws RuntimeException if the request fails or is interrupted
     */
    public static <T> HttpResponse<T> get(HttpConfig config, String url, HttpResponse.BodyHandler<T> responseBodyHandler) {
        return request(config, url, HttpMethod.GET, null, responseBodyHandler);
    }

    /**
     * Perform a POST request with an optional JSON body, decoding the response
     * as a String using the default {@link HttpResponse.BodyHandlers#ofString()} handler.
     * <p>
     * The {@code body} parameter is null-safe. If it is {@code null}, an empty
     * request body is sent.
     *
     * @param config request configuration (headers, timeout, etc.)
     * @param url    absolute URL to post to
     * @param body   JSON payload or {@code null}
     * @return the completed HTTP response with a String body
     * @throws RuntimeException if the request fails or is interrupted
     */
    public static HttpResponse<String> post(HttpConfig config, String url, @Nullable JSONObject body) {
        return post(config, url, body, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Perform a POST request with an optional JSON body and a custom
     * {@link HttpResponse.BodyHandler} for the response.
     * <p>
     * The {@code body} parameter is null-safe. If it is {@code null}, an empty
     * request body is sent.
     *
     * @param config               request configuration (headers, timeout, etc.)
     * @param url                  absolute URL to post to
     * @param body                 JSON payload or {@code null}
     * @param responseBodyHandler  handler that controls how the response body is produced
     * @param <T>                  the type produced by the handler
     * @return the completed HTTP response with a body of type {@code T}
     * @throws RuntimeException if the request fails or is interrupted
     */
    public static <T> HttpResponse<T> post(HttpConfig config, String url, @Nullable JSONObject body, HttpResponse.BodyHandler<T> responseBodyHandler) {
        return request(config, url, HttpMethod.POST, body, responseBodyHandler);
    }

    /**
     * Perform an HTTP request using the given configuration and method.
     * <p>
     * Behavior and notes:
     * - Applies any headers provided by {@link HttpConfig#headers()}.
     * - Uses {@link HttpConfig#connectTimeout()} as the per-request timeout.
     * - For {@link HttpMethod#POST} and {@link HttpMethod#PUT}, the {@code body} is null-safe; if {@code null}, an empty
     *   request body is sent.
     * - Throws {@link IllegalArgumentException} for unsupported methods.
     *
     * @param config               request configuration (headers, timeout, etc.)
     * @param url                  absolute URL to call
     * @param method               HTTP method to use
     * @param body                 optional JSON payload for POST/PUT; ignored for GET/DELETE
     * @param responseBodyHandler  handler that controls how the response body is produced
     * @param <T>                  the type produced by the handler
     * @return the completed HTTP response with a body of type {@code T}
     * @throws RuntimeException if the request fails or the thread is interrupted
     */
    public static <T> HttpResponse<T> request(HttpConfig config, String url, HttpMethod method, @Nullable JSONObject body, HttpResponse.BodyHandler<T> responseBodyHandler) {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url)).timeout(config.connectTimeout());
        if (config.headers() != null) for (HttpHeader header : config.headers())
            builder.header(header.name, header.value);
        switch (method) {
            case GET -> builder.GET();
            case POST -> builder.POST(HttpRequest.BodyPublishers.ofString(body != null ? body.toString() : ""));
            case PUT -> builder.PUT(HttpRequest.BodyPublishers.ofString(body != null ? body.toString() : ""));
            case DELETE -> builder.DELETE();
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        try {
            return http.send(builder.build(), responseBodyHandler);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(HttpResponse<InputStream> response, File file) {
        try (InputStream in = response.body(); OutputStream out = new FileOutputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}