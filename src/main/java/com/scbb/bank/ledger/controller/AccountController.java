package com.scbb.bank.ledger.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.ledger.model.Account;
import com.scbb.bank.ledger.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("accounts")
public class AccountController implements AbstractController<Account, Integer> {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> findAll() {
        return modifyResources(accountService.findAll());
    }

    @GetMapping("{id}")
    public Account findById(@PathVariable Integer id) {
        return modifyResource(accountService.findById(id));
    }

    @GetMapping("number/{number}")
    public Account findByNumber(@PathVariable String number) {
        return modifyResource(accountService.findByNumber(number));
    }

    @PostMapping
    @PutMapping
    public Account persist(@RequestBody @Valid Account account) {
        return modifyResource(accountService.persist(account));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        if (accountService.delete(id))
            return ResponseEntity.ok(true);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
    }

    @PutMapping("search")
    public List<Account> search(@RequestBody Account account) {
        return modifyResources(accountService.search(account));
    }

    @Override
    public Account modifyResource(Account account) {
        if (account.getShareHolder() != null) {
            account.getShareHolder().setShareAccount(null);
            account.getShareHolder().setSubsidy(null);
            account.getShareHolder().setBoardMember(null);
            account.getShareHolder().setTeam(null);
            account.getShareHolder().setSavingsList(null);
        }
        if (account.getTeam() != null) {
            account.getTeam().setAccount(null);
            account.getTeam().setMemberList(null);
            account.getTeam().setSociety(null);
        }
        if (account.getAccountType() != null) {
            account.getAccountType().setAccountList(null);
            account.getAccountType().setSubAccountTypeList(null);
        }
        if (account.getSubAccountType() != null) {
            account.getSubAccountType().setAccountType(null);
            account.getSubAccountType().setAccountList(null);
        }

        if (account.getSavings() != null) {
            account.getSavings().setAccount(null);
            account.getSavings().setMember(null);
            account.getSavings().setSavingType(null);
        }
        if (account.getLoan() != null) {
            account.getLoan().setAccount(null);
            account.getLoan().setLoanType(null);
            account.getLoan().setMember(null);
        }
        return account;
    }

    @Override
    public List<Account> modifyResources(List<Account> accounts) {
        return accounts.stream()
                .map(this::modifyResource)
                .collect(Collectors.toList());
    }
}