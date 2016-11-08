package zagar.api;

import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import zagar.auth.AuthClient;

import java.io.IOException;

import static zagar.GameConstants.DEFAULT_ACCOUNT_SERVER_HOST;
import static zagar.GameConstants.DEFAULT_ACCOUNT_SERVER_PORT;

public class DataProvider {
    @NotNull
    private static final Logger log = LogManager.getLogger(AuthClient.class);
    @NotNull
    private static final String SERVICE_URL = "http://" + DEFAULT_ACCOUNT_SERVER_HOST + ":" + DEFAULT_ACCOUNT_SERVER_PORT;
    @NotNull
    private final OkHttpClient client = new OkHttpClient();


    public String getNLeaders(@NotNull Integer amount) {
        log.info("Trying to get first " + amount + "leaders");
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("amount=%s", amount)
        );

        String requestUrl = SERVICE_URL + "/api/leaders";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().toString();
        } catch (IOException e) {
            log.warn("Something went wrong in register.", e);
            return "";
        }
    }
}
