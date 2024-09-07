package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.Budget;
import com.example.budgeKeemi.domain.Category;
import com.example.budgeKeemi.domain.Transaction;
import com.example.budgeKeemi.dto.ReqBudget;
import com.example.budgeKeemi.dto.RespBudget;
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

    public List<RespBudget> getBudgets() {

        List<Budget> budgets = repository.findAll();

        Map<Long,Integer> useAmountMap=new HashMap<>();

        for(Budget budget:budgets){
            Long categoryId = budget.getCategory().getId();
            List<Transaction> transactions = transactionService.getTransactionsByCategoryId(categoryId);
            useAmountMap.put(categoryId,transactions.stream().mapToInt(Transaction::getAmount).sum());
        }

        List<RespBudget> respBudgets = budgets.stream().map(RespBudget::toDto).toList();

        for(RespBudget respBudget:respBudgets){
            respBudget.updateUseAmount(useAmountMap.get(respBudget.getCategoryId()));
        }

        return respBudgets;
    }

    public RespBudget createBudget(ReqBudget reqBudget) {

        Budget budget=ReqBudget.toEntity(reqBudget);

        Category category = categoryService.getCategoryById(reqBudget.getCategoryId());

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

    public RespBudget updateBudget(Long id, ReqBudget reqBudget) {
        Optional<Budget> _budget = repository.findById(id);

        if(_budget.isPresent()){
            Budget budget = _budget.get();
            budget.updateAmount(reqBudget.getGoalAmount());
            budget.updateEndDate(reqBudget.getEndDate());

            Budget updateBudget = repository.save(budget);

            return RespBudget.toDto(updateBudget);
        }else{
            return null;
        }

    }

    public boolean deleteBudget(Long id) {
        Optional<Budget> _budget=repository.findById(id);

        if(_budget.isPresent()){
            repository.delete(_budget.get());
            return true;
        }else{
            return false;
        }

    }
}
