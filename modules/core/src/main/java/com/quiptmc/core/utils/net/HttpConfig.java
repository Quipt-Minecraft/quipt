package com.quiptmc.core.utils.net;

import java.time.Duration;

/**
 * Immutable HTTP client configuration used by NetworkUtils and related helpers.
 * - connectTimeout: Max time allowed to establish a TCP connection.
 * - readTimeout: Max time in milliseconds to wait for data after connection.
 * - useCaches: Whether protocol caches may be used by underlying connection.
 * - allowUserInteraction: Whether the user may be interacted with for authentication.
 * - contentType: Default Content-Type header for requests (e.g., application/json).
 * - acceptCharset: Default Accept-Charset header (e.g., UTF-8).
 * - headers: Additional headers to send with each request.
 */
public record HttpConfig(Duration connectTimeout, int readTimeout, boolean useCaches, boolean allowUserInteraction,
                         String contentType, String acceptCharset, HttpHeader... headers) {

    public static final HttpConfig DEFAULTS = new HttpConfig(
            Duration.ofSeconds(10),
            3000,
            false,
            false,
            "application/json",
            "UTF-8");


    public static HttpConfig config(Duration connectTimeout, int readTimeout, boolean useCaches, boolean allowUserInteraction, String contentType, String acceptCharset, HttpHeader... headers) {
        return new HttpConfig(connectTimeout, readTimeout, useCaches, allowUserInteraction, contentType, acceptCharset, headers);
    }

    public static HttpConfig config(Duration connectTimeout, int readTimeout, boolean useCaches, boolean allowUserInteraction, String contentType, String acceptCharset) {
        return new HttpConfig(connectTimeout, readTimeout, useCaches, allowUserInteraction, contentType, acceptCharset);
    }

    public static HttpConfig defaults(){
        return new HttpConfig(
                DEFAULTS.connectTimeout,
                DEFAULTS.readTimeout,
                DEFAULTS.useCaches,
                DEFAULTS.allowUserInteraction,
                DEFAULTS.contentType,
                DEFAULTS.acceptCharset
        );
    }

    public static HttpConfig defaults(HttpHeader... headers) {
        return new HttpConfig(
                DEFAULTS.connectTimeout,
                DEFAULTS.readTimeout,
                DEFAULTS.useCaches,
                DEFAULTS.allowUserInteraction,
                DEFAULTS.contentType,
                DEFAULTS.acceptCharset,
                headers
        );
    }

}
