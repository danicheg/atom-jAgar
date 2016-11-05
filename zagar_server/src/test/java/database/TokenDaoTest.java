package database;

import dao.TokenDao;
import dao.UserDao;
import entities.token.Token;
import entities.user.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class TokenDaoTest {

    private TokenDao firstTokenDao;
    private UserDao userDao;

    private Token firstToken;
    private Token secondToken;
    private Token thirdToken;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {

        firstTokenDao = new TokenDao();
        userDao = new UserDao();

        user1 = new User("userOne", "password");
        user2 = new User("userTwo", "password");
        user3 = new User("userThree", "password");

        firstToken = new Token(0L, user1);
        secondToken = new Token(1L, user2);
        thirdToken = new Token(2L, user3);

    }

    @Test
    public void getAllToken() {
        assertThat(firstTokenDao.getAll()).hasSize(0);
    }

    @Test
    public void insertTokenTest(){
        final int initialSize = firstTokenDao.getAll().size();
        user1.setToken(firstToken);
        userDao.insert(user1);
        assertThat(firstTokenDao.getAll())
                .hasSize(initialSize + 1)
                .extracting(Token::getToken, Token::getDate, Token::getUser)
                .contains(tuple(0L, LocalDate.now(), user1));
        assertThat(userDao.getAll()).extracting(User::getToken).contains(firstToken);
        firstTokenDao.delete(firstToken);
    }

    @Test
    public void deleteTest() {
        user1.setToken(firstToken);
        userDao.insert(user1);
        final int initialSize = firstTokenDao.getAll().size();
        userDao.delete(user1);
        assertThat(firstTokenDao.getAll()).hasSize(initialSize - 1);
    }

    @Test
    public void insertAllTokensTest(){
        final int initialSize = firstTokenDao.getAll().size();
        firstTokenDao.insertAll(firstToken, secondToken, thirdToken);
        assertThat(firstTokenDao.getAll()).hasSize(initialSize + 3);
        firstTokenDao.deleteAll(firstToken, secondToken, thirdToken);
    }

    @Test
    public void deleteAllTest(){
        firstTokenDao.insertAll(firstToken, secondToken, thirdToken);
        final int initialSize = firstTokenDao.getAll().size();
        firstTokenDao.deleteAll(firstToken, secondToken, thirdToken);
        assertThat(firstTokenDao.getAll()).hasSize(initialSize - 3);
    }
}
