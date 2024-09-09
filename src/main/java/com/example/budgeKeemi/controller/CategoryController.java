package com.example.budgeKeemi.controller;

import com.example.budgeKeemi.dto.req.ReqCategory;
import com.example.budgeKeemi.dto.resp.RespCategory;
import com.example.budgeKeemi.dto.resp.RespCategoryStatus;
import com.example.budgeKeemi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/categories")
@Controller
public class CategoryController {

    private final CategoryService service;


    //카테고리 목록 조회
    @GetMapping
    public ResponseEntity<?> getCategories(){
        List<RespCategory> categories= service.getCategories();
        return ResponseEntity.ok(categories);
    }

    //카테고리 생성
    @PostMapping
    public ResponseEntity<?> addCategories(@RequestBody ReqCategory reqCategory){

        RespCategory respCategory=this.service.createCategory(reqCategory);

        return new ResponseEntity<>(respCategory, HttpStatus.CREATED);
    }


    //카테고리 상세조회
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryDetail(@PathVariable(name = "categoryId") Long id){

        RespCategory respCategory=service.getCategoryDetail(id);

        return ResponseEntity.ok(respCategory);

    }
    //카테고리 수정
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable(name = "categoryId") Long id,
                                               @RequestBody ReqCategory reqCategory){
        RespCategory category=service.updateCategory(id, reqCategory);

//        if(category==null){
//            return ResponseEntity.notFound().build();
//        }
        return ResponseEntity.ok(category);
    }

    //카테고리 삭제
    //TODO: 거래내역이 있을시 삭제 안됨 -> JdbcSQLIntegrityConstraintViolationException
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable(name = "categoryId") Long id){
        boolean isDeleted=service.deleteCategory(id);

        //if(isDeleted){
        return ResponseEntity.ok().build();
        //}
        //return ResponseEntity.badRequest().build();
    }

    @GetMapping("/categoryStatus")
    public ResponseEntity<?> getCategoryStatus(){
        List<RespCategoryStatus> categories= service.getCategoryStatus();
        return ResponseEntity.ok(categories);
    }

}
