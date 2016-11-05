package accountserver.api;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dao.DatabaseAccessLayer;
import entities.user.UserBatchHolder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/data")
public class UserProvider {

    private static final Logger log = LogManager.getLogger(UserProvider.class);

    /*curl -X GET
     "http://localhost:8080/data/users"*/
    @GET
    @Path("/users")
    @Produces("application/json")
    public Response getUsersBatch() {
        log.info("Batch of users requested.");
        return Response.ok(new UserBatchHolder(DatabaseAccessLayer.getUserList()).writeJson()).build();
    }

}
