package database;

import dao.TokenDao;
import dao.UserDao;
import entities.token.Token;
import entities.user.UserEntity;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class TokenDaoTest {

    private TokenDao tokenDao;
    private UserDao userDao;

    private Token firstToken;
    private Token secondToken;
    private Token thirdToken;

    private UserEntity user1;
    private UserEntity user2;
    private UserEntity user3;

    @Before
    public void setUp() {

        tokenDao = new TokenDao();
        userDao = new UserDao();

        user1 = new UserEntity("userOne", "password");
        user2 = new UserEntity("userTwo", "password");
        user3 = new UserEntity("userThree", "password");

        firstToken = new Token(0L, user1);
        secondToken = new Token(1L, user2);
        thirdToken = new Token(2L, user3);

    }

    @Test
    public void getAllToken() {
        assertThat(tokenDao.getAll()).hasSize(0);
    }

    @Test
    public void insertTokenTest(){
        final int initialSize = tokenDao.getAll().size();
        tokenDao.insert(firstToken);
        userDao.insert(user1);
        assertThat(tokenDao.getAll())
                .hasSize(initialSize + 1)
                .extracting(Token::getToken, Token::getDate, Token::getUser)
                .contains(tuple(0L, LocalDate.now(), user1));
        assertThat(userDao.getAll()).extracting(UserEntity::getToken).contains(firstToken);
        tokenDao.delete(firstToken);
        userDao.delete(user1);
    }

    @Test
    public void deleteTest() {
        tokenDao.insert(firstToken);
        userDao.insert(user1);
        final int initialSize = tokenDao.getAll().size();
        tokenDao.delete(firstToken);
        userDao.delete(user1);
        assertThat(tokenDao.getAll()).hasSize(initialSize - 1);
    }

    @Test
    public void insertAllTokensTest(){
        final int initialSize = tokenDao.getAll().size();
        tokenDao.insertAll(firstToken, secondToken, thirdToken);
        userDao.insertAll(user1, user2, user3);
        assertThat(tokenDao.getAll()).hasSize(initialSize + 3);
        tokenDao.deleteAll(firstToken, secondToken, thirdToken);
        userDao.deleteAll(user1, user2, user3);
    }

    @Test
    public void deleteAllTest(){
        tokenDao.insertAll(firstToken, secondToken, thirdToken);
        userDao.insertAll(user1, user2, user3);
        assertThat(tokenDao.getAll()).hasSize(3);
        tokenDao.deleteAll(firstToken, secondToken, thirdToken);
        userDao.deleteAll(user1, user2, user3);
        assertThat(tokenDao.getAll()).hasSize(0);
    }
}
