package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemoService {

    @Autowired
    private DemoRepository demoRepository;

    void start() {
        System.out.println("DemoService.start");

        demoRepository.get();
    }

}
