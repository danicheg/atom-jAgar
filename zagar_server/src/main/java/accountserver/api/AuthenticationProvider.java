package accountserver.api;

import dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import accountserver.auth.Authorized;
import entities.token.Token;
import entities.token.TokensStorage;
import entities.user.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.CopyOnWriteArrayList;

@Path("/auth")
public class AuthenticationProvider {

    private static final Logger log = LogManager.getLogger(AuthenticationProvider.class);
    private static CopyOnWriteArrayList<User> registeredUsers;
    private static UserDao userDao;

    static {
        registeredUsers = new CopyOnWriteArrayList<>();
        userDao = new UserDao();
    }

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

        if (name == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        boolean checkUser = registeredUsers.parallelStream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .isPresent();

        if (checkUser) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        User user = new User(name, password);
        userDao.insert(user);

        registeredUsers.add(user);
        log.info("New user registered with login {}", name);
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

        if (user == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {

            if (!authenticate(user, password)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            Token token = TokensStorage.issueToken(user);
            log.info("User '{}' successfully logged in", user);
            return Response.ok(Long.toString(token.getToken())).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
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

            Token token = TokensStorage.parse(rawToken);

            if (!TokensStorage.contains(token)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                User user = TokensStorage.getUser(token);
                TokensStorage.remove(token);
                log.info("User '{}' logout successfully", user.getName());
                return Response.ok(user.getName() + " successfully logout!").build();
            }

        } catch (Exception e) {
            log.warn(e);
            return Response.status(Response.Status.UNAUTHORIZED).build();

        }

    }

    @NotNull
    public static CopyOnWriteArrayList<User> getRegisteredUsers() {
        return registeredUsers;
    }

    private boolean authenticate(String name, String password) throws Exception {
        return registeredUsers.stream()
                .filter(usr -> usr.getName().equals(name) && usr.checkPassword(password))
                .findFirst()
                .isPresent();
    }

}
