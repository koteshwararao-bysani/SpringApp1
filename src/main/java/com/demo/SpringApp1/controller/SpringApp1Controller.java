package com.demo.SpringApp1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo/springApp1")
public class SpringApp1Controller {
    @GetMapping("/hi")
    public ResponseEntity<String> sayHiMsg(@RequestParam String name){
        System.out.println("i am from main SpringApp1Controller ::  sayHiMsg ");
        if(name != null){
            return ResponseEntity.ok("Hi Welcome .... "+name);
        }else{
            return ResponseEntity.ok("Hi Hello World ");
        }
    }
}
