package com.quiptmc.core;

import com.quiptmc.core.utils.net.HttpConfig;
import com.quiptmc.core.utils.net.NetworkUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;

public class NetworkTests {

    @Test
    public void testGet() throws InterruptedException {
        String etag = "null";
        for (int i = 0; i < 10; i++) {
            HttpConfig config = HttpConfig.config(
                    Duration.ofSeconds(10),
                    3000,
                    false,
                    false,
                    "image/jpg",
                    "UTF-8");
            HttpResponse<InputStream> response = NetworkUtils.get(config, "https://image.tmdb.org/t/p/original/nqXkCrSxGIwaiMnRYo9hvx0sc4G.jpg", HttpResponse.BodyHandlers.ofInputStream());
            NetworkUtils.save(response, new File("./test.jpg"));

//            String newEtag = response.headers().firstValue("etag").orElse(null);
//            if (newEtag != null && !newEtag.isBlank()) {
//                etag = newEtag;     // keep the quotes if present
//            }
//            System.out.println(etag);
//            Thread.sleep(1500);

        }


    }
}
