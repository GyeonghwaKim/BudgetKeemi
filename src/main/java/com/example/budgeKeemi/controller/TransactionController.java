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
    public ResponseEntity<?> getTransactions(Principal principal){

        String username = getUsername(principal);
        List<RespTransaction> transactions= service.getTransactionsByUsername(username);
        return ResponseEntity.ok(transactions);
    }



    //거래 생성
    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody ReqTransaction reqTransaction,Principal principal){
        String username = getUsername(principal);
        RespTransaction respTransaction=this.service.createTransaction(reqTransaction,username);

        return new ResponseEntity<>(respTransaction, HttpStatus.CREATED);
    }
    //거래내역 상세조회
    @GetMapping("/{transactionId}")
    public ResponseEntity<?> getTransactionDetail(@PathVariable(name = "transactionId") Long id){

        RespTransaction respTransaction=service.getTransactionDetail(id);

        return ResponseEntity.ok(respTransaction);

    }
    //거래내역 수정
    @PutMapping("/{transactionId}")
    public ResponseEntity<?> updateTransaction(@PathVariable(name = "transactionId") Long id,
                                               @RequestBody ReqTransaction reqTransaction){
        RespTransaction transaction=service.updateTransaction(id,reqTransaction);

//        if(transaction==null){
//            return ResponseEntity.notFound().build();
//        }
        return ResponseEntity.ok(transaction);
    }

    //거래내역 삭제
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<?> deleteTransaction(@PathVariable(name = "transactionId") Long id){
        boolean isDeleted=service.deleteTransaction(id);

        //if(isDeleted){
            return ResponseEntity.ok().build();
        //}
        //return ResponseEntity.badRequest().build();
    }


    //한달 거래내역 총 합계 출력
    @GetMapping("/monthlySummary/{yearMonth}")
    public ResponseEntity<?> getMonthlySummary(@PathVariable(name = "yearMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth){
        MonthlySummary monthlySummary=service.getMonthlySummary(yearMonth);
        return ResponseEntity.ok(monthlySummary);
    }

    //한달 거래 내역 리스트
    @GetMapping("/monthly/{yearMonth}")
    public ResponseEntity<?> getDaySummary(@PathVariable(name = "yearMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth){
        List<DailySummary> dailySummaries=service.getDaySummary(yearMonth);

        return ResponseEntity.ok(dailySummaries);
    }

    @GetMapping("/graph")
    public ResponseEntity<?> getGraphData(@RequestParam(name="startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                          @RequestParam(name="endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate){
        List<ExpenseGraph> expenseGraphDatas=service.getExpenseGraph(startDate,endDate);

        return ResponseEntity.ok(expenseGraphDatas);
    }

    private static String getUsername(Principal principal) {
        String username="";
        if(principal instanceof OAuth2AuthenticationToken){
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) principal;
            OAuth2User oAuth2User = authToken.getPrincipal();
            if(oAuth2User instanceof CustomOAuth2User){
                CustomOAuth2User customOAuth2User = (CustomOAuth2User) oAuth2User;
                username= customOAuth2User.getUsername();
            }
        }
        return username;
    }

}
