package com.quiptmc.core.utils.net;

import java.util.Base64;

public class HttpHeaders {

    public static HttpHeader EXPECT(String value) {
        return new HttpHeader("Expect", value);
    }

    public static HttpHeader FROM(String value) {
        return new HttpHeader("From", value);
    }

    public static HttpHeader FORWARDED(String value) {
        return new HttpHeader("Forwarded", value);
    }

    public static HttpHeader FOLLOW_ONLY_WHEN_PRERENDER_SHOWN(String value) {
        return new HttpHeader("Follow-Only-When-Prerender-Shown", value);
    }

    public static HttpHeader HOST(String value) {
        return new HttpHeader("Host", value);
    }

    public static HttpHeader HTTP2_SETTINGS(String value) {
        return new HttpHeader("HTTP2-Settings", value);
    }

    public static HttpHeader IF_MATCH(String value) {
        return new HttpHeader("If-Match", value);
    }

    public static HttpHeader IF_MODIFIED_SINCE(String value) {
        return new HttpHeader("If-Modified-Since", value);
    }

    public static HttpHeader IF_NONE_MATCH(String value) {
        return new HttpHeader("If-None-Match", value);
    }

    public static HttpHeader IF_RANGE(String value) {
        return new HttpHeader("If-Range", value);
    }

    public static HttpHeader IF_UNMODIFIED_SINCE(String value) {
        return new HttpHeader("If-Unmodified-Since", value);
    }

    public static HttpHeader LAST_EVENT_ID(String value) {
        return new HttpHeader("Last-Event-ID", value);
    }

    public static HttpHeader MAX_FORWARDS(String value) {
        return new HttpHeader("Max-Forwards", value);
    }

    public static HttpHeader ORIGIN(String value) {
        return new HttpHeader("Origin", value);
    }

    public static HttpHeader ORIGIN_ISOLATION(String value) {
        return new HttpHeader("Origin-Isolation", value);
    }

    public static HttpHeader PROXY_AUTHORIZATION(String value) {
        return new HttpHeader("Proxy-Authorization", value);
    }

    public static HttpHeader RANGE(String value) {
        return new HttpHeader("Range", value);
    }

    public static HttpHeader REFERER(String value) {
        return new HttpHeader("Referer", value);
    }

    public static HttpHeader REFERRER_POLICY(String value) {
        return new HttpHeader("Referrer-Policy", value);
    }

    public static HttpHeader SERVICE_WORKER(String value) {
        return new HttpHeader("Service-Worker", value);
    }

    public static HttpHeader TE(String value) {
        return new HttpHeader("TE", value);
    }

    public static HttpHeader UPGRADE(String value) {
        return new HttpHeader("Upgrade", value);
    }

    public static HttpHeader UPGRADE_INSECURE_REQUESTS(String value) {
        return new HttpHeader("Upgrade-Insecure-Requests", value);
    }

    public static HttpHeader USER_AGENT(String value) {
        return new HttpHeader("User-Agent", value);
    }

    public static HttpHeader ACCEPT_RANGES(String value) {
        return new HttpHeader("Accept-Ranges", value);
    }

    public static HttpHeader ACCESS_CONTROL_ALLOW_HEADERS(String value) {
        return new HttpHeader("Access-Control-Allow-Headers", value);
    }

    public static HttpHeader ACCESS_CONTROL_ALLOW_METHODS(String value) {
        return new HttpHeader("Access-Control-Allow-Methods", value);
    }

    public static HttpHeader ACCESS_CONTROL_ALLOW_ORIGIN(String value) {
        return new HttpHeader("Access-Control-Allow-Origin", value);
    }

    public static HttpHeader ACCESS_CONTROL_ALLOW_PRIVATE_NETWORK(String value) {
        return new HttpHeader("Access-Control-Allow-Private-Network", value);
    }

    public static HttpHeader ACCESS_CONTROL_ALLOW_CREDENTIALS(String value) {
        return new HttpHeader("Access-Control-Allow-Credentials", value);
    }

    public static HttpHeader ACCESS_CONTROL_EXPOSE_HEADERS(String value) {
        return new HttpHeader("Access-Control-Expose-Headers", value);
    }

