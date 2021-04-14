package com.magazine.dao;

import com.magazine.MagazineApplication;
import com.magazine.constant.RedisConsts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MagazineApplication.class)
class ConfigRepositoryTest {

    @Autowired
    private ConfigRepository configRepository;

    @Test
    void testSave() {
        configRepository.saveConfig("runTime", "100");

        final Set<String> configKeys = configRepository.getConfigKeys();
        configKeys.forEach(System.out::println);

        final String runTime = configRepository.getConfig("runTime");
        System.out.println("runTime = " + runTime);

        final String searchField = configRepository.getConfig(RedisConsts.CONFIG_KEY_SEARCH_FIELD);
        System.out.println("searchField = " + searchField);
    }
}