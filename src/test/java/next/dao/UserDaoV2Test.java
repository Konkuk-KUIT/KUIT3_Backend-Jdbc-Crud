package next.dao;

import core.dao.UserDao;
import core.dao.UserDaoV2;
import core.jdbc.ConnectionManager;
import core.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserDaoV2Test {
    @Before
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void crud() throws Exception {
        DataSource dataSource = ConnectionManager.getDataSource();

        User expected = new User("userId", "password", "김민우", "minunu12@email.com");
        UserDaoV2 userDaoV2 = new UserDaoV2(dataSource);
        userDaoV2.insert(expected);
        User actual = userDaoV2.findByUserId(expected.getUserId());
        assertEquals(expected, actual);

        expected.update(new User("userId", "password2", "고양이", "myamya@email.com"));
        userDaoV2.update(expected);
        actual = userDaoV2.findByUserId(expected.getUserId());
        assertEquals(expected, actual);
    }

    @Test
    public void findAll() throws Exception {
        DataSource dataSource = ConnectionManager.getDataSource();

        UserDaoV2 userDaoV2 = new UserDaoV2(dataSource);
        List<User> users = userDaoV2.findAll();
        assertEquals(1, users.size());
    }
}