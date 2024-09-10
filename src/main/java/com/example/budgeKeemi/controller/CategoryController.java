package com.example.budgeKeemi.controller;

import com.example.budgeKeemi.dto.req.ReqCategory;
import com.example.budgeKeemi.dto.resp.RespCategory;
import com.example.budgeKeemi.dto.resp.RespCategoryStatus;
import com.example.budgeKeemi.oauth.CustomOAuth2User;
import com.example.budgeKeemi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/categories")
@Controller
public class CategoryController {

    private final CategoryService service;


    //활성화 카테고리 목록 조회
    @GetMapping("/active")
    public ResponseEntity<?> getActiveCategories(Principal principal) {
        String username = getUsername(principal);
        List<RespCategory> categories = service.getActiveCategoriesByUsername(username);
        return ResponseEntity.ok(categories);
    }

    //카테고리 생성
    @PostMapping
    public ResponseEntity<?> addCategories(@RequestBody ReqCategory reqCategory,Principal principal){

        String username = getUsername(principal);

        RespCategory respCategory=this.service.createCategory(reqCategory,username);

        return new ResponseEntity<>(respCategory, HttpStatus.CREATED);
    }


    //카테고리 상세조회
//    @GetMapping("/{categoryId}")
//    public ResponseEntity<?> getCategoryDetail(@PathVariable(name = "categoryId") Long id){
//
//        RespCategory respCategory=service.getCategoryDetail(id);
//
//        return ResponseEntity.ok(respCategory);
//
//    }
    //카테고리 수정
    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable(name = "categoryId") Long id,
                                               @RequestBody ReqCategory reqCategory,Principal principal){

        String username = getUsername(principal);

        RespCategory category=service.updateCategory(id, reqCategory, username);

//        if(category==null){
//            return ResponseEntity.notFound().build();
//        }
        return ResponseEntity.ok(category);
    }

    //카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable(name = "categoryId") Long id,Principal principal){

        String username = getUsername(principal);
        boolean isDeleted=service.changeInactiveCategory(id,username);

        if(isDeleted){
        return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/categoryStatus")
    public ResponseEntity<?> getCategoryStatus(){
        List<RespCategoryStatus> categories= service.getCategoryStatus();
        return ResponseEntity.ok(categories);
    }

    private static String getUsername(Principal principal) {

        String username="";

        if(principal instanceof OAuth2AuthenticationToken){
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) principal;
            OAuth2User oAuth2User = authToken.getPrincipal();

            if(oAuth2User instanceof CustomOAuth2User){
                CustomOAuth2User customOAuth2User = (CustomOAuth2User) oAuth2User;
                username=customOAuth2User.getUsername();

            }
        }
        return username;

    }
}
