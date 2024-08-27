package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.Category;
import com.example.budgeKeemi.domain.Transaction;
import com.example.budgeKeemi.dto.ReqCategory;
import com.example.budgeKeemi.dto.RespCategory;
import com.example.budgeKeemi.dto.RespTransaction;
import com.example.budgeKeemi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RequestMapping("/categories")
@Service
public class CategoryService {

    private final CategoryRepository repository;

    public Category getCategoryById(Long categoryId) {
        Optional<Category> _category = repository.findById(categoryId);
        if(_category.isPresent()){
            return _category.get();
        }else {
            return null;
        }
    }

    public List<RespCategory> getCategories() {
        List<Category> categories = repository.findAll();
        List<RespCategory> respCategories = categories.stream().map(RespCategory::toDto).toList();
        return respCategories;
    }

    public RespCategory createCategory(ReqCategory reqCategory) {
        Category category=ReqCategory.toEntity(reqCategory);

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

    public RespCategory updateCategory(Long id, ReqCategory reqCategory) {

        Optional<Category> _category = repository.findById(id);

        if(_category.isPresent()){

            Category category=_category.get();

            category.updateName(reqCategory.getName());
            category.updateStatus(reqCategory.getStatus());

            Category updateCategory = repository.save(category);

            return RespCategory.toDto(updateCategory);
        }else{
            return null;
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
}
