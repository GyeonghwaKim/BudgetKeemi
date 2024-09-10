package com.example.budgeKeemi.controller;

import com.example.budgeKeemi.dto.req.ReqAccount;
import com.example.budgeKeemi.dto.resp.RespAccount;
import com.example.budgeKeemi.oauth.CustomOAuth2User;
import com.example.budgeKeemi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/accounts")
@Controller
public class AccountController {

    private final AccountService accountService;

    //계좌 목록 조회
    @GetMapping
    public ResponseEntity<?> getAccounts(Principal principal){

        List<RespAccount> accounts=new ArrayList<>();
        String username = getUsername(principal);

        accounts=accountService.getAccountsByUsername(username);
        return ResponseEntity.ok(accounts);
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


    //계좌 생성
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody ReqAccount reqAccount,Principal principal) {
        
        String username = getUsername(principal);
        RespAccount newAccount = accountService.createAccount(reqAccount,username);

        return ResponseEntity.ok(newAccount);

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
                                                  @RequestBody  ReqAccount reqAccount,Principal principal){

        String username = getUsername(principal);
//TODO updateAccount로 변경
        RespAccount updateAccount=accountService.updateAccountDetails(id,reqAccount,username);

//        if(updateAccount==null){
//
//            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
//            return ResponseEntity.notFound().build();
//        }

        return ResponseEntity.ok(updateAccount);
    }

    //계좌 삭제
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable(name = "accountId") Long id,Principal principal){

        String username = getUsername(principal);

        boolean isDeleted=accountService.deleteAccount(id,username);

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
