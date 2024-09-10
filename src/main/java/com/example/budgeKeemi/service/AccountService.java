package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.entity.Account;
import com.example.budgeKeemi.domain.entity.Member;
import com.example.budgeKeemi.domain.type.IsActive;
import com.example.budgeKeemi.domain.type.AccountType;
import com.example.budgeKeemi.dto.req.ReqAccount;
import com.example.budgeKeemi.dto.resp.RespAccount;
import com.example.budgeKeemi.exception.excep.UnauthorizedException;
import com.example.budgeKeemi.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository repository;

    private final MemberService memberService;

    @Transactional
    public RespAccount createAccount(ReqAccount reqAccount,String username) {

        Member member = memberService.getMemberByUsername(username);

        Account account = ReqAccount.toEntity(reqAccount);
        account.updateMember(member);

        Account saveAccount = this.repository.save(account);
        RespAccount respAccount = RespAccount.toDto(saveAccount);

        return respAccount;
    }

    //나의 활성화 계좌 목록
    public List<RespAccount> getActiveAccountsByUsername(String username) {

        List<RespAccount> respAccounts = getRespAccounts(username, IsActive.Y);
        return respAccounts;
    }


    //나의 비활성화 계좌 목록
    public List<RespAccount> getInactiveAccountsByUsername(String username) {

        List<RespAccount> respAccounts = getRespAccounts(username, IsActive.N);
        return respAccounts;
    }

    //나의 모든 계좌 목록
    public List<RespAccount> getAccountsByUsername(String username) {

        List<RespAccount> respAccounts = getRespAccounts(username);
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

    public RespAccount updateAccount(Long id, ReqAccount reqAccount, String username) {

        Optional<Account> _account = repository.findById(id);

        if (_account.isPresent()) {

            Account account = _account.get();

            //소유자 검증
            validationAuthorization(account, username, "수정 권한이 없습니다");

            account.replaceName(reqAccount.getName());
            account.replaceBalance(reqAccount.getBalance());
            account.replaceStatus(reqAccount.getStatus());
            Account updateAccount = repository.save(account);

            return RespAccount.toDto(updateAccount);

        } else {
            return null;
        }


    }

    public boolean changeInactiveAccount(Long id, String username) {

        Optional<Account> _account = repository.findById(id);

        if (_account.isPresent()) {
            Account account = _account.get();

            //소유자 검증
            validationAuthorization(account, username, "삭제 권한이 없습니다");

            //활성화 계좌인지?
            if(account.getActive()==IsActive.Y){
                account.changeActive(IsActive.N);
                repository.save(account);
                return true;
            }

            return  false;
        } else {
            return false;
        }

    }


    public Account getAccountById(Long id) {
        Optional<Account> _account = repository.findById(id);

        if (_account.isPresent()) {
            return _account.get();
        } else {
            return null;
        }
    }

    public List<String> getAccountType() {

        List<String> typeList=new ArrayList<>();

        for (AccountType value : AccountType.values()) {
            typeList.add(value.name());
        }
        return typeList;
    }

    private List<RespAccount> getRespAccounts(String username) {
        Member member=memberService.getMemberByUsername(username);
        List<Account> accounts = repository.findAllByMember(member);
        List<RespAccount> respAccounts = accounts
                .stream()
                .map(RespAccount::toDto)
                .toList();
        return respAccounts;
    }


    private List<RespAccount> getRespAccounts(String username, IsActive isActive) {
        Member member=memberService.getMemberByUsername(username);
        List<Account> accounts = repository.findAllByMemberAndActive(member, isActive);
        List<RespAccount> respAccounts = accounts
                .stream()
                .map(RespAccount::toDto)
                .toList();
        return respAccounts;
    }

    private static void validationAuthorization(Account account, String username, String message) {
        if (!account.getMember().getUsername().equals(username)) {
            throw new UnauthorizedException(message);
        }
    }
}