    public static HttpHeader ACCESS_CONTROL_MAX_AGE(String value) {
        return new HttpHeader("Access-Control-Max-Age", value);
    }

    public static HttpHeader AGE(String value) {
        return new HttpHeader("Age", value);
    }

    public static HttpHeader ALLOW(String value) {
        return new HttpHeader("Allow", value);
    }

    public static HttpHeader CONTENT_DISPOSITION(String value) {
        return new HttpHeader("Content-Disposition", value);
    }

    public static HttpHeader CONTENT_ENCODING(String value) {
        return new HttpHeader("Content-Encoding", value);
    }

    public static HttpHeader CONTENT_LANGUAGE(String value) {
        return new HttpHeader("Content-Language", value);
    }

    public static HttpHeader CONTENT_LOCATION(String value) {
        return new HttpHeader("Content-Location", value);
    }

    public static HttpHeader CONTENT_MD5(String value) {
        return new HttpHeader("Content-MD5", value);
    }

    public static HttpHeader CONTENT_RANGE(String value) {
        return new HttpHeader("Content-Range", value);
    }

    public static HttpHeader CONTENT_SECURITY_POLICY(String value) {
        return new HttpHeader("Content-Security-Policy", value);
    }

    public static HttpHeader CONTENT_SECURITY_POLICY_REPORT_ONLY(String value) {
        return new HttpHeader("Content-Security-Policy-Report-Only", value);
    }

    public static HttpHeader X_CONTENT_SECURITY_POLICY(String value) {
        return new HttpHeader("X-Content-Security-Policy", value);
    }

    public static HttpHeader X_CONTENT_SECURITY_POLICY_REPORT_ONLY(String value) {
        return new HttpHeader("X-Content-Security-Policy-Report-Only", value);
    }

    public static HttpHeader X_WEBKIT_CSP(String value) {
        return new HttpHeader("X-WebKit-CSP", value);
    }

    public static HttpHeader X_WEBKIT_CSP_REPORT_ONLY(String value) {
        return new HttpHeader("X-WebKit-CSP-Report-Only", value);
    }

    public static HttpHeader CROSS_ORIGIN_EMBEDDER_POLICY(String value) {
        return new HttpHeader("Cross-Origin-Embedder-Policy", value);
    }

    public static HttpHeader CROSS_ORIGIN_EMBEDDER_POLICY_REPORT_ONLY(String value) {
        return new HttpHeader("Cross-Origin-Embedder-Policy-Report-Only", value);
    }

    public static HttpHeader CROSS_ORIGIN_OPENER_POLICY(String value) {
        return new HttpHeader("Cross-Origin-Opener-Policy", value);
    }

    public static HttpHeader ETAG(String value) {
        return new HttpHeader("ETag", value);
    }

    public static HttpHeader EXPIRES(String value) {
        return new HttpHeader("Expires", value);
    }

    public static HttpHeader LAST_MODIFIED(String value) {
        return new HttpHeader("Last-Modified", value);
    }

    public static HttpHeader LINK(String value) {
        return new HttpHeader("Link", value);
    }

    public static HttpHeader LOCATION(String value) {
        return new HttpHeader("Location", value);
    }

    public static HttpHeader KEEP_ALIVE(String value) {
        return new HttpHeader("Keep-Alive", value);
    }

    public static HttpHeader NO_VARY_SEARCH(String value) {
        return new HttpHeader("No-Vary-Search", value);
    }

    public static HttpHeader ORIGIN_TRIAL(String value) {
        return new HttpHeader("Origin-Trial", value);
    }

    public static HttpHeader P3P(String value) {
        return new HttpHeader("P3P", value);
    }

    public static HttpHeader PROXY_AUTHENTICATE(String value) {
        return new HttpHeader("Proxy-Authenticate", value);
    }

    public static HttpHeader REFRESH(String value) {
        return new HttpHeader("Refresh", value);
    }

    public static HttpHeader REPORT_TO(String value) {
        return new HttpHeader("Report-To", value);
    }

    public static HttpHeader RETRY_AFTER(String value) {
        return new HttpHeader("Retry-After", value);
    }

