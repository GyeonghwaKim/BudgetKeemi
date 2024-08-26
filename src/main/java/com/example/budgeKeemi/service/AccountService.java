package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.Account;
import com.example.budgeKeemi.dto.ReqAccount;
import com.example.budgeKeemi.dto.RespAccount;
import com.example.budgeKeemi.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository repository;

    @Transactional
    public RespAccount createAccount(ReqAccount reqAccount) {

        Account account = ReqAccount.toEntity(reqAccount);
        Account saveAccount = this.repository.save(account);
        RespAccount respAccount = RespAccount.toDto(saveAccount);

        return respAccount;
    }

    public List<RespAccount> getAccounts() {
        List<Account> accounts = repository.findAll();

        List<RespAccount> respAccounts = accounts
                .stream()
                .map(RespAccount::toDto)
                .toList();

        return respAccounts;
    }

    public RespAccount getAccountDetails(Long id) {
        Optional<Account> _account = repository.findById(id);

        if (_account.isPresent()) {
            Account account = _account.get();

            return RespAccount.toDto(account);
        } else {
            return null;
        }
    }

    public RespAccount updateAccountDetails(Long id, ReqAccount reqAccount) {

        Optional<Account> _account = repository.findById(id);

        if (_account.isPresent()) {
            Account account = _account.get();
            account.setName(reqAccount.getName());
            Account updateAccount = repository.save(account);

            return RespAccount.toDto(updateAccount);
        } else {
            return null;
        }


    }

    public boolean deleteAccount(Long id) {

        Optional<Account> _account = repository.findById(id);

        if (_account.isPresent()) {
            Account account = _account.get();
            repository.delete(account);

            return  true;
        } else {
            return false;
        }

    }
}