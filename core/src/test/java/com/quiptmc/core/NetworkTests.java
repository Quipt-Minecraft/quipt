package com.quiptmc.core;

import com.quiptmc.core.discord.WebhookManager;
import com.quiptmc.core.discord.embed.Embed;
import com.quiptmc.core.utils.NetworkUtils;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.net.http.HttpResponse;
import java.time.Duration;

public class NetworkTests {


    @Test
    public void testWebhook(){
        String title = "Test Embed";
        String description = "This is a test embed";
        Color color = new Color(0x44c444);
        String image = "https://i.imgur.com/3ZQ3ZQ3.png";
        String channelName = "test-webhook";
        Embed.Field field = new Embed.Field("Field 1", "This is field 1", false);
        Embed.Field field2 = new Embed.Field("Field 2", "This is field 2", true);
        Embed.Field field3 = new Embed.Field("Field 3", "This is field 3", true);
        Embed.Field[] fields = new Embed.Field[]{field, field2, field3};


        Embed.Builder builder = new Embed.Builder()
                .title(title)
                .description(description)
                .color(color).thumbnail(image);
        for (Embed.Field fieldItem : fields) {
            builder.field(fieldItem.name, fieldItem.value, fieldItem.inline);
        }
        WebhookManager.send(channelName, builder.build());


//        Embed embed = new Embed()
//                .title(title)
//                .description(description)
//                .color(color);
//        if(image != null) embed.thumbnail(image);
//        if(fields != null && fields.length > 0){
//            for(Embed.Field field : fields){
//                embed.field(field.name, field.value, field.inline);
//            }
//        }
//        WebhookManager.send(channelName, embed);
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
                etag);
        HttpResponse<String> response = NetworkUtils.get(get, "https://www.extra-life.org/api/teams/69005");
            String newEtag = response.headers().firstValue("etag").orElse(null);
            if (newEtag != null && !newEtag.isBlank()) {
                etag = newEtag;     // keep the quotes if present
            }
        System.out.println(etag);
        Thread.sleep(15000);
        }


    }
}