    public static HttpHeader SERVER(String value) {
        return new HttpHeader("Server", value);
    }

    public static HttpHeader SERVER_TIMING(String value) {
        return new HttpHeader("Server-Timing", value);
    }

    public static HttpHeader SERVICE_WORKER_ALLOWED(String value) {
        return new HttpHeader("Service-Worker-Allowed", value);
    }

    public static HttpHeader SET_COOKIE(String value) {
        return new HttpHeader("Set-Cookie", value);
    }

    public static HttpHeader SET_COOKIE2(String value) {
        return new HttpHeader("Set-Cookie2", value);
    }

    public static HttpHeader SOURCE_MAP(String value) {
        return new HttpHeader("SourceMap", value);
    }

    public static HttpHeader SUPPORTS_LOADING_MODE(String value) {
        return new HttpHeader("Supports-Loading-Mode", value);
    }

    public static HttpHeader STRICT_TRANSPORT_SECURITY(String value) {
        return new HttpHeader("Strict-Transport-Security", value);
    }

    public static HttpHeader TIMING_ALLOW_ORIGIN(String value) {
        return new HttpHeader("Timing-Allow-Origin", value);
    }

    public static HttpHeader TRAILER(String value) {
        return new HttpHeader("Trailer", value);
    }

    public static HttpHeader TRANSFER_ENCODING(String value) {
        return new HttpHeader("Transfer-Encoding", value);
    }

    public static HttpHeader VARY(String value) {
        return new HttpHeader("Vary", value);
    }

    public static HttpHeader WWW_AUTHENTICATE(String value) {
        return new HttpHeader("WWW-Authenticate", value);
    }

    public static HttpHeader DNT(String value) {
        return new HttpHeader("DNT", value);
    }

    public static HttpHeader X_CONTENT_TYPE_OPTIONS(String value) {
        return new HttpHeader("X-Content-Type-Options", value);
    }

    public static HttpHeader X_DEVICE_IP(String value) {
        return new HttpHeader("X-Device-IP", value);
    }

    public static HttpHeader X_DEVICE_REFERER(String value) {
        return new HttpHeader("X-Device-Referer", value);
    }

    public static HttpHeader X_DEVICE_ACCEPT_LANGUAGE(String value) {
        return new HttpHeader("X-Device-Accept-Language", value);
    }

    public static HttpHeader X_DEVICE_REQUESTED_WITH(String value) {
        return new HttpHeader("X-Device-Requested-With", value);
    }

    public static HttpHeader X_DO_NOT_TRACK(String value) {
        return new HttpHeader("X-Do-Not-Track", value);
    }

    public static HttpHeader X_FORWARDED_FOR(String value) {
        return new HttpHeader("X-Forwarded-For", value);
    }

    public static HttpHeader X_FORWARDED_PROTO(String value) {
        return new HttpHeader("X-Forwarded-Proto", value);
    }

    public static HttpHeader X_FORWARDED_HOST(String value) {
        return new HttpHeader("X-Forwarded-Host", value);
    }

    public static HttpHeader X_FORWARDED_PORT(String value) {
        return new HttpHeader("X-Forwarded-Port", value);
    }

    public static HttpHeader X_FRAME_OPTIONS(String value) {
        return new HttpHeader("X-Frame-Options", value);
    }

    public static HttpHeader X_POWERED_BY(String value) {
        return new HttpHeader("X-Powered-By", value);
    }

    public static HttpHeader PUBLIC_KEY_PINS(String value) {
        return new HttpHeader("Public-Key-Pins", value);
    }

    public static HttpHeader PUBLIC_KEY_PINS_REPORT_ONLY(String value) {
        return new HttpHeader("Public-Key-Pins-Report-Only", value);
    }

    public static HttpHeader X_REQUEST_ID(String value) {
        return new HttpHeader("X-Request-ID", value);
    }

    public static HttpHeader X_REQUESTED_WITH(String value) {
        return new HttpHeader("X-Requested-With", value);
    }

    public static HttpHeader X_USER_IP(String value) {
        return new HttpHeader("X-User-IP", value);
    }

