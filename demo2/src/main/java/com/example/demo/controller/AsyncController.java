package com.example.demo.controller;

import com.example.demo.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AsyncController {

    @Autowired
    AsyncService asyncService;

    @GetMapping("/async")
    public String goAsync() throws Exception{
        for(int a = 0; a < 1000; a++){
            asyncService.onAsync(a);
        }

        String str ="Hello World";
        return str;
    }
}
