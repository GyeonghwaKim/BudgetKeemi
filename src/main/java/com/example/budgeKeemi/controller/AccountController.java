package com.example.budgeKeemi.controller;

import com.example.budgeKeemi.dto.ReqAccount;
import com.example.budgeKeemi.dto.RespAccount;
import com.example.budgeKeemi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/accounts")
@Controller
public class AccountController {

    private final AccountService accountService;

    //계좌 목록 조회
    @GetMapping
    public ResponseEntity<?> getAccounts(){

        List<RespAccount> accounts=accountService.getAccounts();

        return ResponseEntity.ok(accounts);
    }


    //계좌 생성
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody ReqAccount reqAccount){

        RespAccount newAccount=accountService.createAccount(reqAccount);

        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    //계좌 상세 조회
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountDetails(@PathVariable(name = "accountId") Long id){

        RespAccount account = accountService.getAccountDetails(id);

//        if (account == null) {
//            return ResponseEntity.notFound().build();
//        }

        return ResponseEntity.ok(account);
    }

    //계좌 수정
    @PutMapping("/{accountId}")
    public ResponseEntity<?> updateAccountDetails(@PathVariable(name = "accountId") Long id,
                                                  @RequestBody  ReqAccount reqAccount){

        RespAccount updateAccount=accountService.updateAccountDetails(id,reqAccount);

//        if(updateAccount==null){
//
//            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
//            return ResponseEntity.notFound().build();
//        }

        return ResponseEntity.ok(updateAccount);
    }

    //계좌 삭제
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable(name = "accountId") Long id){

        boolean isDeleted=accountService.deleteAccount(id);

        //if(isDeleted){
            return ResponseEntity.ok().build();
        //}else{
        //    return ResponseEntity.notFound().build();
        //}

    }

    @GetMapping("/accountType")
    public ResponseEntity<?> getAccountType(){
        List<String> typeList=accountService.getAccountType();

        return ResponseEntity.ok(typeList);
    }

}