    public static HttpHeader X_DOWNLOAD_OPTIONS(String value) {
        return new HttpHeader("X-Download-Options", value);
    }

    public static HttpHeader X_XSS_PROTECTION(String value) {
        return new HttpHeader("X-XSS-Protection", value);
    }

    public static HttpHeader X_DNS_PREFETCH_CONTROL(String value) {
        return new HttpHeader("X-DNS-Prefetch-Control", value);
    }

    public static HttpHeader PING_FROM(String value) {
        return new HttpHeader("Ping-From", value);
    }

    public static HttpHeader PING_TO(String value) {
        return new HttpHeader("Ping-To", value);
    }

    public static HttpHeader PURPOSE(String value) {
        return new HttpHeader("Purpose", value);
    }

    public static HttpHeader X_PURPOSE(String value) {
        return new HttpHeader("X-Purpose", value);
    }

    public static HttpHeader X_MOZ(String value) {
        return new HttpHeader("X-Moz", value);
    }

    public static HttpHeader DEVICE_MEMORY(String value) {
        return new HttpHeader("Device-Memory", value);
    }

    public static HttpHeader DOWNLINK(String value) {
        return new HttpHeader("Downlink", value);
    }

    public static HttpHeader ECT(String value) {
        return new HttpHeader("ECT", value);
    }

    public static HttpHeader RTT(String value) {
        return new HttpHeader("RTT", value);
    }

    public static HttpHeader SAVE_DATA(String value) {
        return new HttpHeader("Save-Data", value);
    }

    public static HttpHeader VIEWPORT_WIDTH(String value) {
        return new HttpHeader("Viewport-Width", value);
    }

    public static HttpHeader WIDTH(String value) {
        return new HttpHeader("Width", value);
    }

    public static HttpHeader PERMISSIONS_POLICY(String value) {
        return new HttpHeader("Permissions-Policy", value);
    }

    public static HttpHeader PERMISSIONS_POLICY_REPORT_ONLY(String value) {
        return new HttpHeader("Permissions-Policy-Report-Only", value);
    }

    public static HttpHeader SEC_CH_PREFERS_COLOR_SCHEME(String value) {
        return new HttpHeader("Sec-CH-Prefers-Color-Scheme", value);
    }

    public static HttpHeader ACCEPT_CH(String value) {
        return new HttpHeader("Accept-CH", value);
    }

    public static HttpHeader CRITICAL_CH(String value) {
        return new HttpHeader("Critical-CH", value);
    }

    public static HttpHeader SEC_CH_UA(String value) {
        return new HttpHeader("Sec-CH-UA", value);
    }

    public static HttpHeader SEC_CH_UA_ARCH(String value) {
        return new HttpHeader("Sec-CH-UA-Arch", value);
    }

    public static HttpHeader SEC_CH_UA_MODEL(String value) {
        return new HttpHeader("Sec-CH-UA-Model", value);
    }

    public static HttpHeader SEC_CH_UA_PLATFORM(String value) {
        return new HttpHeader("Sec-CH-UA-Platform", value);
    }

    public static HttpHeader SEC_CH_UA_PLATFORM_VERSION(String value) {
        return new HttpHeader("Sec-CH-UA-Platform-Version", value);
    }

    public static HttpHeader SEC_CH_UA_FULL_VERSION(String value) {
        return new HttpHeader("Sec-CH-UA-Full-Version", value);
    }

    public static HttpHeader SEC_CH_UA_FULL_VERSION_LIST(String value) {
        return new HttpHeader("Sec-CH-UA-Full-Version-List", value);
    }

    public static HttpHeader SEC_CH_UA_MOBILE(String value) {
        return new HttpHeader("Sec-CH-UA-Mobile", value);
    }

    public static HttpHeader SEC_CH_UA_WOW64(String value) {
        return new HttpHeader("Sec-CH-UA-WoW64", value);
    }

    public static HttpHeader SEC_CH_UA_BITNESS(String value) {
        return new HttpHeader("Sec-CH-UA-Bitness", value);
    }

    public static HttpHeader SEC_CH_UA_FORM_FACTOR(String value) {
        return new HttpHeader("Sec-CH-UA-Form-Factor", value);
    }

