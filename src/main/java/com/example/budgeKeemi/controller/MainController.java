package com.example.budgeKeemi.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String main(){
        return "main";
    }

    @GetMapping("/transactionPage")
    public String transactionPage(){
        return "transaction";
    }
    @GetMapping("/accountPage")
    public String accountPage(){
        return "account";
    }
    @GetMapping("/budgetPage")
    public String budgetPage(){
        return "budget";
    }
}
