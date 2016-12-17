package accountserver.profile;

import accountserver.auth.Authorized;
import dao.DatabaseAccessLayer;
import entities.token.Token;
import entities.user.UserEntity;
import errormessages.ApiErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("profile")
public class UserSettings {

    private static final Logger LOG = LogManager.getLogger(UserSettings.class);

    /*curl -X POST
         -H "Content-Type: application/x-www-form-urlencoded"
         -H "Authorization: Bearer {token}"
         -d "name={newName}"
    "http://localhost:8080/profile/name"*/
    @Authorized
    @POST
    @Path("/name")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public Response changePlayerName(@HeaderParam("Authorization") String rawToken,
                                     @FormParam("name") String name) {
        try {

            if (name == null || "".equals(name)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiErrorMessages.WRONG_NAME)
                        .build();
            }

            Token token = DatabaseAccessLayer.parse(rawToken);

            if (!DatabaseAccessLayer.contains(token)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiErrorMessages.CAN_NOT_FIND_TOKEN)
                        .build();
            }

            final String findByNameCondition = "name=\'" + name + "\'";
            Boolean checkName = DatabaseAccessLayer.checkByCondition(findByNameCondition);

            if (checkName) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiErrorMessages.BUSY_NAME)
                        .build();
            }

            UserEntity user = DatabaseAccessLayer.getUser(token);
            String oldName = user.getName();
            user.setName(name);
            DatabaseAccessLayer.updateUser(user);
            LOG.info("UserEntity with name {} set name to {}", oldName, name);
            return Response.ok("Your name successfully changed to " + name).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiErrorMessages.WRONG_CREDENTIALS)
                    .build();
        }
    }

    /* curl -X POST
          -H "Content-Type: application/x-www-form-urlencoded"
          -H "Authorization: Bearer {token}"
          -H "Host: localhost:8080
          -d "password={newPassword}"
     "http://localhost:8080/profile/password"*/
    @Authorized
    @POST
    @Path("/password")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public Response changePlayerPassword(@HeaderParam("Authorization") String rawToken,
                                         @FormParam("password") String password) {
        try {

            if (password == null || "".equals(password)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiErrorMessages.WRONG_PASSWORD)
                        .build();
            }

            Token token = DatabaseAccessLayer.parse(rawToken);

            if (!DatabaseAccessLayer.contains(token)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiErrorMessages.CAN_NOT_FIND_TOKEN)
                        .build();
            }

            UserEntity user = DatabaseAccessLayer.getUser(token);
            user.setPassword(password);
            DatabaseAccessLayer.updateUser(user);
            LOG.info("UserEntity {} successfully change password", user.getName());
            return Response.ok("Your password successfully changed to " + password).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiErrorMessages.WRONG_CREDENTIALS)
                    .build();
        }
    }

    /*curl -X POST
          -H "Content-Type: application/x-www-form-urlencoded"
          -H "Authorization: Bearer {token}"
          -H "Host: localhost:8080
          -d "email={newEmail}"
     "http://localhost:8080/profile/email"*/
    @Authorized
    @POST
    @Path("/email")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public Response changePlayerEmail(@HeaderParam("Authorization") String rawToken,
                                      @FormParam("email") String email) {
        try {

            if (email == null || "".equals(email)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiErrorMessages.WRONG_EMAIL)
                        .build();
            }

            Token token = DatabaseAccessLayer.parse(rawToken);

            if (!DatabaseAccessLayer.contains(token)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiErrorMessages.CAN_NOT_FIND_TOKEN)
                        .build();
            }

            final String findByEmailCondition = "name=\'" + email + "\'";
            Boolean checkEmail = DatabaseAccessLayer.checkByCondition(findByEmailCondition);

            if (checkEmail) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiErrorMessages.BUSY_EMAIL)
                        .build();
            }

            UserEntity user = DatabaseAccessLayer.getUser(token);
            String oldEmail = user.getEmail();
            user.setEmail(email);
            DatabaseAccessLayer.updateUser(user);
            LOG.info("UserEntity with email {} set email to {}", oldEmail, email);
            return Response.ok("Your email successfully changed to " + email).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiErrorMessages.WRONG_CREDENTIALS)
                    .build();
        }
    }
}
