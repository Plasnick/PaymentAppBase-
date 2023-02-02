package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate template;

    public JdbcAccountDao(JdbcTemplate template) {
        this.template = template;
    }
    @Override
    public double getBalance(String userName) {
        double balance = 0;

        String sql = "SELECT * " +
                "FROM account " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "where username = ?";      //incorporate principal
        SqlRowSet result = template.queryForRowSet(sql, userName);      //issue with "Bad SQL grammar"
//            double balance = result.getDouble("balance");
        if(result.next()) {
            balance = mapRowToAccount(result).getBalance();
        }
        return balance;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        int accountId = rs.getInt("account_id");
        int userId = rs.getInt("user_id");
        double balance = rs.getDouble("balance");
        Account account = new Account(accountId, userId, balance);
        return account;
    }

    public double subtractFromAccount(double amount, String userName) {

        String sql1 = "SELECT *" +
                " FROM account" +
                " JOIN tenmo_user ON tenmo_user.user_id = account.user_id" +
                " WHERE username = ?";
        SqlRowSet result = template.queryForRowSet(sql1, userName);

        int accountId = 0;
        if(result.next()) {
            accountId = mapRowToAccount(result).getId();
        }

        String sql2 = "UPDATE account" +
                " SET balance = balance - ?" +
                " WHERE account_id = ?";
        //SqlRowSet subtractFunds =
        template.update(sql2, amount, accountId);
        return getBalance(userName);
    }

    public double addToAccount(double amount, String userName) {

        String sql1 = "SELECT *" +
                " FROM account" +
                " JOIN tenmo_user ON tenmo_user.user_id = account.user_id" +
                " WHERE username = ?";
        SqlRowSet result = template.queryForRowSet(sql1, userName);

        int accountId = 0;
        if(result.next()) {
           accountId = mapRowToAccount(result).getId();
        }

        String sql2 = "UPDATE account" +
                " SET balance = balance + ?" +
                " WHERE account_id = ?";
        //SqlRowSet addFunds =
        template.update(sql2, amount, accountId);
        return getBalance(userName);
    }

    public int audit(double amount, String sendingUser, String receivingUser) {
        String sql1 = "SELECT *" +
                " FROM account" +
                " JOIN tenmo_user ON tenmo_user.user_id = account.user_id" +
                " WHERE username = ?";
        SqlRowSet result = template.queryForRowSet(sql1, sendingUser);

        int sendingAccountId = 0;
        if(result.next()) {
            sendingAccountId = mapRowToAccount(result).getId();
        }

        String sql2 = "SELECT *" +
                " FROM account" +
                " JOIN tenmo_user ON tenmo_user.user_id = account.user_id" +
                " WHERE username = ?";
        SqlRowSet result2 = template.queryForRowSet(sql2, receivingUser);

        int receivingAccountId = 0;
        if(result2.next()) {
            receivingAccountId = mapRowToAccount(result2).getId();
        }

        String sqlAudit = "INSERT INTO transfer (from_account_id, to_account_id, amount) " +
                "Values (?, ?, ?) RETURNING transfer_id";
       int newId = template.queryForObject(sqlAudit, int.class, sendingAccountId, receivingAccountId, amount);

        return newId;
    }
}
