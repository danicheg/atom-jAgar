package accountserver.api;

import entities.user.UserEntity;
import errormessages.ApiErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import accountserver.auth.Authorized;
import entities.token.Token;
import dao.DatabaseAccessLayer;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthenticationProvider {

    private static final Logger LOG = LogManager.getLogger(AuthenticationProvider.class);

    /*curl -i \
          -X POST \
          -H "Content-Type: application/x-www-form-urlencoded" \
          -d "user={user}&password={password}" \
     "http://localhost:8080/auth/register"*/
    @POST
    @Path("register")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public Response register(@FormParam("user") String name,
                             @FormParam("password") String password) {

        if (name == null || password == null || name.isEmpty() || password.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiErrorMessages.WRONG_NAME_OR_PASSWORD)
                    .build();
        }

        final String findByNameCondition = "name=\'" + name + "\'";

        if (DatabaseAccessLayer.checkByCondition(findByNameCondition)) {
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(ApiErrorMessages.BUSY_NAME)
                    .build();
        }

        UserEntity user = new UserEntity(name, password);
        DatabaseAccessLayer.insertUser(user);

        LOG.info("New user registered with login {}", name);
        return Response.ok(user.getName() + " registered.").build();

    }

    /*curl -X POST \
          -H "Content-Type: application/x-www-form-urlencoded" \
          -d "user={user}&password={password}" \
     "http://localhost:8080/auth/login"*/
    @POST
    @Path("login")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public Response authenticateUser(@FormParam("user") String user,
                                     @FormParam("password") String password) {

        if (user == null || password == null || user.isEmpty() || password.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiErrorMessages.WRONG_NAME_OR_PASSWORD)
                    .build();
        }

        try {

            if (!authenticate(user, password)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(ApiErrorMessages.WRONG_CREDENTIALS)
                        .build();
            }

            Token token = DatabaseAccessLayer.issueToken(user);
            LOG.info("UserEntity '{}' successfully logged in", user);
            return Response.ok(Long.toString(token.getToken())).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiErrorMessages.WRONG_CREDENTIALS)
                    .build();
        }

    }

    /*curl -X POST \
            -H "Authorization: Bearer {token}" \
            "http://localhost:8080/auth/logout"*/
    @Authorized
    @POST
    @Path("logout")
    @Produces("text/plain")
    public Response logoutPlayer(@HeaderParam("Authorization") String rawToken) {

        try {
            Token token = DatabaseAccessLayer.parse(rawToken);
            LOG.info("Provided token is {}", token);
            if (!DatabaseAccessLayer.contains(token)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiErrorMessages.CAN_NOT_FIND_TOKEN)
                        .build();
            } else {
                UserEntity user = DatabaseAccessLayer.getUser(token);
                DatabaseAccessLayer.removeToken(token);
                LOG.info("UserEntity '{}' logout successfully", user.getName());
                return Response.ok(user.getName() + " successfully logout!").build();
            }

        } catch (Exception e) {
            LOG.warn(e);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiErrorMessages.WRONG_CREDENTIALS)
                    .build();

        }

    }

    private boolean authenticate(String name, String password) {
        final String findByNameCondition = "name=\'" + name + "\'";
        final String findByPassCondition = "password=\'" + password + "\'";
        return DatabaseAccessLayer.checkByCondition(findByNameCondition, findByPassCondition);
    }

}
