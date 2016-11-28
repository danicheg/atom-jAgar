package test_client;


import entities.leaderboard.Leaderboard;
import entities.user.UserEntity;

import java.util.List;

public interface RestClient {
    boolean register(String name, String password);
    Long authenticateUser(String name, String password);
    boolean logoutPlayer(Long token);
    boolean changePlayerName(Long token, String newName);
    boolean changePlayerPassword(Long token, String newName);
    boolean changePlayerEmail(Long token, String newName);
    List<UserEntity> getNLeaders(Integer input, Leaderboard leaderboard);
    String[] getNLeaderNames(Integer input, Leaderboard leaderboard);
    boolean addScore(Long token, Integer input);
}