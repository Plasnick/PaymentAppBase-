package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcAccountDaoTests extends BaseDaoTests {

    private static final Account ACCOUNT_1 = new Account(2001, 1001, 1000.00);
    private static final Account ACCOUNT_2 = new Account(2002, 1002, 1000.00);
    private static final Account ACCOUNT_3 = new Account(2003, 1003, 1000.00);

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getBalance_works_correctly() {
        User user = new User();
        double account = sut.getBalance("andy");

        Assert.assertEquals(ACCOUNT_3.getBalance(), account, 0.01);
    }

    @Test
    public void subtractFromAccount_works_correctly() {
        double expected = 850.00;
        double actual = sut.subtractFromAccount(150.00, "bob");
        Assert.assertEquals(expected, actual, 0.01);
    }

    @Test
    public void addToAccount_works_correctly() {
        double expected = 1300.00;
        double actual = sut.addToAccount(300.00, "user");
        Assert.assertEquals(expected, actual, 0.01);
    }

    @Test
    public void audit_works_correctly() {

        int expected = 3004;
        int actual = sut.audit(400.00, "user", "andy");

        Assert.assertEquals(expected, actual);
    }

    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance(), 0.01);
    }
}
