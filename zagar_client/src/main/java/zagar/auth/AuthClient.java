package zagar.auth;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static zagar.GameConstants.DEFAULT_ACCOUNT_SERVER_HOST;
import static zagar.GameConstants.DEFAULT_ACCOUNT_SERVER_PORT;

public class AuthClient {

    @NotNull
    private static final Logger log = LogManager.getLogger(AuthClient.class);
    @NotNull
    private static final String SERVICE_URL = "http://" +
            DEFAULT_ACCOUNT_SERVER_HOST + ":" + DEFAULT_ACCOUNT_SERVER_PORT;
    private static final String HEADER = "content-type";
    private static final String HEADER_VALUE = "application/x-www-form-urlencoded";

    @NotNull
    private final OkHttpClient client = new OkHttpClient();

    public boolean register(@NotNull String user, @NotNull String password) {
        log.info("Trying to register user=" + user);
        MediaType mediaType = MediaType.parse(HEADER_VALUE);
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("user=%s&password=%s", user, password)
        );

        String requestUrl = SERVICE_URL + "/auth/register";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader(HEADER, HEADER_VALUE)
                .build();

        try {
            Response response = client.newCall(request).execute();
            final boolean result = response.isSuccessful();
            if (result) {
                log.info("You have been registered.");
            } else {
                log.warn("You can't be registered with such values.");
            }
            return result;
        } catch (IOException e) {
            log.warn("Something went wrong in register.", e);
            return false;
        }
    }

    @Nullable
    public String login(@NotNull String user, @NotNull String password) {
        log.info("Trying to login user=" + user);
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("user=%s&password=%s", user, password)
        );
        String requestUrl = SERVICE_URL + "/auth/login";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader(HEADER, HEADER_VALUE)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                log.info("You have been logged in.");
                return response.body().string();
            } else return null;
        } catch (IOException e) {
            log.warn("Something went wrong in login.", e);
            return null;
        }
    }

    public boolean logout(@NotNull Long token) {
        log.info("Trying to logout user with token " + token);
        MediaType mediaType = MediaType.parse(HEADER_VALUE);
        RequestBody body = RequestBody.create(
                mediaType,
                ""
        );
        String requestUrl = SERVICE_URL + "/auth/logout";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader(HEADER, HEADER_VALUE)
                .addHeader("authorization", "Bearer " + token)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.isSuccessful();
        } catch (IOException e) {
            log.warn("Something went wrong in logout.", e);
            return false;
        }
    }
}

