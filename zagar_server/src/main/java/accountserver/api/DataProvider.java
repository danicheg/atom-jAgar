package accountserver.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import dao.LeaderboardDao;
import entities.leaderboard.Leaderboard;
import entities.leaderboard.LeaderboardBatchHolder;
import entities.user.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dao.DatabaseAccessLayer;
import entities.user.UserBatchHolder;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    public Response getNLeaders(@QueryParam("amount") String n) throws JsonProcessingException {
        log.info("Batch of leaders requested.");
        if (n != null) {
            Integer param = Integer.parseInt(n);
            return Response.ok(new LeaderboardBatchHolder(new LeaderboardDao().getNLeaders(param))
                    .writeJson())
                    .build();
        } else {
            return Response.ok(new LeaderboardBatchHolder(new LeaderboardDao().getAll())
                    .writeJson())
                    .build();
        }
    }

    /*curl -X GET
     "http://localhost:8080/data/leaderboard"*/
    @GET
    @Path("/leadernames")
    @Produces("application/json")
    public Response getNLeaderNames(@QueryParam("amount") String n) throws JsonProcessingException {
        log.info("Batch of leaders requested.");
        if (n != null) {
            Integer param = Integer.parseInt(n);
            List<Long> list = new LeaderboardDao().getNLeaders(param)
                    .stream().map(Leaderboard::getUserId)
                    .collect(Collectors.toList());
            List<UserEntity> users = DatabaseAccessLayer.getLoginUserList();
            List<String> usersList = users.stream().filter(usr -> list.contains(usr.getUserID()))
                    .map(UserEntity::getName)
                    .collect(Collectors.toList());
            return Response.ok(LeaderboardBatchHolder
                    .writeJsonNames(usersList))
                    .build();
        } else {
            List<Long> list = new LeaderboardDao().getAll()
                    .stream().map(Leaderboard::getUserId)
                    .collect(Collectors.toList());
            List<UserEntity> users = DatabaseAccessLayer.getLoginUserList();
            List<String> usersList = users.stream().filter(usr -> list.contains(usr.getUserID()))
                    .map(UserEntity::getName)
                    .collect(Collectors.toList());
            return Response.ok(LeaderboardBatchHolder
                    .writeJsonNames(usersList))
                    .build();
        }
    }
}
