package com.example.budgeKeemi.controller;

import com.example.budgeKeemi.dto.req.ReqBudget;
import com.example.budgeKeemi.dto.resp.RespBudget;
import com.example.budgeKeemi.oauth.CustomOAuth2User;
import com.example.budgeKeemi.service.BudgetService;
import jakarta.validation.Valid;
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
@RequestMapping("/budgets")
@Controller
public class BudgetController {
    private final BudgetService service;

    //예산 목록 조회
    @GetMapping
    public ResponseEntity<?> getBudgets(Principal principal){

        String username = getUsername(principal);

        List<RespBudget> budgets= service.getBudgetsByUsername(username);

        return ResponseEntity.ok(budgets);
    }

    //예산  생성
    @PostMapping
    public ResponseEntity<?> addBudget(@Valid  @RequestBody ReqBudget reqBudget, Principal principal){

        String username = getUsername(principal);

        RespBudget respBudget=this.service.createBudget(reqBudget,username);

        return new ResponseEntity<>(respBudget, HttpStatus.CREATED);
    }

    //예산 내역 수정
    @PutMapping("/{budgetId}")
    public ResponseEntity<?> updateBudget(@PathVariable(name = "budgetId") Long id,
                                          @Valid @RequestBody ReqBudget reqBudget,Principal principal){

        String username = getUsername(principal);

        RespBudget budget=service.updateBudget(id,reqBudget,username);

        if(budget==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(budget);
    }

    //예산 내역 삭제
    @DeleteMapping("/{budgetId}")
    public ResponseEntity<?> deleteBudget(@PathVariable(name = "budgetId") Long id,Principal principal){

        String username = getUsername(principal);

        boolean isDeleted=service.deleteBudget(id,username);

        if(isDeleted){
        return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
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
