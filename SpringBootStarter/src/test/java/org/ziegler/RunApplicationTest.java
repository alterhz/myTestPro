package org.ziegler;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RunApplication.class)
@SpringBootApplication
public class RunApplicationTest {
    @Test
    public void contextLoads() {
        System.out.println("RunApplicationTest.contextLoads");
    }
}
