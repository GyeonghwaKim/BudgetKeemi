package com.example.budgeKeemi.repository;

import com.example.budgeKeemi.domain.entity.Account;
import com.example.budgeKeemi.domain.entity.Member;
import com.example.budgeKeemi.domain.type.IsActive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    List<Account> findAllByMember(Member member);

    List<Account> findAllByMemberAndActive(Member member, IsActive isActive);

}
