package com.quiptmc.core;

import com.quiptmc.core.utils.net.HttpConfig;
import com.quiptmc.core.utils.net.HttpHeaders;
import com.quiptmc.core.utils.net.NetworkUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.time.Duration;

public class NetworkTests {

    @Test
    public void testGet() throws InterruptedException {
        String etag = "null";
        for(int i = 0; i < 10; i++) {
        HttpConfig config = NetworkUtils.DEFAULT.config(
                Duration.ofSeconds(10),
                3000,
                false,
                false,
                "application/json",
                "UTF-8",
                HttpHeaders.ETAG(etag));
        HttpResponse<String> response = NetworkUtils.get(config, "https://www.extra-life.org/api/teams/69005");
            String newEtag = response.headers().firstValue("etag").orElse(null);
            if (newEtag != null && !newEtag.isBlank()) {
                etag = newEtag;     // keep the quotes if present
            }
        System.out.println(etag);
        Thread.sleep(1500);
        }


    }
}
