package com.quiptmc.core.utils.net;

import java.time.Duration;

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
