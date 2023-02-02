package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;

public interface TransferDAO {
     public Transfer mapRowToTransfer(SqlRowSet rs);

     public List<Transfer> getYourTransfers(String username);

     Transfer getTransferById(int id);
}
