package com.quiptmc.core.utils.net;

import java.util.Base64;

public class HttpHeaders {

    public static HttpHeader AUTHORIZATION(String value) {
        return new HttpHeader("Authorization", value);
    }

    public static HttpHeader CONTENT_TYPE(String value) {
        return new HttpHeader("Content-Type", value);
    }

    public static HttpHeader ACCEPT(String value) {
        return new HttpHeader("Accept", value);
    }

    public static HttpHeader USER_AGENT(String value) {
        return new HttpHeader("User-Agent", value);
    }

    public static HttpHeader CACHE_CONTROL(String value) {
        return new HttpHeader("Cache-Control", value);
    }

    public static HttpHeader CONTENT_LENGTH(String value) {
        return new HttpHeader("Content-Length", value);
    }

    public static HttpHeader HOST(String value) {
        return new HttpHeader("Host", value);
    }

    public static HttpHeader CONNECTION(String value) {
        return new HttpHeader("Connection", value);
    }

    public static HttpHeader REFERER(String value) {
        return new HttpHeader("Referer", value);
    }

    public static HttpHeader ORIGIN(String value) {
        return new HttpHeader("Origin", value);
    }

    public static HttpHeader COOKIE(String value) {
        return new HttpHeader("Cookie", value);
    }

    public static HttpHeader SET_COOKIE(String value) {
        return new HttpHeader("Set-Cookie", value);
    }

    public static HttpHeader IF_NONE_MATCH(String value) {
        return new HttpHeader("If-None-Match", value);
    }

    public static HttpHeader IF_MODIFIED_SINCE(String value) {
        return new HttpHeader("If-Modified-Since", value);
    }

    public static HttpHeader ACCEPT_ENCODING(String value) {
        return new HttpHeader("Accept-Encoding", value);
    }

    public static HttpHeader ACCEPT_LANGUAGE(String value) {
        return new HttpHeader("Accept-Language", value);
    }

    public static HttpHeader ACCEPT_RANGES(String value) {
        return new HttpHeader("Accept-Ranges", value);
    }

    public static HttpHeader ETAG(String value) {
        return new HttpHeader("ETag", value);
    }

    public static HttpHeader EXPECTATION_ID(String value) {
        return new HttpHeader("Expectation-Id", value);
    }

    public static HttpHeader LAST_MODIFIED(String value) {
        return new HttpHeader("Last-Modified", value);
    }

    public static HttpHeader LOCATION(String value) {
        return new HttpHeader("Location", value);
    }

    public static HttpHeader PROXY_AUTHENTICATE(String value) {
        return new HttpHeader("Proxy-Authenticate", value);
    }

    public static HttpHeader AUTHORIZATION_BASIC(String username, String password) {
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        return new HttpHeader("Authorization", "Basic " + encodedAuth);
    }

    public static HttpHeader AUTHORIZATION_BEARER(String token) {
        return new HttpHeader("Authorization", "Bearer " + token);
    }



}
