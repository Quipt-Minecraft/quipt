package com.quiptmc.core;

import com.quiptmc.core.utils.net.HttpHeaders;
import com.quiptmc.core.utils.net.NetworkUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.time.Duration;

public class NetworkTests {





    @Test
    public void testGet() throws InterruptedException {
        String etag = "";
        for(int i = 0; i < 10; i++) {
        NetworkUtils.Get get = NetworkUtils.GET.config(
                Duration.ofSeconds(10),
                3000,
                false,
                false,
                "application/json",
                "UTF-8",
                HttpHeaders.ETAG(etag));
        HttpResponse<String> response = NetworkUtils.get(get, "https://www.extra-life.org/api/teams/69005");
            String newEtag = response.headers().firstValue("etag").orElse(null);
            if (newEtag != null && !newEtag.isBlank()) {
                etag = newEtag;     // keep the quotes if present
            }
        System.out.println(etag);
        Thread.sleep(1500);
        }


    }
}
