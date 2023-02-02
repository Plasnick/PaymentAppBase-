package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class JdbcTransferDaoTests extends BaseDaoTests {

    private static final Transfer TRANSFER_1 = new Transfer(3001, 2001, 2002, 150.00);
    private static final Transfer TRANSFER_2 = new Transfer(3002, 2002, 2001, 300.00);
    private static final Transfer TRANSFER_3 = new Transfer(3003, 2003, 2002, 200.00);

    private JdbcTransferDAO sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDAO(jdbcTemplate);
    }

    @Test
    public void getYourTransfers_works_correctly() {
       List<Transfer> transfers = sut.getYourTransfers("andy");
       Assert.assertEquals(1, transfers.size());

        List<Transfer> transfers2 = sut.getYourTransfers("user");
        Assert.assertEquals(3, transfers2.size());
    }

    @Test
    public void getYourTransfers_works_correctly_when_User_Doesnt_Exist() {
        List<Transfer> transfers = sut.getYourTransfers("Phil");
        Assert.assertEquals(0, transfers.size());
    }

    @Test
    public void getTransferById_works_correctly() {
        Transfer transfer = sut.getTransferById(3001);
        assertTransfersMatch(TRANSFER_1, transfer);

        Transfer transfer1 = sut.getTransferById(3002);
        assertTransfersMatch(TRANSFER_2, transfer1);
    }

    private void assertTransfersMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getFromAccountId(), actual.getFromAccountId());
        Assert.assertEquals(expected.getToAccountId(), actual.getToAccountId());
        Assert.assertEquals(expected.getAmount(), actual.getAmount(), 0.01);
    }
}
