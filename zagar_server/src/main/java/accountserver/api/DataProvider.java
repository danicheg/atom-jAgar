package accountserver.api;


import dao.LeaderboardDao;
import entities.leaderboard.LeaderboardBatchHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import entities.token.TokensStorage;
import entities.user.UserBatchHolder;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.jetbrains.annotations.NotNull;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/data")
public class DataProvider {

    private static final Logger log = LogManager.getLogger(DataProvider.class);

    /*curl -X GET
     "http://localhost:8080/data/users"*/
    @GET
    @Path("/users")
    @Produces("application/json")
    public Response getUsersBatch() {
        log.info("Batch of users requested.");
        return Response.ok(new UserBatchHolder(TokensStorage.getUserList()).writeJson()).build();
    }

    /*curl -X GET
     "http://localhost:8080/data/leaderboard"*/
    @GET
    @Path("/leaderboard")
    @Produces("application/json")
    public Response getNLeaders(@QueryParam("amount") String n) {
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

}
