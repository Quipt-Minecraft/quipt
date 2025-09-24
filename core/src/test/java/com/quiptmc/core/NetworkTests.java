package com.quiptmc.core;

import com.quiptmc.core.utils.net.HttpHeaders;
import com.quiptmc.core.utils.net.NetworkUtils;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.time.Duration;

public class NetworkTests {



    @Test
    public void destroyWhenDone(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMzgwMjEzYTRlYjQ2NWRlODYwZjdlY2VkZTJkYTBiOSIsIm5iZiI6MTc1ODY3MzM5Ni4zMDksInN1YiI6IjY4ZDMzOWY0MjE4ZDgyN2UyMTJlYzIzMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.uojistGgBlTW_85XqMM_aHEHYeRY1WfHhHFTby5biJ0";
        String url = "https://api.themoviedb.org/3/person/25451?append_to_response=combined_credits";

        NetworkUtils.Get get = NetworkUtils.Get.config(
                Duration.ofSeconds(10),
                3000,
                true,
                false,
                "application/json",
                "UTF-8",
                HttpHeaders.AUTHORIZATION_BEARER(token));
        HttpResponse<String> response = NetworkUtils.get(get, url);
        System.out.println(response.body());
    }


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
