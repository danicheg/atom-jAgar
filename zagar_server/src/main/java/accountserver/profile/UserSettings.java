package accountserver.profile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import accountserver.api.AuthenticationProvider;
import accountserver.auth.Authorized;
import accountserver.entities.token.Token;
import accountserver.entities.token.TokensStorage;
import accountserver.entities.user.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("profile")
public class UserSettings {

    private static final Logger log = LogManager.getLogger(UserSettings.class);

    // curl -X POST
    //      -H "Content-Type: application/x-www-form-urlencoded"
    //      -H "Authorization: Bearer {token}"
    //      -H "Host: localhost:8080
    //      -d "name={newName}"
    // "http://localhost:8080/profile/name"
    @Authorized
    @POST
    @Path("/name")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public Response changePlayerName(@HeaderParam("Authorization") String rawToken,
                                     @FormParam("name") String name) {
        try {

            if (name == null || name.equals("")) {
                log.warn("Wrong name - " + name);
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            Token token = TokensStorage.parse(rawToken);

            if (!TokensStorage.contains(token)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            Boolean checkName = AuthenticationProvider.getRegisteredUsers().stream()
                    .map(User::getName)
                    .filter(name::equals)
                    .findFirst()
                    .isPresent();

            if (checkName) {
                log.warn("User try to change name, but name is already present: " + name);
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            User user = TokensStorage.getUser(token);
            String oldName = user.getName();
            TokensStorage.remove(token);
            user.setName(name);
            TokensStorage.add(user, token);
            log.info("User with name {} set name to {}", oldName, name);
            return Response.ok("Your name successfully changed to " + name).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    // curl -X POST
    //      -H "Content-Type: application/x-www-form-urlencoded"
    //      -H "Authorization: Bearer {token}"
    //      -H "Host: localhost:8080
    //      -d "password={newPassword}"
    // "http://localhost:8080/profile/password"
    @Authorized
    @POST
    @Path("/password")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public Response changePlayerPassword(@HeaderParam("Authorization") String rawToken,
                                     @FormParam("password") String password) {
        try {

            if (password == null || password.equals("")) {
                log.warn("Wrong password - " + password);
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            Token token = TokensStorage.parse(rawToken);

            if (!TokensStorage.contains(token)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            User user = TokensStorage.getUser(token);
            String oldPassword = user.getPassword();
            TokensStorage.remove(token);
            user.setPassword(password);
            TokensStorage.add(user, token);
            log.info("User with password {} set password to {}", oldPassword, password);
            return Response.ok("Your password successfully changed to " + password).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    // curl -X POST
    //      -H "Content-Type: application/x-www-form-urlencoded"
    //      -H "Authorization: Bearer {token}"
    //      -H "Host: localhost:8080
    //      -d "email={newEmail}"
    // "http://localhost:8080/profile/email"
    @Authorized
    @POST
    @Path("/email")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public Response changePlayerEmail(@HeaderParam("Authorization") String rawToken,
                                         @FormParam("email") String email) {
        try {

            if (email == null || email.equals("")) {
                log.warn("Wrong email - " + email);
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            Token token = TokensStorage.parse(rawToken);

            if (!TokensStorage.contains(token)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            Boolean checkEmail = AuthenticationProvider.getRegisteredUsers().stream()
                    .map(User::getEmail)
                    .filter(email::equals)
                    .findFirst()
                    .isPresent();

            if (checkEmail) {
                log.warn("User try to change email, but email is already present: " + email);
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            User user = TokensStorage.getUser(token);
            String oldEmail = user.getEmail();
            TokensStorage.remove(token);
            user.setEmail(email);
            TokensStorage.add(user, token);
            log.info("User with email {} set email to {}", oldEmail, email);
            return Response.ok("Your email successfully changed to " + email).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
