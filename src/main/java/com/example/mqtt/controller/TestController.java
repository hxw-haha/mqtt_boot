package com.example.mqtt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping(value = "/")
    public String init() {
        return "spring boot war hello word";
    }

    @RequestMapping(value = "/helloWord")
    public String helloWord() {
        return "hello word";
    }

}