    public static HttpHeader SEC_CH_UA_FORM_FACTORS(String value) {
        return new HttpHeader("Sec-CH-UA-Form-Factors", value);
    }

    public static HttpHeader SEC_CH_VIEWPORT_WIDTH(String value) {
        return new HttpHeader("Sec-CH-Viewport-Width", value);
    }

    public static HttpHeader SEC_CH_VIEWPORT_HEIGHT(String value) {
        return new HttpHeader("Sec-CH-Viewport-Height", value);
    }

    public static HttpHeader SEC_CH_DPR(String value) {
        return new HttpHeader("Sec-CH-DPR", value);
    }

    public static HttpHeader SEC_FETCH_DEST(String value) {
        return new HttpHeader("Sec-Fetch-Dest", value);
    }

    public static HttpHeader SEC_FETCH_MODE(String value) {
        return new HttpHeader("Sec-Fetch-Mode", value);
    }

    public static HttpHeader SEC_FETCH_SITE(String value) {
        return new HttpHeader("Sec-Fetch-Site", value);
    }

    public static HttpHeader SEC_FETCH_USER(String value) {
        return new HttpHeader("Sec-Fetch-User", value);
    }

    public static HttpHeader SEC_METADATA(String value) {
        return new HttpHeader("Sec-Metadata", value);
    }

    public static HttpHeader SEC_TOKEN_BINDING(String value) {
        return new HttpHeader("Sec-Token-Binding", value);
    }

    public static HttpHeader SEC_PROVIDED_TOKEN_BINDING_ID(String value) {
        return new HttpHeader("Sec-Provided-Token-Binding-ID", value);
    }

    public static HttpHeader SEC_REFERRED_TOKEN_BINDING_ID(String value) {
        return new HttpHeader("Sec-Referred-Token-Binding-ID", value);
    }

    public static HttpHeader SEC_WEBSOCKET_ACCEPT(String value) {
        return new HttpHeader("Sec-WebSocket-Accept", value);
    }

    public static HttpHeader SEC_WEBSOCKET_EXTENSIONS(String value) {
        return new HttpHeader("Sec-WebSocket-Extensions", value);
    }

    public static HttpHeader SEC_WEBSOCKET_KEY(String value) {
        return new HttpHeader("Sec-WebSocket-Key", value);
    }

    public static HttpHeader SEC_WEBSOCKET_PROTOCOL(String value) {
        return new HttpHeader("Sec-WebSocket-Protocol", value);
    }

    public static HttpHeader SEC_WEBSOCKET_VERSION(String value) {
        return new HttpHeader("Sec-WebSocket-Version", value);
    }

    public static HttpHeader SEC_BROWSING_TOPICS(String value) {
        return new HttpHeader("Sec-Browsing-Topics", value);
    }

    public static HttpHeader OBSERVE_BROWSING_TOPICS(String value) {
        return new HttpHeader("Observe-Browsing-Topics", value);
    }

    public static HttpHeader SEC_AD_AUCTION_FETCH(String value) {
        return new HttpHeader("Sec-Ad-Auction-Fetch", value);
    }

    public static HttpHeader SEC_GPC(String value) {
        return new HttpHeader("Sec-GPC", value);
    }

    public static HttpHeader AD_AUCTION_SIGNALS(String value) {
        return new HttpHeader("Ad-Auction-Signals", value);
    }

    public static HttpHeader AD_AUCTION_ALLOWED(String value) {
        return new HttpHeader("Ad-Auction-Allowed", value);
    }

    public static HttpHeader CDN_LOOP(String value) {
        return new HttpHeader("CDN-Loop", value);
    }

    public static HttpHeader AUTHORIZATION(String value) {
        return new HttpHeader("Authorization", value);
    }

    public static HttpHeader AUTHORIZATION_BEARER(String token) {
        return new HttpHeader("Authorization", "Bearer " + token);
    }

    public static HttpHeader AUTHORIZATION_BASIC(String username, String password) {
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        return new HttpHeader("Authorization", "Basic " + encodedAuth);
    }


}
