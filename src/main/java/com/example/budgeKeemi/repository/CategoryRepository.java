package com.example.budgeKeemi.repository;

import com.example.budgeKeemi.domain.entity.Category;
import com.example.budgeKeemi.domain.entity.Member;
import com.example.budgeKeemi.domain.type.IsActive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByMemberAndActive(Member member, IsActive active);
}
