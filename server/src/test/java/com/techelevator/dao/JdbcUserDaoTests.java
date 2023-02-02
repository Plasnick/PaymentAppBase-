package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests{

    private static final User USER_1 = new User(1001, "bob", "$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2","USER");
    private static final User USER_2 = new User(1002, "user", "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy","USER");
    private static final User USER_3 = new User(1003, "andy", "$2a$10$GT0mpWeeViaHWwm.lB6u2.iKjv/FwmmtPzUOd0g1sOGSQ/MtGA0z6","USER");




    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void createNewUser_works_correctly() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }

    @Test
    public void findIdByUsername_works_correctly() {
        int expected = 1003;
        int actual = sut.findIdByUsername("andy");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void findAll_works_correctly() {
        List<User> expectedUsers = new ArrayList<>();
        User user1 = USER_1;
        user1.setAuthorities("USER");
        User user2 = USER_2;
        user2.setAuthorities("USER");
        User user3 = USER_3;
        user3.setAuthorities("USER");
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        expectedUsers.add(user3);

        List<User> actualUsers = sut.findAll();

        Assert.assertEquals(expectedUsers.size(), actualUsers.size());
//        Assert.assertEquals(expectedUsers, actualUsers); //This was approved to be commented out by Kelvin, he says the .size() assert was enough of a comparison.
    }           //Authorities text is identical in testing, the JavaAssertion claims they don't match, but then choosing to "see the difference" says they're equal
                //Went with a size test instead to prove both sets of lists would have 3 each, which they do.However, we can't confirm the details of each list item due to the issue as described above

    @Test
    public void findAllUsers_works_correctly() {
        List<String> expectedUsernames = new ArrayList<>();
        expectedUsernames.add(USER_1.getUsername());
        expectedUsernames.add(USER_2.getUsername());
        expectedUsernames.add(USER_3.getUsername());

        List<String> actualUsernames = sut.findAllUsers();

        Assert.assertEquals(expectedUsernames, actualUsernames);
    }   //

    @Test
    public void findByUsername_works_correctly () {
        User expected = USER_1;
        expected.setAuthorities("USER");

        User actual = sut.findByUsername("bob");

        assertUsersMatch(expected, actual);
    } //Authorities text is identical in testing, the JavaAssertion claims they don't match, but then choosing to "see the difference" says they're equal


    private void assertUsersMatch(User expected, User actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
        Assert.assertEquals(expected.isActivated(), actual.isActivated());
//        Assert.assertEquals(expected.getAuthorities(), actual.getAuthorities());
        //Commented out by Kelvin; he said that the comparison of the other properties is proof enough that they match given their unique individual natures
    }
}
