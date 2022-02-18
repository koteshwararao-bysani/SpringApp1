package com.demo.SpringApp1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo/springApp1")
public class SpringApp1Controller {

    @PostMapping("/hi")
    public ResponseEntity<String> sayHiMsg(@RequestParam(required = true) String name){
        if(name != null){
            return ResponseEntity.ok("Hi Welcome .... "+name);
        }else{
            return ResponseEntity.ok("Hi Hello World ");
        }
    }
}
