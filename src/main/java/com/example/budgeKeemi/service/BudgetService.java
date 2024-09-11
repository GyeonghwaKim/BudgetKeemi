package com.example.budgeKeemi.service;

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

import java.time.LocalDate;
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

        //사용자의 카테고리 아이디 리스트
        List<Long> categoryIds = getCategoryIds(username);

        //사용자의 카테고리 아이디 리스트로 예산 리스트 조회
        List<Budget> budgets = repository.findAllByCategoryIdIn(categoryIds);
        
        Map<Long, Integer> useAmountMap = getUseAmountMap(budgets);

        return budgets.stream()
                .map(budget -> {
                    RespBudget respBudget = RespBudget.toDto(budget);
                    respBudget.updateUseAmount(useAmountMap.getOrDefault(respBudget.getCategoryId(),0));
                    return respBudget;
                })
                .toList();

    }




    public RespBudget createBudget(ReqBudget reqBudget,String username) {

        Category category = categoryService.getCategoryByCategoryId(reqBudget.getCategoryId());

        //소유자 검증
        validationAuthorization(username, category, "작성 권한이 없습니다");

        Budget budget=ReqBudget.toEntity(reqBudget);
        budget.addCategory(category);

        Budget saveBudget=repository.save(budget);

        return RespBudget.toDto(saveBudget);
    }

    public RespBudget updateBudget(Long id, ReqBudget reqBudget,String username) {

        Optional<Budget> _budget = repository.findById(id);

        if(_budget.isPresent()){
            Budget budget = _budget.get();

            Category category = categoryService.getCategoryByCategoryId(reqBudget.getCategoryId());

            //소유자 검증
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

            //소유자 검증
            validationAuthorization(username, category, "삭제 권한이 없습니다");

            repository.delete(budget);
            return true;
        }else{
            return false;
        }

    }

    private List<Long> getCategoryIds(String username) {
        return categoryService.getActiveCategoriesByUsername(username)
                .stream()
                .map(RespCategory::getId)
                .toList();
    }

    private static void validationAuthorization(String username, Category category, String message) {
        if(!category.getMember().getUsername().equals(username)){
            throw new UnauthorizedException(message);
        }
    }

    private Map<Long, Integer> getUseAmountMap(List<Budget> budgets) {

        Map<Long,Integer> useAmountMap=new HashMap<>();

        for(Budget budget: budgets){
            Long categoryId = budget.getCategory().getId();
            LocalDate startDate = budget.getStartDate();
            LocalDate endDate = budget.getEndDate();

            int sum = getTransactionsSum(categoryId, startDate, endDate);

            useAmountMap.put(categoryId, sum);
        }
        return useAmountMap;
    }

    private int getTransactionsSum(Long categoryId, LocalDate startDate, LocalDate endDate) {

        List<Transaction> transactions = transactionService.getTransactionsByCategoryIdAndDate(categoryId, startDate, endDate);

        return transactions.stream().mapToInt(Transaction::getAmount).sum();
    }
}
