package com.example.budgeKeemi.controller;

import com.example.budgeKeemi.dto.req.ReqTransaction;
import com.example.budgeKeemi.dto.resp.DailySummary;
import com.example.budgeKeemi.dto.resp.ExpenseGraph;
import com.example.budgeKeemi.dto.resp.MonthlySummary;
import com.example.budgeKeemi.dto.resp.RespTransaction;
import com.example.budgeKeemi.oauth.CustomOAuth2User;
import com.example.budgeKeemi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/transactions")
@Controller
public class TransactionController {

    private final TransactionService service;

    //거래 목록 조회
    @GetMapping
    public ResponseEntity<?> getTransactions(Principal principal) {

        String username = getUsername(principal);
        List<RespTransaction> transactions = service.getTransactionsByUsername(username);
        return ResponseEntity.ok(transactions);
    }


    //거래 생성
    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody ReqTransaction reqTransaction, Principal principal) {
        String username = getUsername(principal);
        RespTransaction respTransaction = this.service.createTransaction(reqTransaction, username);

        return new ResponseEntity<>(respTransaction, HttpStatus.CREATED);
    }


    //거래내역 취소
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<?> cancelTransaction(@PathVariable(name = "transactionId") Long id, Principal principal) {
        String username = getUsername(principal);
        boolean isDeleted = service.cancelTransaction(id, username);

        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    //월별 거래내역 총 합계 출력
    @GetMapping("/monthlySummary/{yearMonth}")
    public ResponseEntity<?> getMonthlySummary(@PathVariable(name = "yearMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth, Principal principal) {

        String username = getUsername(principal);
        MonthlySummary monthlySummary = service.getMonthlySummary(yearMonth, username);
        return ResponseEntity.ok(monthlySummary);
    }

    //월별 거래 내역 일일 단위 출력
    @GetMapping("/monthly/{yearMonth}")
    public ResponseEntity<?> getDaySummary(@PathVariable(name = "yearMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth, Principal principal) {
        String username = getUsername(principal);
        List<DailySummary> dailySummaries = service.getDaySummary(yearMonth, username);

        return ResponseEntity.ok(dailySummaries);
    }

    //지출 그래프 데이터
    @GetMapping("/graph")
    public ResponseEntity<?> getGraphData(@RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                          @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate,
                                          Principal principal) {

        String username = getUsername(principal);
        List<ExpenseGraph> expenseGraphDatas = service.getExpenseGraph(startDate, endDate, username);

        return ResponseEntity.ok(expenseGraphDatas);
    }

    private static String getUsername(Principal principal) {
        String username = "";
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) principal;
            OAuth2User oAuth2User = authToken.getPrincipal();
            if (oAuth2User instanceof CustomOAuth2User) {
                CustomOAuth2User customOAuth2User = (CustomOAuth2User) oAuth2User;
                username = customOAuth2User.getUsername();
            }
        }
        return username;
    }

}
