package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Widget;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    UserDao userDAO;
    AccountDao accountDao;
    TransferDAO transferDAO;

    public UserController(UserDao userDAO, AccountDao accountDao, TransferDAO transferDAO) {
        this.userDAO = userDAO;
        this.accountDao = accountDao;
        this.transferDAO = transferDAO;
    }
    @RequestMapping(path = "/balance", method = RequestMethod.GET)     // can remove /{id}
    public double getBalance(Principal principal) {        //Instead of PathVariable, use Principal principal
         double balance = accountDao.getBalance(principal.getName());            //principal
        return balance;
    }

    @RequestMapping(path = "/allUsers", method = RequestMethod.GET)
    public List<String> getAllUsers() {
        List<String> userList = new ArrayList<>();
        userList = userDAO.findAllUsers();

        return userList;
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id) {
        Transfer transfer = transferDAO.getTransferById(id);
        return transfer;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(path = "/send", method = RequestMethod.PUT)
    public double sendMoney(@Valid @RequestBody Widget widget, Principal principal) {
        if (widget.getTo_user().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This was pointless...");
        }
        if (widget.getTransfer_amount() >= accountDao.getBalance(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "That's not allowed, and you know that...scammer");
        }
        if (widget.getTransfer_amount() <= 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must really not know how math works, huh?");
        }
        accountDao.subtractFromAccount(widget.getTransfer_amount(), principal.getName());
        accountDao.addToAccount(widget.getTransfer_amount(), widget.getTo_user());
        accountDao.audit(widget.getTransfer_amount(), principal.getName(), widget.getTo_user());

        double balance = accountDao.getBalance(principal.getName());

        return balance;
    }

    @RequestMapping(path="/getMyTransfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(Principal principal) {
        List<Transfer> transfers = new ArrayList<>();
        transfers = transferDAO.getYourTransfers(principal.getName());
        //transfers.toString();
        return transfers;
    }

//    @RequestMapping(path = "/balance", method = RequestMethod.GET)
//    public Account accountBalance() {
//        return accountDao.getBalance();
//    }

}
