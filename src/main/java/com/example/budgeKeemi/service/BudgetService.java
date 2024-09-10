package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.entity.Account;
import com.example.budgeKeemi.domain.entity.Budget;
import com.example.budgeKeemi.domain.entity.Category;
import com.example.budgeKeemi.domain.entity.Transaction;
import com.example.budgeKeemi.dto.req.ReqBudget;
import com.example.budgeKeemi.dto.resp.RespBudget;
import com.example.budgeKeemi.dto.resp.RespCategory;
import com.example.budgeKeemi.exception.excep.UnauthorizedException;
import com.example.budgeKeemi.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BudgetService {
    private final BudgetRepository repository;

    private final CategoryService categoryService;
    private final TransactionService transactionService;

    public List<RespBudget> getBudgetsByUsername(String username) {

        List<Long> categoryIds =
                categoryService.getCategoriesByUsername(username)
                        .stream()
                        .map(RespCategory::getId)
                        .toList();

        List<Budget> budgets = repository.findAllByCategoryIdIn(categoryIds);
//사용량
//        Map<Long,Integer> useAmountMap=new HashMap<>();
//
//        for(Budget budget:budgets){
//            Long categoryId = budget.getCategory().getId();
//            List<Transaction> transactions = transactionService.getTransactionsByCategoryId(categoryId);
//            int sum = transactions.stream().mapToInt(Transaction::getAmount).sum();
//            useAmountMap.put(categoryId, sum);
//        }

        List<RespBudget> respBudgets = budgets.stream().map(RespBudget::toDto).toList();

        for(RespBudget respBudget:respBudgets){
            respBudget.updateUseAmount(1);
        }

        return respBudgets;
    }

    public RespBudget createBudget(ReqBudget reqBudget,String username) {
        Category category = categoryService.getCategoryById(reqBudget.getCategoryId());

        validationAuthorization(username, category, "작성 권한이 없습니다");

        Budget budget=ReqBudget.toEntity(reqBudget);
        budget.addCategory(category);

        Budget saveBudget=repository.save(budget);

        RespBudget respBudget = RespBudget.toDto(saveBudget);

        return respBudget;
    }

    public RespBudget getBudgetDetail(Long id) {
        Optional<Budget> _budget=repository.findById(id);

        if(_budget.isPresent()){
            return RespBudget.toDto(_budget.get());
        }else{
            return null;
        }
    }

    public RespBudget updateBudget(Long id, ReqBudget reqBudget,String username) {
        Optional<Budget> _budget = repository.findById(id);

        if(_budget.isPresent()){
            Budget budget = _budget.get();

            Category category = categoryService.getCategoryById(reqBudget.getCategoryId());

            validationAuthorization(username, category, "수정 권한이 없습니다");

            budget.replaceCategory(category);
            budget.replaceGoalAmount(reqBudget.getGoalAmount());
            budget.replaceStartDate(reqBudget.getStartDate());
            budget.replaceEndDate(reqBudget.getEndDate());

            Budget updateBudget = repository.save(budget);

            return RespBudget.toDto(updateBudget);
        }else{
            return null;
        }

    }

    public boolean deleteBudget(Long id,String username) {
        Optional<Budget> _budget=repository.findById(id);

        if(_budget.isPresent()){

            Budget budget = _budget.get();
            Category category = budget.getCategory();
            validationAuthorization(username, category, "삭제 권한이 없습니다");

            repository.delete(budget);
            return true;
        }else{
            return false;
        }

    }

    private static void validationAuthorization(String username, Category category, String message) {
        if(!category.getMember().getUsername().equals(username)){
            throw new UnauthorizedException(message);
        }
    }
}
