package test_client;


import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import entities.leaderboard.LeaderBatchHolder;
import entities.leaderboard.Leaderboard;
import entities.user.UserBatchHolder;
import entities.user.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class RestClientImpl implements RestClient {

    private static final Logger log = LogManager.getLogger(RestClient.class);
    private static final String PROTOCOL = "http";
    private static final String HOST = "localhost";
    private static final String PORT = "8080";
    private static final String SERVICE_URL = PROTOCOL + "://" + HOST + ":" + PORT;

    private static final OkHttpClient client = new OkHttpClient();

    @Override
    public boolean register(String user, String password) {

        MediaType mediaType = MediaType.parse("raw");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("user=%s&password=%s", user, password)
        );

        String requestUrl = SERVICE_URL + "/auth/register";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();
            boolean result = response.isSuccessful();
            if (result) {
                log.info("You have been registered.");
            } else {
                log.warn("You can't be registered with such values.");
            }
            return result;
        } catch (IOException e) {
            log.warn("Something went wrong in the request.", e);
            return false;
        }
    }

    @Override
    public Long authenticateUser(String user, String password) {

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("user=%s&password=%s", user, password)
        );
        String requestUrl = SERVICE_URL + "/auth/login";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();
            Long result = Long.parseLong(response.body().string());
            log.info("You have been logged in.");
            return result;
        } catch (NumberFormatException e) {
            log.warn("Login is not correct.");
            return null;
        } catch (IOException e) {
            log.warn("Something went wrong in login.", e);
            return null;
        }
    }

    @Override
    public boolean changePlayerPassword(Long token, String newPass) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("password=%s", newPass)
        );
        String requestUrl = SERVICE_URL + "/profile/password";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("authorization","Bearer " + token)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            boolean result = response.isSuccessful();
            if (result) {
                log.info("The password was changed successfully!");
            } else {
                log.warn("Your token is not valid.");
            }
            return result;
        } catch (IOException e) {
            log.warn("Something went wrong in the request.", e);
            return false;
        }
    }

    @Override
    public boolean changePlayerEmail(Long token, String NewEmail) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("email=%s", NewEmail)
        );
        String requestUrl = SERVICE_URL + "/profile/email";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("authorization","Bearer " + token)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            boolean result = response.isSuccessful();
            if (result) {
                log.info("The email was changed successfully!");
            } else {
                log.warn("Your token is not valid.");
            }
            return result;
        } catch (IOException e) {
            log.warn("Something went wrong in the request.", e);
            return false;
        }
    }

    @Override
    public List<UserEntity> getNLeaders(Integer input) {
        String requestUrl = SERVICE_URL + String.format("/data/leaderboard?amount=%d", input);
        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response);
            if (response.code() == 200) {
                String leadersJson = response.body().string();
                log.info("Json string - {}", leadersJson);
                return LeaderBatchHolder.readJson(leadersJson).getUsers();
            } else {
                log.warn("Something is here.");
                return null;
            }
        } catch (IOException e) {
            log.warn("Something went wrong in the request.", e);
            return null;
        }
    }

    @Override
    public boolean addScore(Long token, Integer input) {
        String requestUrl = SERVICE_URL + "/actions/addscore";
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("score=%d", input)
        );
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("authorization","Bearer " + token)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            boolean result = response.isSuccessful();
            if (result) {
                log.info("The score was added successfully!");
            } else {
                log.warn("Your token is not valid.");
            }
            return result;
        } catch (IOException e) {
            log.warn("Something went wrong in the request.", e);
            return false;
        }
    }

    @Override
    public String[] getNLeaderNames(Integer input) {
        String requestUrl = SERVICE_URL + String.format("/data/leadernames?amount=%d", input);
        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response);
            if (response.code() == 200) {
                String leadersJson = response.body().string();
                log.info("Json string - {}", leadersJson);
                return LeaderBatchHolder.readJsonNames(leadersJson);
            } else {
                log.warn("Something is here.");
                return null;
            }
        } catch (IOException e) {
            log.warn("Something went wrong in the request.", e);
            return null;
        }
    }

    @Override
    public boolean logoutPlayer(Long token) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                ""
        );
        String requestUrl = SERVICE_URL + "/auth/logout";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("authorization","Bearer " + token)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            boolean result = response.isSuccessful();
            if (result) {
                log.info("You have been logged out.");
            } else {
                log.warn("Your token is not valid.");
            }
            return result;
        } catch (IOException e) {
            log.warn("Something went wrong in the request.", e);
            return false;
        }
    }

    @Override
    public boolean changePlayerName(Long token, String userName) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(
                mediaType,
                String.format("name=%s", userName)
        );
        String requestUrl = SERVICE_URL + "/profile/name";
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .addHeader("authorization","Bearer " + token)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            boolean result = response.isSuccessful();
            if (result) {
                log.info("The name was changed successfully!");
            } else {
                log.warn("Your token is not valid.");
            }
            return result;
        } catch (IOException e) {
            log.warn("Something went wrong in the request.", e);
            return false;
        }
    }

    public List<UserEntity> getUsersBatch() {
        String requestUrl = SERVICE_URL + "/data/users";
        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response);
            if (response.code() == 200) {
                String usersJson = response.body().string();
                log.info("Json string - {}", usersJson);
                return UserBatchHolder.readJson(usersJson).getUsers();
            } else {
                log.warn("Your token is not valid.");
                return null;
            }
        } catch (IOException e) {
            log.warn("Something went wrong in the request.", e);
            return null;
        }

    }

}
