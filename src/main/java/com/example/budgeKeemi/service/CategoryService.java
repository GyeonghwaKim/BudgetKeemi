package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.entity.Category;
import com.example.budgeKeemi.domain.entity.Member;
import com.example.budgeKeemi.domain.type.CategoryStatus;
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
//Todo: getCategoryByCategoryId() 로 이름 변겅
    public Category getCategoryById(Long categoryId) {
        Optional<Category> _category = repository.findById(categoryId);
        if(_category.isPresent()){
            return _category.get();
        }else {
            return null;
        }
    }

    public List<RespCategory> getCategoriesByUsername(String username) {

        Member member = memberService.getMemberByUsername(username);
        List<Category> categories = repository.findAllByMember(member);
        List<RespCategory> respCategories = categories
                .stream()
                .map(RespCategory::toDto)
                .toList();
        return respCategories;
    }

    public RespCategory createCategory(ReqCategory reqCategory,String username) {

        Member member = memberService.getMemberByUsername(username);

        Category category=ReqCategory.toEntity(reqCategory);
        category.updateMember(member);
        Category saveCategory = this.repository.save(category);

        RespCategory respCategory = RespCategory.toDto(saveCategory);

        return respCategory;
    }

    public RespCategory getCategoryDetail(Long id) {
        Optional<Category> _category = repository.findById(id);

        if(_category.isPresent()){
            return RespCategory.toDto(_category.get());
        }else{
            return null;
        }
    }

    public RespCategory updateCategory(Long id, ReqCategory reqCategory,String username) {
        
        Optional<Category> _category = repository.findById(id);

        if(_category.isPresent()){

            Category category=_category.get();

            validationAuthorization(username, category, "수정 권한이 없습니다");

            category.replaceName(reqCategory.getName());
            category.replaceName(reqCategory.getStatus());

            Category updateCategory = repository.save(category);

            return RespCategory.toDto(updateCategory);
        }else{
            return null;
        }

    }

    private static void validationAuthorization(String username, Category category, String message) {
        if(!category.getMember().getUsername().equals(username)){
            throw new UnauthorizedException(message);
        }
    }


    public boolean deleteCategory(Long id) {
        Optional<Category> _category = repository.findById(id);

        if(_category.isPresent()){
            repository.delete(_category.get());
            return true;
        }else{
            return false;
        }
    }

    public List<RespCategoryStatus> getCategoryStatus() {

        List<RespCategoryStatus> categoryStatuses
                = Arrays.stream(CategoryStatus.values()).map(
                RespCategoryStatus::toDto).toList();
            return categoryStatuses;
        }

}
