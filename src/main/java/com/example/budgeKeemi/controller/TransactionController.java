package com.example.budgeKeemi.controller;

import com.example.budgeKeemi.dto.DailySummary;
import com.example.budgeKeemi.dto.MonthlySummary;
import com.example.budgeKeemi.dto.ReqTransaction;
import com.example.budgeKeemi.dto.RespTransaction;
import com.example.budgeKeemi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/transactions")
@Controller
public class TransactionController {

    private final TransactionService service;

    //거래 목록 조회
    @GetMapping
    public ResponseEntity<?> getTransactions(){
        List<RespTransaction> transactions= service.getTransactions();
        return ResponseEntity.ok(transactions);
    }

    //거래 생성
    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody ReqTransaction reqTransaction){

        RespTransaction respTransaction=this.service.createTransaction(reqTransaction);

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

}
