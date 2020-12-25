package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class DemoRepository {

    public void get() {
        System.out.println("DemoRepository.get");
    }
}
