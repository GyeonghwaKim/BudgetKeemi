package com.example.budgeKeemi.controller;

import com.example.budgeKeemi.dto.ReqBudget;
import com.example.budgeKeemi.dto.RespBudget;
import com.example.budgeKeemi.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/budgets")
@Controller
public class BudgetController {
    private final BudgetService service;

    //예산  목록 조회
    @GetMapping
    public ResponseEntity<?> getBudgets(){
        List<RespBudget> budgets= service.getBudgets();
        return ResponseEntity.ok(budgets);
    }

    //예산  생성
    @PostMapping
    public ResponseEntity<?> addBudget(@RequestBody ReqBudget reqBudget){

        RespBudget respBudget=this.service.createBudget(reqBudget);

        return new ResponseEntity<>(respBudget, HttpStatus.CREATED);
    }
    //예산 내역 상세조회
    @GetMapping("/{budgetId}")
    public ResponseEntity<?> getBudgetDetail(@PathVariable(name = "budgetId") Long id){

        RespBudget respBudget=service.getBudgetDetail(id);

        return ResponseEntity.ok(respBudget);

    }
    //예산 내역 수정
    @PutMapping("/{budgetId}")
    public ResponseEntity<?> updateBudget(@PathVariable(name = "budgetId") Long id,
                                               @RequestBody ReqBudget reqBudget){
        RespBudget budget=service.updateBudget(id,reqBudget);

//        if(budget==null){
//            return ResponseEntity.notFound().build();
//        }
        return ResponseEntity.ok(budget);
    }

    //예산 내역 삭제
    @DeleteMapping("/{budgetId}")
    public ResponseEntity<?> deleteBudget(@PathVariable(name = "budgetId") Long id){
        boolean isDeleted=service.deleteBudget(id);

        //if(isDeleted){
        return ResponseEntity.ok().build();
        //}
        //return ResponseEntity.badRequest().build();
    }

}
