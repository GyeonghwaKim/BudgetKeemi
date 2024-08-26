package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.Category;
import com.example.budgeKeemi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
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
}
