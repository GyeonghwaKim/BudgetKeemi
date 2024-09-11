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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository repository;

    private final MemberService memberService;

    //계좌 생성
    public RespAccount createAccount(ReqAccount reqAccount,String username) {

        Account account = ReqAccount.toEntity(reqAccount);

        Member member = memberService.getMemberByUsername(username);
        account.updateMember(member);

        Account saveAccount = this.repository.save(account);
        return RespAccount.toDto(saveAccount);
    }

    //나의 활성화 계좌 목록
    public List<RespAccount> getActiveAccountsByUsername(String username) {

        return getRespAccounts(username, IsActive.Y);
    }

    //나의 비활성화 계좌 목록
    public List<RespAccount> getInactiveAccountsByUsername(String username) {

        return getRespAccounts(username, IsActive.N);
    }

    //나의 모든 계좌 목록
    public List<RespAccount> getAccountsByUsername(String username) {

        return getRespAccounts(username);
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

    public boolean disableAccount(Long id, String username) {

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

        return Arrays.stream(AccountType.values())
                .map(AccountType::name)
                .toList();
    }

    private List<RespAccount> getRespAccounts(String username) {

        Member member=memberService.getMemberByUsername(username);

        List<Account> accounts = repository.findAllByMember(member);

        return accounts.stream()
                .map(RespAccount::toDto)
                .toList();
    }


    private List<RespAccount> getRespAccounts(String username, IsActive isActive) {

        Member member=memberService.getMemberByUsername(username);

        List<Account> accounts = repository.findAllByMemberAndActive(member, isActive);

        return accounts.stream()
                .map(RespAccount::toDto)
                .toList();
    }

    private static void validationAuthorization(Account account, String username, String message) {

        if (!account.getMember().getUsername().equals(username)) {
            throw new UnauthorizedException(message);
        }
    }
}