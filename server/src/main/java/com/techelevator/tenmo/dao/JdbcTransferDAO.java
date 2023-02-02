package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDAO implements TransferDAO{

    private JdbcTemplate template;

    public JdbcTransferDAO(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List <Transfer> getYourTransfers(String username) {
        List <Transfer> transfers = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM transfer " +
                "Join account ON account.account_id = transfer.from_account_id " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE username = ?";
        SqlRowSet result = template.queryForRowSet(sql, username);
        while(result.next()) {
            Transfer transfer = mapRowToTransfer(result);
            System.out.println(transfer.getTransferId());
            transfers.add(transfer);
        }
        String sql1 = "SELECT * " +
                "FROM transfer " +
                "Join account ON account.account_id = transfer.to_account_id " +
                "JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE username = ?";
        SqlRowSet result1 = template.queryForRowSet(sql1, username);
        while(result1.next()) {
            Transfer transfer = mapRowToTransfer(result1);
            System.out.println(transfer.getTransferId());
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransferById(int id) {

        String sql = "SELECT *" +
                " From transfer" +
                " WHERE transfer_id = ?;";
        Transfer transfer = null;
        SqlRowSet result = template.queryForRowSet(sql, id);


        if(result.next()) {
            transfer = mapRowToTransfer(result);
        }
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer Not Found");
        } else {
            return transfer;
        }
    }

    @Override
    public Transfer mapRowToTransfer(SqlRowSet rs) {
        int transferId = rs.getInt("transfer_id");
        int fromAccountId = rs.getInt("from_account_id");
        int toAccountId = rs.getInt("to_account_id");
        double amount = rs.getDouble("amount");
        Transfer transfer = new Transfer(transferId, fromAccountId, toAccountId, amount);
        return transfer;
    }
}
