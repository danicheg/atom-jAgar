package zagar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zagar.auth.AuthClient;
import zagar.network.ServerConnectionSocket;
import zagar.network.packets.PacketMove;
import zagar.util.Reporter;
import zagar.view.Blob;
import zagar.view.Cell;
import zagar.view.Food;
import zagar.view.GameFrame;
import zagar.view.Virus;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import static protocol.GameConstraints.*;

public class Game {

    @NotNull
    private static final Logger LOG = LogManager.getLogger(Game.class);

    @NotNull
    public static volatile Cell[] cells = new Cell[0];

    @NotNull
    public static volatile Virus[] viruses = new Virus[0];

    @NotNull
    public static volatile Food[] foods = new Food[0];

    @NotNull
    public static volatile Blob[] blobs = new Blob[0];

    @NotNull
    public static ConcurrentLinkedDeque<Cell> player = new ConcurrentLinkedDeque<>();

    @NotNull
    public static String[] leaderBoard = new String[10];

    public static double maxSizeX = GameFrame.size.width;
    public static double maxSizeY = GameFrame.size.height;
    public static double minSizeX = 0;
    public static double minSizeY = 0;

    @NotNull
    private static List<Integer> playerID = new ArrayList<>();

    public static float followX;
    public static float followY;
    public static double zoom;
    public static int score;

    @NotNull
    public static ServerConnectionSocket socket;

    @NotNull
    public static String serverToken;

    @NotNull
    public static String login = DEFAULT_LOGIN;

    @NotNull
    public static Map<Integer, String> cellNames = new HashMap<>();

    public static long fps = 60;

    @NotNull
    public static GameState state = GameState.NOT_AUTHORIZED;

    @NotNull
    private String gameServerUrl = "ws://" + DEFAULT_GAME_SERVER_HOST +
            ":" + DEFAULT_GAME_SERVER_PORT;

    @NotNull
    private AuthClient authClient = new AuthClient();

    private double zoomm = -1;

    public Game() throws Exception {

        try {
            authenticate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Problem with login in game");
        }

        final WebSocketClient client = new WebSocketClient();
        socket = new ServerConnectionSocket();
        new Thread(() -> {
            try {
                client.start();
                URI serverURI = new URI(gameServerUrl + "/clientConnection");
                ClientUpgradeRequest request = new ClientUpgradeRequest();
                request.setHeader("Origin", "zagar.io");
                client.connect(socket, serverURI, request);
                LOG.info("Trying to connect <" + gameServerUrl + ">");
                socket.awaitClose(7, TimeUnit.DAYS);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }).start();
    }

    private void authenticate() throws Exception {
        while (serverToken == null) {

            AuthOption authOption = chooseAuthOption();
            if (authOption == null) {
                throw new Exception("AuthOption is null");
            }

            login = JOptionPane.showInputDialog(
                    null,
                    "Login",
                    DEFAULT_LOGIN
            );

            String password = JOptionPane.showInputDialog(
                    null,
                    "Password",
                    DEFAULT_PASSWORD
            );

            if (login == null) {
                throw new Exception("Login input closed");
            }

            if (password == null) {
                throw new Exception("Register input closed");
            }

            if (authOption == AuthOption.REGISTER) {
                if (!authClient.register(login, password)) {
                    Reporter.reportFail("Register failed", "Register failed");
                }
            } else if (authOption == AuthOption.LOGIN) {
                String authToken = authClient.login(Game.login, password);
                if (authToken == null) {
                    authenticate();
                } else {
                    serverToken = "Bearer " + authToken;
                    if (serverToken == null) {
                        Reporter.reportWarn("Login failed", "Login failed");
                    }
                }
            }
        }
    }

    @Nullable
    private AuthOption chooseAuthOption() {
        Object[] options = {AuthOption.LOGIN, AuthOption.REGISTER};
        int authOption = JOptionPane.showOptionDialog(
                null,
                "Choose authentication option",
                "Authentication",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );

        if (authOption == 0) {
            return AuthOption.LOGIN;
        }
        if (authOption == 1) {
            return AuthOption.REGISTER;
        }
        return null;
    }

    public void tick() throws IOException {

        LOG.info("[TICK]");

        ArrayList<Integer> toRemove = new ArrayList<>();

        for (int i : playerID) {
            for (Cell c : Game.cells) {
                if (c != null) {
                    if (c.id == i && !player.contains(c)) {
                        LOG.info("Centered cell " + c.name);
                        player.add(c);
                        toRemove.add(i);
                    }
                }
            }
        }

        for (int i : toRemove) {
            playerID.remove(playerID.indexOf(i));
        }

        if (socket.session != null && !player.isEmpty()) {

            float totalSize = 0;
            int newScore = 0;

            for (Cell c : player) {
                totalSize += c.size;
                newScore += c.mass;
            }

            if (newScore > score) {
                score = newScore;
            }

            zoomm = GameFrame.size.height / (1024 / Math.pow(Math.min(64.0 / totalSize, 1), 0.4));

            if (zoomm > 1) {
                zoomm = 1;
            }

            if (zoomm == -1) {
                zoomm = zoom;
            }
            zoom += (zoomm - zoom) / 40f;

            if (socket.session.isOpen()) {
                if (Main.frame.isActive()) {
                    if ((GameFrame.mouseX >= 0)
                            && (GameFrame.mouseY >= 0)
                            && (GameFrame.mouseX <= Main.frame.getWidth())
                            && (GameFrame.mouseY <= Main.frame.getHeight())) {
                        float avgX = 0;
                        float avgY = 0;
                        totalSize = 0;

                        for (Cell c : Game.player) {
                            avgX += c.x;
                            avgY += c.y;
                            totalSize += c.size;
                        }

                        avgX /= Game.player.size();
                        avgY /= Game.player.size();

                        float x = avgX;
                        float y = avgY;
                        x += (float) ((GameFrame.mouseX - GameFrame.size.width / 2) / zoom);
                        y += (float) ((GameFrame.mouseY - GameFrame.size.height / 2) / zoom);

                        followX = x;
                        followY = y;
                        new PacketMove(x, y, login).write();
                    }
                }
            }
        }

        for (Cell cell : cells) {
            if (cell != null) {
                cell.tick();
            }
        }

        for (Virus virus : viruses) {
            if (virus != null) {
                virus.tick();
            }
        }

        for (Blob blob : blobs) {
            if (blob != null) {
                blob.tick();
            }
        }

    }

    private enum AuthOption {
        REGISTER, LOGIN
    }

    public enum GameState {
        NOT_AUTHORIZED, AUTHORIZED
    }
}
