package live.qsmc.core2;

import live.qsmc.core2.configs.TestConfig;
import live.qsmc.core2.utils.TestUtils;
import live.qsmc.core2.utils.net.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.http.HttpResponse;

public class NetworkTests {

    public static final String TOKEN = "3b18dad4a71b55e7bb7a8c2a1bc245a02305cfb0552b83ecf119b5da621f8e04850c6bf700a90fc530af15d3affeae2619cf2e51858bd46d444225232b97d592";

    public static QuiptIntegration testIntegration;

    @BeforeAll
    public static void setup() {
        testIntegration = new QuiptIntegration() {
            @Override
            public String name() {
                return "QuiptTest";
            }

            @Override
            public String version() {
                return "indev";
            }

            @Override
            public File folder() {
                return new File("quipt/test-data");
            }

            @Override
            public void enable() {

            }
        };
        Quipt.INSTANCE.enable(testIntegration);
    }


    @Test
    public void testUpload() throws IOException, InterruptedException {
//        TestConfig testConfig = testIntegration.configs().register(TestConfig.class);
//        testConfig.save();
////
//        File file = testConfig.file();
//
//        String secret = "abc123";
//
//        HttpResponse<String> response = NetworkUtils.upload(HttpConfig.defaults(HttpHeaders.AUTHORIZATION_BEARER(secret)), "https://api.qsmc.live/files/upload", file);
//        System.out.println(response.body());

        //        // and an Authorization header — anonymous writes are not permitted.
//        HttpConfig httpConfig = HttpConfig.defaults(
//                HttpHeaders.AUTHORIZATION_BASIC("admin", "")
//        );
//        String url = "https://repo.qsmc.live/repository/quipt-public/" + file.getName();
//        HttpResponse<String> responseRaw = NetworkUtils.request(httpConfig, url, HttpMethod.PUT, file, HttpResponse.BodyHandlers.ofString());
//        System.out.println("Status : " + responseRaw.statusCode());
//        System.out.println("Body   : " + responseRaw.body());
    }

    @Test
    public void testHastebin() throws FileNotFoundException {
//        JSONObject data = new JSONObject();
//        JSONObject core = new JSONObject();
//        for(String cid : Quipt.INSTANCE.configs().all()){
//            core.put(cid, Quipt.INSTANCE.configs().config(cid).json());
//        }
//        data.put("core", core);
//        for(QuiptIntegration integration : Quipt.INSTANCE.integrations()){
//            JSONObject integrationData = new JSONObject();
//            for(String cid : integration.configs().all()){
//                integrationData.put(cid, integration.configs().config(cid).json());
//            }
//            data.put(integration.name(), integrationData);
//        }
//
//
//
//        HttpConfig config = HttpConfig.defaults(HttpHeaders.X_CONTENT_TYPE_OPTIONS("json/application"), HttpHeaders.AUTHORIZATION_BEARER(TOKEN));
//        HttpResponse<String> responseRaw = NetworkUtils.post(config, "https://hastebin.com/documents", new JSONObject().put("data", data));
//        System.out.println(responseRaw.body());
//        JSONObject response = new JSONObject(responseRaw.body());
//        String shareUrl = "https://hastebin.com/share/" + response.getString("key") + ".json";

    }
}
