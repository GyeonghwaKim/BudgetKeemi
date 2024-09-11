package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.entity.Category;
import com.example.budgeKeemi.domain.entity.Member;
import com.example.budgeKeemi.domain.type.CategoryStatus;
import com.example.budgeKeemi.domain.type.IsActive;
import com.example.budgeKeemi.dto.req.ReqCategory;
import com.example.budgeKeemi.dto.resp.RespCategory;
import com.example.budgeKeemi.dto.resp.RespCategoryStatus;
import com.example.budgeKeemi.exception.excep.UnauthorizedException;
import com.example.budgeKeemi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RequestMapping("/categories")
@Service
public class CategoryService {

    private final CategoryRepository repository;

    private final MemberService memberService;

    //나의 활성화 카테고리 목록
    public List<RespCategory> getActiveCategoriesByUsername(String username) {

        Member member = memberService.getMemberByUsername(username);

        return getRespCategories(member, IsActive.Y);
    }


    public RespCategory createCategory(ReqCategory reqCategory, String username) {

        Category category = ReqCategory.toEntity(reqCategory);

        Member member = memberService.getMemberByUsername(username);
        category.updateMember(member);

        Category saveCategory = this.repository.save(category);

        return RespCategory.toDto(saveCategory);
    }


    public RespCategory updateCategory(Long id, ReqCategory reqCategory, String username) {

        Optional<Category> _category = repository.findById(id);

        if (_category.isPresent()) {

            Category category = _category.get();

            //소유자 검증
            validationAuthorization(username, category, "수정 권한이 없습니다");

            category.replaceName(reqCategory.getName());
            category.replaceName(reqCategory.getStatus());

            Category updateCategory = repository.save(category);

            return RespCategory.toDto(updateCategory);
        } else {
            return null;
        }

    }

    public boolean changeInactiveCategory(Long id, String username) {

        Optional<Category> _category = repository.findById(id);

        if (_category.isPresent()) {
            Category category = _category.get();

            //소유자 검증
            validationAuthorization(username, category, "삭제 권한이 없습니다");

            //활성화 카테고리인지?
            if (category.getActive() == IsActive.Y) {
                category.changeActive(IsActive.N);
                repository.save(category);
                return true;
            }
            return false;

        } else {
            return false;
        }
    }

    //categoryStatus 목록 조회
    public List<RespCategoryStatus> getCategoryStatus() {

        return Arrays.stream(CategoryStatus.values()).map(
                RespCategoryStatus::toDto).toList();
    }

    // id로 카테고리 조회
    public Category getCategoryByCategoryId(Long categoryId) {

        Optional<Category> _category = repository.findById(categoryId);

        if (_category.isPresent()) {
            return _category.get();
        } else {
            return null;
        }
    }

    private static void validationAuthorization(String username, Category category, String message) {

        if (!category.getMember().getUsername().equals(username)) {
            throw new UnauthorizedException(message);
        }
    }

    private List<RespCategory> getRespCategories(Member member, IsActive isActive) {

        List<Category> categories = repository.findAllByMemberAndActive(member, isActive);

        return categories
                .stream()
                .map(RespCategory::toDto)
                .toList();
    }
}
