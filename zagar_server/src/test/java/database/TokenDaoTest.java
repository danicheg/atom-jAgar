package database;

import dao.TokenDao;
import entities.token.Token;
import entities.user.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class TokenDaoTest {

    private TokenDao firstTokenDao;
    private Token firstToken;
    private Token secondToken;
    private Token thirdToken;

    @Before
    public void setUp() {
        firstTokenDao = new TokenDao();
        firstToken = new Token(0L, new User("userOne", "password"));
        secondToken = new Token(1L, new User("userTwo", "password"));
        thirdToken = new Token(2L, new User("userThree", "password"));
    }

    @Test
    public void getAllToken() {
        assertThat(firstTokenDao.getAll()).hasSize(0);
    }

    @Test
    public void insertTokenTest(){
        final int initialSize = firstTokenDao.getAll().size();
        firstTokenDao.insert(firstToken);
        assertThat(firstTokenDao.getAll())
                .hasSize(initialSize + 1)
                .extracting(Token::getToken, Token::getDate)
                .contains(tuple(0L, LocalDate.now()));
        firstTokenDao.delete(firstToken);
    }

    @Test
    public void deleteTest() {
        firstTokenDao.insert(firstToken);
        final int initialSize = firstTokenDao.getAll().size();
        firstTokenDao.delete(firstToken);
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
