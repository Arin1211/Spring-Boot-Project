package com.demo.bankapp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class BankApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    public void contextLoads() {
    }
}
