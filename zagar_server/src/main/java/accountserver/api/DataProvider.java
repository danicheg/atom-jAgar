package accountserver.api;

import accountserver.auth.Authorized;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.LeaderboardDao;
import dao.UserDao;
import entities.leaderboard.LeaderBatchHolder;
import entities.leaderboard.Leaderboard;
import entities.token.Token;
import entities.user.UserEntity;
import model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dao.DatabaseAccessLayer;
import entities.user.UserBatchHolder;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/data")
public class DataProvider {

    private static final Logger log = LogManager.getLogger(DataProvider.class);

    /*curl -X GET
     "http://localhost:8080/data/users"*/
    @GET
    @Path("/users")
    @Produces("application/json")
    public Response getUsersBatch() throws JsonProcessingException {
        log.info("Batch of users requested.");
        @NotNull final List<UserEntity> loginUserList = DatabaseAccessLayer.getLoginUserList();
        return Response.ok(new UserBatchHolder(loginUserList).writeJson()).build();
    }

    /*curl -X GET
     "http://localhost:8080/data/leaderboard"*/
    @GET
    @Path("/leaderboard")
    @Produces("application/json")
    public Response getNLeaders(@QueryParam("amount") String n, @QueryParam("leaderboard") String leaderID) throws JsonProcessingException {
        log.info("Batch of leaders requested.");

        if (leaderID != null) {
            if (n != null) {
                Integer param = Integer.parseInt(n);
                Leaderboard leaderboard = new LeaderboardDao().getById(Long.parseLong(leaderID));
                return Response.ok(new LeaderBatchHolder(new LeaderboardDao().getNLeaders(leaderboard, param))
                        .writeJson())
                        .build();
            } else {
                Leaderboard leaderboard = new LeaderboardDao().getById(Long.parseLong(leaderID));
                if (leaderboard != null) {
                    return Response.ok(new LeaderBatchHolder(leaderboard.getUsers())
                            .writeJson())
                            .build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /*curl -X GET
     "http://localhost:8080/data/leadernames"*/
    @GET
    @Path("/leadernames")
    @Produces("application/json")
    public Response getNLeaderNames(@QueryParam("amount") String n,
                                    @QueryParam("leaderboard") String leaderboardID) throws JsonProcessingException {
        log.info("Batch of leaders requested.");
        if (leaderboardID != null) {
            if (n != null) {
                Leaderboard leaderboard = new LeaderboardDao().getById(Long.parseLong(leaderboardID));
                List<UserEntity> users = UserDao.getAllLoginUsers();
                List<UserEntity> userEntities = new LeaderboardDao().getNLeaders(leaderboard, 5);
                if ( userEntities != null) {
                    List<String> userNames = userEntities.stream()
                            .map(UserEntity::getName)
                            .collect(Collectors.toList());
                    List<String> usersList = users.stream().filter(usr -> userNames.contains(usr.getName()))
                            .map(UserEntity::getName)
                            .collect(Collectors.toList());
                    return Response.ok(LeaderBatchHolder
                            .writeJsonNames(usersList))
                            .build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            } else {
                List<UserEntity> users = DatabaseAccessLayer.getLoginUserList();
                Leaderboard leaderboard = new LeaderboardDao().getById(Long.parseLong(leaderboardID));
                if (leaderboard != null) {
                    List<String> userNames = leaderboard.getUsers().
                            stream()
                            .map(UserEntity::getName)
                            .collect(Collectors.toList());
                    List<String> usersList = users.stream().filter(usr -> userNames.contains(usr.getName()))
                            .map(UserEntity::getName)
                            .collect(Collectors.toList());
                    return Response.ok(LeaderBatchHolder
                            .writeJsonNames(usersList))
                            .build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    /* curl -X POST
         -H "Content-Type: application/x-www-form-urlencoded"
         -H "Authorization: Bearer {token}"
         -H "Host: localhost:8080
    "http://localhost:8080/data/score"*/
    @Authorized
    @POST
    @Path("/score")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public Response getUserScore(@HeaderParam("Authorization") String rawToken) {
        try {
            Token token = DatabaseAccessLayer.parse(rawToken);

            if (!DatabaseAccessLayer.contains(token)) {
                log.warn(token);
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            UserEntity user = DatabaseAccessLayer.getUser(token);
            ObjectMapper MAPPER = new ObjectMapper();
            int score = user.getScore();
            log.info("UserEntity got hist score {}", user.getScore());
            return Response.ok(MAPPER.writeValueAsString("{ user: " + score + "}")).build();
        } catch (Exception e) {
            return Response.ok(e.toString()).build();
        }
    }
}
